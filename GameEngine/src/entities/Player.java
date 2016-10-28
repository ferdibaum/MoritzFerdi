package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import engineTester.MainGameLoop;
import renderEngine.MasterRenderer;
import terrains.Terrain;

public class Player extends Entity {

	private static final int TURNSTEPS = 3;
	private static final float MAX_SPEED = 0.2f;
	private static final int HITBOX = 1;
	
	// private static final float TERRAIN_HEIGHT = 0;
	private float speed = 0.2f;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private TexturedModel bulletModel;
	private long lastShoot;
	private long deltaShoot;

	private int atkSpeed;

	private boolean shooting;
	private boolean moving;
	private Vector3f destination = new Vector3f();

	private float oneRot;
	private int turnesDone;

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			TexturedModel bulletModel, int life) {
		super(model, position, rotX, rotY, rotZ, scale, HITBOX, life);
		this.bulletModel = bulletModel;
		lastShoot = System.currentTimeMillis();

		atkSpeed = 500;
		destination.set(this.getPosition());
		moving = false;
		turnesDone = 0;
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

		if (Mouse.isButtonDown(0)) {
			shooting = true;
			long now = System.currentTimeMillis();
			deltaShoot = now - lastShoot;
			if (deltaShoot > atkSpeed) {
				Vector3f bullletPos = new Vector3f();
				bullletPos.x = this.getPosition().x;
				bullletPos.y = this.getPosition().y + 3;
				bullletPos.z = this.getPosition().z;
				bullets.add(new Projectile(bulletModel, bullletPos, 0.0f, this.getRotY(), this.getRotZ(), 1f));
				deltaShoot = 0;
				lastShoot = now;
			}
		} else {
			shooting = false;
		}

		if (Mouse.isButtonDown(1)) {

			turnesDone = 0;

			destination = new Vector3f().set(MainGameLoop.getMPicker().getCurrentTerrainPoint());

			Vector3f dir = Vector3f.sub(destination, this.getPosition(), null);
			Vector3f currDir = new Vector3f();
			currDir.x = (float) (Math.sin(Math.toRadians(super.getRotY())));
			currDir.y = 0;
			currDir.z = (float) (Math.cos(Math.toRadians(super.getRotY())));

			double angleofTurn = Math.acos(
					(currDir.x * dir.x + currDir.y * dir.y + currDir.z * dir.z) / (dir.length() * currDir.length()));

			currDir.x = (float) (Math.sin(Math.toRadians(super.getRotY() + 0.1)));
			currDir.y = 0;
			currDir.z = (float) (Math.cos(Math.toRadians(super.getRotY() + 0.1)));

			double angleofTurn2 = Math.acos(
					(currDir.x * dir.x + currDir.y * dir.y + currDir.z * dir.z) / (dir.length() * currDir.length()));

			if (angleofTurn < angleofTurn2) {
				oneRot = (float) (-1 * (Math.toDegrees(angleofTurn) / TURNSTEPS));
			} else {
				oneRot = (float) ((Math.toDegrees(angleofTurn) / TURNSTEPS));
			}
			moving = true;
		}
	}

	public void update() {
		// ********** COLLIDING ***************
		
		
		
		// ********** COLLIDING ***************
		
		
		// ********** SHOOTING ***************
		if (shooting) {

			speed = MAX_SPEED / 2;
		} else {
			speed = MAX_SPEED;
		}
		for (int i = 0; i < bullets.size(); i++) {
			Projectile bullet = bullets.get(i);
			float dx = (float) (Projectile.SPEED * Math.sin(Math.toRadians(bullet.getRotY())));
			float dz = (float) (Projectile.SPEED * Math.cos(Math.toRadians(bullet.getRotY())));
			bullet.increasePosition(dx, 0, dz);
			if(bullet.colliding() != null){
				if(bullet.colliding().getClass().getName().equals("entities.Enemy")){
					bullet.colliding().setLife(bullet.colliding().getLife() - 1);
					bullets.remove(this);
				}
			}
			if (Vector3f.sub(bullet.getPosition(), bullet.getStart(), null).length() > Projectile.RANGE) {
				bullets.remove(bullet);
				bullet.destroy();
			}
		}
		// ********** SHOOTING ***************
		
		// ********** MOVE ***************

		if (moving) {

			Vector3f dir = Vector3f.sub(destination, this.getPosition(), null);
			if (dir.length() > 0.1) {

				if (turnesDone < TURNSTEPS) {
					this.increaseRotation(0, oneRot, 0);
					turnesDone++;
				}

				this.increasePosition(dir.x / dir.length() * speed, 0, dir.z / dir.length() * speed);

			} else {
				moving = false;
				turnesDone = 0;
			}
		}
		
		// ********** MOVE ***************
	}

	public void render(MasterRenderer renderer) {
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}
}
