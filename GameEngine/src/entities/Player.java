package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import abilitys.Wall;
import animatedModel.AnimatedModel;
import engineTester.MainGameLoop;
import models.TexturedModel;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import toolbox.Maths;

public class Player extends Entity {

	private static final int TURNSTEPS = 3;

	private static final int HITBOX = 1;

	// private static final float TERRAIN_HEIGHT = 0;
	private static float MAX_SPEED = 0.2f;
	private float speed = MAX_SPEED;

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
	ParticleSystem pSysSprint;

	private AnimatedModel animModel;

	private Wall wall;
	boolean wallBool = false;

	private int sprintcd = 0;
	Vector2f wall1 = new Vector2f(0, 0);
	Vector2f wall2 = new Vector2f(0, 0);

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, ParticleSystem pSys, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, TexturedModel bulletModel, int life, AnimatedModel animModel) {
		super(model, position, rotX, rotY, rotZ, scale, HITBOX, life);
		this.bulletModel = bulletModel;
		lastShoot = System.currentTimeMillis();

		atkSpeed = 500;
		destination.set(0, 0);
		moving = false;
		turnesDone = 0;

		this.pSys = pSys;

		this.animModel = animModel;
		wall = null;

		Loader loader = new Loader();
		ParticleTexture pTexFire = new ParticleTexture(loader.loadTexture("fire"), 8);
		pSysSprint = new ParticleSystem(pTexFire, 350, 43, -0.3f, 0.3f, 3);
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

				Vector3f newPos = new Vector3f(this.getPosition().x + dir.x / dir.length() * speed,
						this.getPosition().y, this.getPosition().z + dir.y / dir.length() * speed);
				float terrainHeight = terrain.getHeightOfTerrain(newPos.x, newPos.z);
				boolean b = true;
				for (Entity e : Entity.entities) {
					if (!e.getClass().toString().equals("class entities.Player")) {
						if (Vector3f.sub(newPos, e.getPosition(), null).length() < 3)
							b = false;
					}
				}
				// collision height here
				if (terrainHeight > 0.05 && b) {
					this.increasePosition((dir.x / dir.length()) * speed, 0, (dir.y / dir.length()) * speed);
					// System.out.println((dir.x / dir.length()) * speed);
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
			if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
				if (!wallBool) {
					wallBool = true;
					wall1.setX(MainGameLoop.getMPicker().getCurrentTerrainPoint().x);
					wall1.setY(MainGameLoop.getMPicker().getCurrentTerrainPoint().z);
				}
			} else {
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
			}
		} else {
			shooting = false;
			if (wallBool) {
				wall2.setX(MainGameLoop.getMPicker().getCurrentTerrainPoint().x);
				wall2.setY(MainGameLoop.getMPicker().getCurrentTerrainPoint().z);
				wall = new Wall(wall1, wall2);
				wallBool = false;

			}
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
		// System.out.println(sprintcd);
		if (Keyboard.isKeyDown(Keyboard.KEY_C) && sprintcd == 0) {
			MAX_SPEED = MAX_SPEED * 2;
			sprintcd = 600;
		}

	}

	public void update() {
		if (sprintcd > 0)
			sprintcd--;

		checkInputs();

		// ********** SHOOTING ***************
		if (shooting) {
			speed = MAX_SPEED / 2;
		} else {
			speed = MAX_SPEED;
		}
		for (int i = 0; i < bullets.size(); i++) {
			Projectile bullet = bullets.get(i);
			if (bullet.delta > 0)
				bullet.delta--;
			float dx = (float) (Projectile.SPEED * Math.sin(Math.toRadians(bullet.getRotY())));
			float dz = (float) (Projectile.SPEED * Math.cos(Math.toRadians(bullet.getRotY())));
			pSys.setDirection(new Vector3f((float) Math.sin(Math.toRadians(bullet.getRotY())), 0,
					(float) Math.cos(Math.toRadians(bullet.getRotY()))), 0.025f);
			bullet.increasePosition(dx, 0, dz);
			pSys.generateParticles(bullet.getPosition());

			if (wall != null) {

				float diffWall = calcDiffWall(wall.getPos1(), wall.getPos2(), bullet.getPosition());
				if (diffWall < 2f && bullet.delta < 30) {
					double angle = Maths.angleBetweenVs(
							new Vector2f((float) Math.sin(Math.toRadians(bullet.getRotY())),
									(float) Math.cos(Math.toRadians(bullet.getRotY()))),
							Vector2f.sub(wall.getPos1(), wall.getPos2(), null));
					bullet.setRotY((float) (bullet.getRotY() + (360 - 2 * angle)));
					double angle2 = Maths.angleBetweenVs(
							new Vector2f((float) Math.sin(Math.toRadians(bullet.getRotY())),
									(float) Math.cos(Math.toRadians(bullet.getRotY()))),
							Vector2f.sub(wall.getPos1(), wall.getPos2(), null));
					if (!(Math.abs(angle - angle2) <= 1)) {
						bullet.setRotY((float) (bullet.getRotY() - (360 - 2 * angle)));
						bullet.setRotY((float) (bullet.getRotY() - (360 - 2 * angle)));
					}
					bullet.delta = 60;
				}
			}

			if (bullet.colliding() != null) {
				if (bullet.colliding().getClass().getName().equals("entities.Object")) {
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
		if (moving)
			animModel.update();
		if (wall != null)
			wall.update();

		if (sprintcd > 300)
			pSysSprint.generateParticles(this.getPosition());
		if (sprintcd == 300)
			MAX_SPEED = MAX_SPEED / 2;
	}

	private float calcDiffWall(Vector2f pos1, Vector2f pos2, Vector3f position) {
		Vector2f posB = new Vector2f(position.x, position.z);
		Vector2f dir = Vector2f.sub(pos1, pos2, null);
		Vector2f start = new Vector2f(pos1);
		dir = dir.normalise(dir);
		float abstand1 = Vector2f.sub(start, posB, null).length();
		float abstand2 = Vector2f.sub(Vector2f.sub(start, dir, null), posB, null).length();
		if (abstand1 > abstand2) {
			while (abstand1 > abstand2) {
				start = Vector2f.sub(start, dir, null);
				abstand1 = Vector2f.sub(start, posB, null).length();
				abstand2 = Vector2f.sub(Vector2f.sub(start, dir, null), posB, null).length();
			}
		} else {
			while (abstand1 < abstand2) {
				start = Vector2f.add(start, dir, null);
				abstand1 = Vector2f.sub(start, posB, null).length();
				abstand2 = Vector2f.sub(Vector2f.sub(start, dir, null), posB, null).length();
			}
		}

		if ((Vector2f.sub(start, pos1, null).length() < Vector2f.sub(pos1, pos2, null).length())
				&& (Vector2f.sub(start, pos2, null).length() < Vector2f.sub(pos1, pos2, null).length())) {
			return Vector2f.sub(start, posB, null).length();
		} else if (Vector2f.sub(start, pos1, null).length() < Vector2f.sub(start, pos2, null).length()) {
			return Vector2f.sub(pos1, posB, null).length();
		} else {
			return Vector2f.sub(pos2, posB, null).length();
		}

	}

	public void render(MasterRenderer renderer, Camera camera) {
		renderer.getAnimRenderer().render(animModel, camera, new Vector3f(0, -1, 0), renderer.getProjectionMatrix(),
				this);
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}

	private void sprint() {

	}
}
