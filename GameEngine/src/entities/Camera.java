package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(-51,70,25);
	private float pitch = 43;
	private float yaw = 9.5f;
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
		/* DISABLED
		Vector3f diff = new Vector3f();
		diff.x = (float) (getSin((double)yaw));
		diff.y = 0; 
		diff.z = (float) (getCos((double)yaw));
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.x = position.x + diff.x * 0.2f;
			position.z = position.z + diff.z * -0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x = position.x + diff.z * 0.2f;
			position.z = position.z + diff.x * 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x = position.x + diff.z * -0.2f;
			position.z = position.z + diff.x * -0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.x = position.x + diff.x * -0.2f;
			position.z = position.z + diff.z * 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			yaw+=0.6f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			yaw-=0.6f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y = position.y + 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X)){
			if(position.y > 1) {
				position.y = position.y - 0.2f;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			pitch+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_T)){
			pitch-=0.2f;
		}
		System.out.println(position.x + "\t" + position.y + "\t" + position.z + "\t" + yaw + "\t" + pitch);
		*/
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