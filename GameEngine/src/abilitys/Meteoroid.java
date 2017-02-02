package abilitys;

import org.lwjgl.util.vector.Vector3f;

import entities.Object;
import models.TexturedModel;
import particles.ParticleSystem;

public class Meteoroid extends Object {

	private Vector3f destiny;
	private float speed = 0.7f;
	private Vector3f start;

	public Meteoroid(TexturedModel model, ParticleSystem pSys, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, int hit, int life, Vector3f start, Vector3f destiny) {
		super(model, pSys, position, rotX, rotY, rotZ, scale, hit, life);
		this.start = start;
		this.destiny = destiny;
	}

	public Vector3f getDestiny() {
		return destiny;
	}

	public void setDestiny(Vector3f destiny) {
		this.destiny = destiny;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Vector3f getStart() {
		return start;
	}

	public void setStart(Vector3f start) {
		this.start = start;
	}

}
