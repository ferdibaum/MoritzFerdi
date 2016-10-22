package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import Models.TexturedModel;
import entities.Entity;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;


public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = { 
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
		};
		
		int[] indices = { 
				0,1,3,
				3,1,2
		};
		
		float[] texturedCoords = {
			0,0,
			0,1,
			1,1,
			1,0
		};
		
		RawModel model = loader.loadToVAO(vertices, texturedCoords, indices);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("Untitled"));
		
		TexturedModel staticModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-1),0,0,0,1);
		
		while(!Display.isCloseRequested()){
			//gameLogic
			entity.increasePosition(0, 0, -0.001f);
			renderer.prepare();
			shader.start();
			renderer.render(entity,shader);
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
