package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;

public class Rock extends Entity {


	public static List<Rock> rocks = new ArrayList<Rock>();

	public Rock(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int hit, int life) {
		super(model, position, rotX, rotY, rotZ, scale, hit, life);
		rocks.add(this);
	}

	public void update() {
		if (this.getLife() <= 0) {
			rocks.remove(this);
			this.destroy();
		}
	}

}
