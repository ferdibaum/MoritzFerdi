package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import Models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {
	
	private static Player player;
	private static Terrain terrain;
	private static Camera camera;
	private static MasterRenderer renderer;
	private static List<Entity> allEntities;
	private static Light light;
	private static MousePicker mPicker;
	
	public static void main(String[] args) {
		
		Random random = new Random();
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//******TERRAIN TEXTURE
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,gTexture,bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//**************
		
		renderer = new MasterRenderer();
		
		RawModel modelNova = OBJLoader.loadObjModel("Nova", loader);
		TexturedModel textModelNova = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("Untitled")));
		RawModel modelTree = OBJLoader.loadObjModel("tree", loader);
		TexturedModel textModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("tree")));
		
		allEntities = new ArrayList<Entity>();
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		
		light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
		
		camera = new Camera();
		
		terrain = new Terrain(-0.5f,0,loader, texturePack, blendMap, "heightMapTest");
		
		player = new Player(textModelNova, new Vector3f(0, terrain.getHeightOfTerrain(0,-50), -50), 0, 0, 0, 1, textModelTree);
		
		mPicker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		for(int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 - 50 ;
			float z = random.nextFloat() * -300 ;
			float y = terrain.getHeightOfTerrain(x, z);
			allEntities.add(new Entity(textModelTree, new Vector3f(x, y ,z), 0, random.nextFloat() * 180f, 0f, 1f));
		}
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		// ************* MAIN GAME LOOP
		
		
		while(!Display.isCloseRequested()){
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				//System.out.println("FPS: " + frames + " UPDATES: " + updates);
				frames = 0;
				updates = 0;
				
			}

			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	// ************* END GAME LOOP ***********************
	
	private static void update(){
		camera.move();
		player.move(terrain);
		player.update();
		mPicker.update();
		Vector3f mousePos = mPicker.getCurrentTerrainPoint();
		if (mousePos != null){
			//System.out.println(mousePos.x + "\t" + mousePos.y + "\t" + mousePos.z);
		}
	}
	
	private static void render(){
		renderer.render(light, camera);
		renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer);
		for(Entity entity:allEntities){
			renderer.processEntity(entity);							
		}
		DisplayManager.updateDisplay();
	}
}
