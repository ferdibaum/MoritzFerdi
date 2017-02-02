package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Camera {

	private static final float PITCH_SENSITIVITY = 0.3f;
	private static final float YAW_SENSITIVITY = 0.3f;
	private static final float MAX_PITCH = 90;
	private static final float Y_OFFSET = 5;

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.2f;
	private static final float FAR_PLANE = 400;

	private static final float SPEED = 0.5f;
	private static final float ZOOM_SPEED = 2;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix = new Matrix4f();

	private Vector3f position = new Vector3f(-38, 55, -22);
	private float yaw = 0;
	private float pitch = 54;
	private float roll;

	public Camera() {
		// this.projectionMatrix = createProjectionMatrix();
	}

	public void move(Player player) {

		Vector2f diff1 = new Vector2f();
		diff1.x = (float) Math.sin(Math.toRadians(yaw));
		diff1.y = (float) Math.cos(Math.toRadians(yaw));

		Vector2f diff2 = new Vector2f();
		diff2.x = (float) Math.sin(Math.toRadians(pitch));
		diff2.y = (float) Math.cos(Math.toRadians(pitch));

		/* ONLY USE FOR CAMERA ADJUSTMENT
		Vector3f diff = new Vector3f();
		diff.x = (float) Math.sin(Math.toRadians(yaw));
		diff.z = (float) Math.cos(Math.toRadians(yaw));
		diff.y = (float) position.y;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x = position.x + diff.x * SPEED;
			position.z = position.z + diff.z * -SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x = position.x + diff.z * SPEED;
			position.z = position.z + diff.x * SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x = position.x + diff.z * -SPEED;
			position.z = position.z + diff.x * -SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x = position.x + diff.x * -SPEED;
			position.z = position.z + diff.z * SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			yaw += 0.6f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			yaw -= 0.6f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			position.y = position.y + SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
			if (position.y > 1) {
				position.y = position.y - SPEED;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			pitch += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			pitch -= 0.2f;
		}
		System.out.println(position.x + "\t" + position.y + "\t" + position.z + "\t" + yaw + "\t" + pitch);
		 ************************************************/ 
		
		int dWheel = Mouse.getDWheel() / 120;
		float viewDist = (float) Math.tan(Math.toRadians(pitch)) * position.y;
		float startViewDist = 75.7f;
		//System.out.println(viewDist);
		
		if (dWheel < 0 && position.y <= 120) {
			position.x = position.x + diff1.x * diff2.y * ZOOM_SPEED * dWheel;
			position.z = position.z + diff1.y * diff2.y * -ZOOM_SPEED * dWheel;
			position.y = position.y - diff2.x * ZOOM_SPEED * dWheel;
		}
		if (dWheel > 0 && position.y >= 10) {
			position.x = position.x + diff1.x * diff2.y * ZOOM_SPEED * dWheel;
			position.z = position.z + diff1.y * diff2.y * -ZOOM_SPEED * dWheel;
			position.y = position.y - diff2.x * ZOOM_SPEED * dWheel;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Vector3f playerPos = player.getPosition();
			position.x = playerPos.x - 15;
			position.z = playerPos.z + 41;
			position.y = 55;
		}
		if (Mouse.getX() < 30 && position.x >= -150) {
			position.x = position.x + diff1.y * -SPEED;
			position.z = position.z + diff1.x * -SPEED;
		}
		if (Mouse.getX() > (DisplayManager.WIDTH - 30) && position.x <= 150) {
			position.x = position.x + diff1.y * SPEED;
			position.z = position.z + diff1.x * SPEED;
		}
		if (Mouse.getY() < 30 && position.z <= 225) {
			position.x = position.x + diff1.x * -SPEED;
			position.z = position.z + diff1.y * SPEED;
		}
		if (Mouse.getY() > (DisplayManager.HEIGHT - 30) && position.z >= (-150 + viewDist - startViewDist)) {
			position.x = position.x + diff1.x * SPEED;
			position.z = position.z + diff1.y * -SPEED;
		}

		// updateViewMatrix();
	}

	public void invPitch() {
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getProjectionViewMatrix() {
		return Matrix4f.mul(projectionMatrix, viewMatrix, null);
	}

	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	}

	private static Matrix4f createProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
}