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
	private static String title = "FPS: 0 UPDATES: 0" ;
	
	public static void main(String[] args) {		
		DisplayManager.createDisplay(); // Fenster erzeugen
		Loader loader = new Loader();
		
		// Terrain mit Texturen erstellen
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(-0.5f, 0, loader, texturePack, blendMap, "heightMapTest");
		
		renderer = new MasterRenderer();
		
		RawModel modelNova = OBJLoader.loadObjModel("Nova", loader);
		TexturedModel textModelNova = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("Untitled")));
		RawModel modelTree = OBJLoader.loadObjModel("bunny", loader);
		TexturedModel textModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("tree")));
		
		
		light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
		
		camera = new Camera();
		
		player = new Player(textModelNova, new Vector3f(0, terrain.getHeightOfTerrain(0,-50), -50), 0, 0, 0, 1, textModelTree);
		
		mPicker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		
		// Paar B‰ume pflanzen
		allEntities = new ArrayList<Entity>();
		//Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Random random = new Random();
		for(int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 - 50 ;
			float z = random.nextFloat() * -300 ;
			float y = terrain.getHeightOfTerrain(x, z);
			allEntities.add(new Entity(textModelTree, new Vector3f(x, y ,z), 0, random.nextFloat() * 180f, 0f, 1f));
		}
		

		/*----------------------------
		 *   MAIN GAME LOOP
		 */
		long lastTime = System.nanoTime();
		final double AMOUNT_OF_TICKS = 60.0;
		final double NS = 1000000000 / AMOUNT_OF_TICKS;
		double delta = 0;
		
		// Updates und Frames f¸r den Fenstertitel nachhalten
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		// Solange das Fenster noch nicht geschlossen wurde
		while(!Display.isCloseRequested()){
			// Physikalische Berechnungen machen, rendern aufschieben
			final long NOW = System.nanoTime();
			delta += (NOW - lastTime) / NS;
			lastTime = NOW;
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			
			// Alles rendern
			render(title);
			
			// Updates und Frames im Fenstertitel anzeigen
			frames++;
			if((System.currentTimeMillis() - timer) > 1000){
				timer += 1000;
				title = "FPS: " + frames + " UPDATES: " + updates;
				frames = 0;
				updates = 0;
			}
		}
		/*
		 *   END MAIN GAME LOOP
		 *---------------------------*/
		
		// Beim Schlieﬂen aufr‰umen
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	// Physikalische Berechnungen und den ganzen Kram machen
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

	// Alles rendern und anzeigen
	private static void render(String s){
		renderer.render(light, camera);
		renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer);
		for(Entity entity:allEntities){
			renderer.processEntity(entity);							
		}
		DisplayManager.updateDisplay(s);
	}
}
