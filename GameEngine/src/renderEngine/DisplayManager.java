package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	public static void createDisplay(){
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}	
	}
	
	
	
}
