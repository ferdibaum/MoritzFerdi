package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;

public class Enemy extends Entity {


	public static List<Enemy> enemies = new ArrayList<Enemy>();

	public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int hit, int life) {
		super(model, position, rotX, rotY, rotZ, scale, hit, life);
		enemies.add(this);
	}

	public void update() {
		if (this.getLife() <= 0) {
			enemies.remove(this);
			this.destroy();
		}
	}

}
