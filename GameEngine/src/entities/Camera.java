package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,5,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}
	
	public static double getSin(double angleInDegrees) {
		 double angleInRadians = Math.toRadians(angleInDegrees);
		 return Math.sin(angleInRadians);
		}
	
	public static double getCos(double angleInDegrees) {
		 double angleInRadians = Math.toRadians(angleInDegrees);
		 return Math.cos(angleInRadians);
		}
	
	public void move(){
		Vector3f diff = new Vector3f();
		diff.x = (float) (getSin((double)yaw));
		diff.y = 0; 
		diff.z = (float) (getCos((double)yaw));
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.x = position.x + diff.x * 0.2f;
			position.y = position.y + diff.y;
			position.z = position.z + diff.z * -0.2f;;

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
		System.out.println(yaw + "\t" + diff.x + "\t" + diff.z);
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
