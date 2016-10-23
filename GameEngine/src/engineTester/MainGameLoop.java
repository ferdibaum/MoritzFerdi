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
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;


public class MainGameLoop {
	
	public static void main(String[] args) {
		
		Random random = new Random();
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("Untitled")));
		
		List<Entity> allEntities = new ArrayList<Entity>();
		
		for(int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			allEntities.add(new Entity(staticModel, new Vector3f(x, y ,z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
		}
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Light light = new Light(new Vector3f(0,0,-20),new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("Untitled")));
		Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("Untitled")));
		
		while(!Display.isCloseRequested()){
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
