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

public class MainGameLoop {
	
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
		
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel modelNova = OBJLoader.loadObjModel("Nova", loader);
		TexturedModel textModelNova = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("Untitled")));
		RawModel modelTree = OBJLoader.loadObjModel("tree", loader);
		TexturedModel textModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("tree")));
		
		List<Entity> allEntities = new ArrayList<Entity>();
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		Terrain terrain = new Terrain(-0.5f,0,loader, texturePack, blendMap, "heightMapTest");
		
		Player player = new Player(textModelNova, new Vector3f(0, terrain.getHeightOfTerrain(0,-50), -50), 0, 0, 0, 1, textModelTree);
		
		for(int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 - 50 ;
			float z = random.nextFloat() * -300 ;
			float y = terrain.getHeightOfTerrain(x, z);
			allEntities.add(new Entity(textModelTree, new Vector3f(x, y ,z), 0, random.nextFloat() * 180f, 0f, 1f));
		}
		
		while(!Display.isCloseRequested()){
			//camera.move();
			player.move(terrain);
			player.update(renderer);
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			for(Entity entity:allEntities){
				renderer.processEntity(entity);							
			}
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
