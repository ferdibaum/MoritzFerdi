package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import engineTester.MainGameLoop;
import renderEngine.MasterRenderer;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 0.2f;
	private static final float TURN_SPEED = 3.5f;

	// private static final float TERRAIN_HEIGHT = 0;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private TexturedModel bulletModel;
	private long lastShoot;
	private long deltaShoot;

	private int atkSpeed;
	
	
	private boolean moving;
	private Vector3f destination = new Vector3f();
	private float destAngle;
	private int turnDir;

	private List<Projectile> bullets = new ArrayList<Projectile>();

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			TexturedModel bulletModel) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.bulletModel = bulletModel;
		lastShoot = System.currentTimeMillis();

		atkSpeed = 500;
		destination.set(this.getPosition());
		moving = false;
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

//		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
//			this.currentSpeed = RUN_SPEED;
//		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
//			this.currentSpeed = -RUN_SPEED;
//		} else {
//			this.currentSpeed = 0;
//		}
//
//		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
//			this.currentTurnSpeed = -TURN_SPEED;
//		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
//			this.currentTurnSpeed = TURN_SPEED;
//		} else {
//			this.currentTurnSpeed = 0;
//		}

		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			long now = System.currentTimeMillis();
			deltaShoot = now - lastShoot;
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

		if (Mouse.isButtonDown(0)) {
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
			
			System.out.println(angleofTurn);
			if(angleofTurn < angleofTurn2)
			{
				turnDir = 1;
				System.out.println("right");
			}else{
				turnDir = -1;
			}
			moving = true;
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

		if (moving) {
			
			Vector3f dir = Vector3f.sub(destination, this.getPosition(), null);
			Vector3f currDir = new Vector3f();
			currDir.x = (float) (Math.sin(Math.toRadians(super.getRotY())));
			currDir.y = 0;
			currDir.z = (float) (Math.cos(Math.toRadians(super.getRotY())));
			
			double angleofTurn = Math.acos(
					(currDir.x * dir.x + currDir.y * dir.y + currDir.z * dir.z) / (dir.length() * currDir.length()));
			
			if(turnDir == 1 ){
				this.currentTurnSpeed = -TURN_SPEED;
				if(angleofTurn < 0.05){
					this.currentTurnSpeed = 0;
					turnDir = 0;
				}
			}
			if(turnDir == -1){
				this.currentTurnSpeed = TURN_SPEED;
				if(angleofTurn < 0.05){
					this.currentTurnSpeed = 0;
					turnDir = 0;
				}
			}
			if(turnDir == 0){
				this.increasePosition(dir.x / dir.length() * RUN_SPEED, dir.y/ dir.length()* RUN_SPEED, dir.z/ dir.length()* RUN_SPEED);
				if(dir.length() <= 0.3){
					moving = false;
				}
			}
		}
	}

	public void render(MasterRenderer renderer) {
		for (Entity entity : bullets) {
			renderer.processEntity(entity);
		}
	}
}
