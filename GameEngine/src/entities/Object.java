package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import particles.ParticleSystem;

public class Object extends Entity {


	public static List<Object> objects = new ArrayList<Object>();

	public Object(TexturedModel model, ParticleSystem pSys, Vector3f position, float rotX, float rotY, float rotZ, float scale, int hit, int life) {
		super(model, position, rotX, rotY, rotZ, scale, hit, life);
		this.pSys = pSys;
		objects.add(this);
	}

	public void update() {
		if (this.getLife() == 0) {
			objects.remove(this);
			Vector3f pos = this.getPos();
			pos.y += 2;
			pSys.generateParticles(pos);
			this.destroy();
		}
	}

}
