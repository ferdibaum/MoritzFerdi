package testing;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import entities.Entity;

public class Projectile extends Entity{
	
	public static float SPEED = 0.2f;

	public static float RANGE = 50;
	
	private Vector3f start = new Vector3f();

	public Projectile(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.start.x = position.x;
		this.start.y = position.y;
		this.start.z = position.z;
	}
	
	public Vector3f getStart(){
		return start;
	}

	
}
