package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import Models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import testing.Projectile;
import textures.ModelTexture;

public class Player extends Entity {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;

	// private static final float TERRAIN_HEIGHT = 0;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private TexturedModel bulletModel;

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			TexturedModel bulletModel) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.bulletModel = bulletModel;
	}

	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y != terrainHeight) {
			super.getPosition().y = terrainHeight;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			Vector3f bullletPos = new Vector3f();
			bullletPos.x = this.getPosition().x;
			bullletPos.y = this.getPosition().y;
			bullletPos.z = this.getPosition().z;
			bullets.add(new Projectile(bulletModel, bullletPos, 0.0f, 0.0f, 0.0f, 1.0f));
		}
	}

	public void update(MasterRenderer renderer) {
		for (int i = 0; i < bullets.size(); i++) {
			Projectile bullet = bullets.get(i);
			bullet.getPosition().x += Projectile.SPEED;
			if (Math.abs(bullet.getPosition().x - bullet.getStart().x) > Projectile.RANGE) {
				bullets.remove(bullet);
			}

		}
		System.out.println(bullets.size());
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}

}
