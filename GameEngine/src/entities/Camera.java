package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,5,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}
	
	public void move(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			Vector3f diff = new Vector3f();
			diff.x = (float) ( -0.2f * Math.sin((double)yaw)); diff.y = 0; diff.z = (float) ( -0.2f * Math.cos((double)yaw));
			position.x = position.x + diff.x;
			position.y = position.y + diff.y;
			position.z = position.z + diff.z;

		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			yaw+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			yaw-=0.2f;
		}
		System.out.println(yaw);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
}
