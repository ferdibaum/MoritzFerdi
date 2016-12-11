package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;


public class Camera {

	private static final float SPEED = 0.5f;
	
	
	private Vector3f position = new Vector3f(-38,55,-22);
	private float yaw = 20;
	private float pitch = 54;
	private float roll;
	
	public Camera(){}
	
	public void move(Player player){
		Vector3f diff = new Vector3f();
		diff.x = (float) Math.sin(Math.toRadians(yaw));
		diff.y = 0; 
		diff.z = (float) Math.cos(Math.toRadians(yaw));
		
		
		/* ONLY USE FOR CAMERA ADJUSTMENT
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.x = position.x + diff.x * SPEED;
			position.z = position.z + diff.z * -SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x = position.x + diff.z * SPEED;
			position.z = position.z + diff.x * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x = position.x + diff.z * -SPEED;
			position.z = position.z + diff.x * -SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.x = position.x + diff.x * -SPEED;
			position.z = position.z + diff.z * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			yaw+=0.6f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			yaw-=0.6f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			position.y = position.y + SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X)){
			if(position.y > 1) {
				position.y = position.y - SPEED;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			pitch+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_T)){
			pitch-=0.2f;
		}
		System.out.println(position.x + "\t" + position.y + "\t" + position.z + "\t" + yaw + "\t" + pitch);
		System.out.println(Mouse.getDWheel());
		 ***************************************/

				
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			Vector3f playerPos = player.getPosition();
			position.x = playerPos.x - 15;
			position.z = playerPos.z + 41;
		}
				
		if(Mouse.getX() < 30){
			position.x = position.x + diff.z * -SPEED;
			position.z = position.z + diff.x * -SPEED;
		}
		if(Mouse.getX() > (DisplayManager.WIDTH - 30)){
			position.x = position.x + diff.z * SPEED;
			position.z = position.z + diff.x * SPEED;
		}
		if(Mouse.getY() < 30){
			position.x = position.x + diff.x * -SPEED;
			position.z = position.z + diff.z * SPEED;
		}
		if(Mouse.getY() > (DisplayManager.HEIGHT - 30)){
			position.x = position.x + diff.x * SPEED;
			position.z = position.z + diff.z * -SPEED;
		}
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