package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;


public class Camera {

	private static final float SPEED = 0.5f;
	private static final float ZOOM_SPEED = 2;
	
	
	private Vector3f position = new Vector3f(-38,55,-22);
	private float yaw = 20;
	private float pitch = 54;
	private float roll;
	
	public Camera(){}
	
	public void move(Player player){
		
		Vector2f diff1 = new Vector2f();
		diff1.x = (float) Math.sin(Math.toRadians(yaw));
		diff1.y = (float) Math.cos(Math.toRadians(yaw));
		
		Vector2f diff2 = new Vector2f();
		diff2.x = (float) Math.sin(Math.toRadians(pitch));
		diff2.y = (float) Math.cos(Math.toRadians(pitch));
		
		
		/* ONLY USE FOR CAMERA ADJUSTMENT
		Vector3f diff = new Vector3f();
		diff.x = (float) Math.sin(Math.toRadians(yaw));
		diff.z = (float) Math.cos(Math.toRadians(yaw)); 
		diff.y = (float) position.y;
		
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
		 ***************************************/

		int dWheel = Mouse.getDWheel() / 120;
		if(dWheel < 0 && position.y <= 450){
			position.x = position.x + diff1.x * diff2.y * ZOOM_SPEED * dWheel;
			position.z = position.z + diff1.y * diff2.y * -ZOOM_SPEED * dWheel;
			position.y = position.y - diff2.x * ZOOM_SPEED * dWheel;
		}
		if(dWheel > 0 && position.y >= 10){
			position.x = position.x + diff1.x * diff2.y * ZOOM_SPEED * dWheel;
			position.z = position.z + diff1.y * diff2.y * -ZOOM_SPEED * dWheel;
			position.y = position.y - diff2.x * ZOOM_SPEED * dWheel;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			Vector3f playerPos = player.getPosition();
			position.x = playerPos.x - 15;
			position.z = playerPos.z + 41;
			position.y = 55;
		}
		if(Mouse.getX() < 30){
			position.x = position.x + diff1.y * -SPEED;
			position.z = position.z + diff1.x * -SPEED;
		}
		if(Mouse.getX() > (DisplayManager.WIDTH - 30)){
			position.x = position.x + diff1.y * SPEED;
			position.z = position.z + diff1.x * SPEED;
		}
		if(Mouse.getY() < 30){
			position.x = position.x + diff1.x * -SPEED;
			position.z = position.z + diff1.y * SPEED;
		}
		if(Mouse.getY() > (DisplayManager.HEIGHT - 30)){
			position.x = position.x + diff1.x * SPEED;
			position.z = position.z + diff1.y * -SPEED;
		}
	}
	
	public void invPitch(){
		this.pitch = -pitch;
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