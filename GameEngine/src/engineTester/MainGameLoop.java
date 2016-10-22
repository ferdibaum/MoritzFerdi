package engineTester;

import org.lwjgl.opengl.Display;

import Models.RawModel;
import Models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;


/// TO_DO:  3, 4, 5



public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
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
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		while(!Display.isCloseRequested()){
			//gameLogic
			renderer.prepare();
			shader.start();
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
