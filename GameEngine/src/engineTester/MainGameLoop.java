package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = { 
				// Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				// Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while(!Display.isCloseRequested()){
			//gameLogic
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
