package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import engineTester.MainGameLoop;
import particles.ParticleSystem;
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
	private Vector2f destination = new Vector2f();

	private float oneRot;
	private int turnesDone;

	ParticleSystem pSys;

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, ParticleSystem pSys, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, TexturedModel bulletModel, int life) {
		super(model, position, rotX, rotY, rotZ, scale, HITBOX, life);
		this.bulletModel = bulletModel;
		lastShoot = System.currentTimeMillis();

		atkSpeed = 500;
		destination.set(0, 0);
		moving = false;
		turnesDone = 0;

		this.pSys = pSys;
	}

	public void move(Terrain terrain) {
		
		if (moving) {

			Vector2f pos = new Vector2f();
			pos.set(this.getPosition().getX(), this.getPosition().getZ());
			Vector2f dir = Vector2f.sub(destination, pos, null);
			if (dir.length() > 0.1) {

				if (turnesDone < TURNSTEPS) {
					this.increaseRotation(0, oneRot, 0);
					turnesDone++;
				}
				
				Vector3f newPos = new Vector3f(this.getPosition().x + dir.x / dir.length() * speed, this.getPosition().y, this.getPosition().z + dir.y / dir.length() * speed);
				float terrainHeight = terrain.getHeightOfTerrain(newPos.x, newPos.z);
				System.out.println(terrainHeight);
				if(terrainHeight < -4){
					this.increasePosition(dir.x / dir.length() * speed, 0, dir.y / dir.length() * speed);
				}
				
				terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
				if (super.getPosition().y != terrainHeight) {
					super.getPosition().y = terrainHeight;
				}

			} else {
				moving = false;
				turnesDone = 0;
			}
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
				bullletPos.y = this.getPosition().y + 5;
				bullletPos.z = this.getPosition().z;
				bullets.add(new Projectile(bulletModel, bullletPos, 0.0f, this.getRotY(), this.getRotZ(), 1f));
				deltaShoot = 0;
				lastShoot = now;
			}
		} else {
			shooting = false;
		}

		if (Mouse.isButtonDown(1)) {

			if (MainGameLoop.getMPicker().getCurrentTerrainPoint() != null) {

				turnesDone = 0;

				destination.set(MainGameLoop.getMPicker().getCurrentTerrainPoint().x,
						MainGameLoop.getMPicker().getCurrentTerrainPoint().z);

				Vector2f pos = new Vector2f();
				pos.set(this.getPosition().getX(), this.getPosition().getZ());
				Vector2f dir = Vector2f.sub(destination, pos, null);
				Vector2f currDir = new Vector2f();
				currDir.x = (float) (Math.sin(Math.toRadians(this.getRotY())));
				currDir.y = (float) (Math.cos(Math.toRadians(this.getRotY())));

				double angleofTurn = Math
						.acos((currDir.x * dir.x + currDir.y * dir.y) / (dir.length() * currDir.length()));
				if (angleofTurn > 0) {

					currDir.x = (float) (Math.sin(Math.toRadians(super.getRotY() + 0.1)));
					currDir.y = (float) (Math.cos(Math.toRadians(super.getRotY() + 0.1)));

					double angleofTurn2 = Math
							.acos((currDir.x * dir.x + currDir.y * dir.y) / (dir.length() * currDir.length()));

					if (angleofTurn < angleofTurn2) {
						oneRot = (float) (-1 * (Math.toDegrees(angleofTurn) / TURNSTEPS));
					} else {
						oneRot = (float) ((Math.toDegrees(angleofTurn) / TURNSTEPS));
					}
					moving = true;
				}
			}
		}
	}

	public void update() {

		checkInputs();
		
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
			pSys.setDirection(new Vector3f((float) Math.sin(Math.toRadians(bullet.getRotY())), 0,
					(float) Math.cos(Math.toRadians(bullet.getRotY()))), 0.025f);
			bullet.increasePosition(dx, 0, dz);
			pSys.generateParticles(bullet.getPosition());
			if (bullet.colliding() != null) {
				if (bullet.colliding().getClass().getName().equals("entities.Enemy")) {
					bullet.colliding().setLife(bullet.colliding().getLife() - 1);
					bullets.remove(bullet);
					bullet.destroy();
				}
			}
			if (Vector3f.sub(bullet.getPosition(), bullet.getStart(), null).length() > Projectile.RANGE) {
				bullets.remove(bullet);
				bullet.destroy();
			}
		}
		// ********** SHOOTING ***************

		// ********** MOVE ***************

		

		// ********** MOVE ***************
	}

	public void render(MasterRenderer renderer) {
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}
}
