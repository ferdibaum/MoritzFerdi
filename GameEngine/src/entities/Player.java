package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import engineTester.MainGameLoop;
import renderEngine.MasterRenderer;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 0.2f;
	private static final float TURN_SPEED = 1.7f;

	// private static final float TERRAIN_HEIGHT = 0;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private TexturedModel bulletModel;
	private long lastShoot;
	private long deltaShoot;
	
	private int atkSpeed;

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			TexturedModel bulletModel) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.bulletModel = bulletModel;
		lastShoot = System.currentTimeMillis();
		
		atkSpeed = 500;
	}

	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed, 0);
		float distance = currentSpeed;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y != terrainHeight) {
			super.getPosition().y = terrainHeight;
		}
	}

	private void checkInputs() {
		
		/*
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
		
		*/

		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			long now = System.currentTimeMillis();
			deltaShoot = now  - lastShoot;
			if (deltaShoot > atkSpeed) {
				Vector3f bullletPos = new Vector3f();
				bullletPos.x = this.getPosition().x;
				bullletPos.y = this.getPosition().y + 3;
				bullletPos.z = this.getPosition().z;
				bullets.add(new Projectile(bulletModel, bullletPos, 0.0f, this.getRotY(), 0.0f, 1.0f));
				deltaShoot = 0; 
				lastShoot = now;
			}
		}
		
		if(Mouse.isButtonDown(0)){
			Vector3f mousePos = new Vector3f().set(MainGameLoop.getMPicker().getCurrentTerrainPoint());
			Vector3f dir = Vector3f.sub(mousePos, this.getPosition(), null);
			this.increasePosition(dir.x / dir.length(), dir.y/ dir.length(), dir.z/ dir.length()); 
		}
	}

	public void update() {
		for (int i = 0; i < bullets.size(); i++) {
			Projectile bullet = bullets.get(i);
			float dx = (float) (Projectile.SPEED * Math.sin(Math.toRadians(bullet.getRotY())));
			float dz = (float) (Projectile.SPEED * Math.cos(Math.toRadians(bullet.getRotY())));
			bullet.increasePosition(dx, 0, dz);

			if (Vector3f.sub(bullet.getPosition(), bullet.getStart(), null).length() > Projectile.RANGE) {
				bullets.remove(bullet);
			}

		}
		// System.out.println(bullets.size());

	}

	public void render(MasterRenderer renderer) {
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}
}
