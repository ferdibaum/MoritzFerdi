package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Projectile extends Entity{
	
	public static float SPEED = 0.4f;
	public static float RANGE = 50;
	public static final int HITBOX = 0;
	
	private Vector3f start = new Vector3f();
	public int delta;

	public Projectile(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale, HITBOX, 0);
		this.start.x = position.x;
		this.start.y = position.y;
		this.start.z = position.z;
		delta =0;
	}
	
	public Vector3f getStart(){
		return start;
	}

	
}
