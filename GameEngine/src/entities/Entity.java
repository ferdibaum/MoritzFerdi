package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import particles.ParticleSystem;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	private int life;

	private int hitBox;
	
	protected ParticleSystem pSys;

	public static List<Entity> entities = new ArrayList<Entity>();

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int hit, int life) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.hitBox = hit;
		this.life = life;
		entities.add(this);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void destroy() {
		entities.remove(this);
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public Entity colliding() {
		Entity entity = null;
		for (Entity col : Entity.entities) {
			if (col != this
					&& ((Vector2f
							.sub(new Vector2f(this.getPosition().x, this.getPosition().z),
									new Vector2f(col.getPosition().x, col.getPosition().z), null)
							.length() - this.getHitBox()) - col.getHitBox()) < 1) {
				entity = col;
				break;
			}
		}
		return entity;
	}

	private int getHitBox() {
		return hitBox;
	}

}
