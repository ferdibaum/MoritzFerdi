package animation;

import org.lwjgl.util.vector.Matrix4f;


public class Quaternion {

	private float x, y, z, w;


	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		normalize();
	}

	public Quaternion(Matrix4f matrix) {
		float diagonal = matrix.m00 + matrix.m11 + matrix.m22;
		if (diagonal > 0) {
			float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
			this.w = w4 / 4f;
			this.x = (matrix.m21 - matrix.m12) / w4;
			this.y = (matrix.m02 - matrix.m20) / w4;
			this.z = (matrix.m10 - matrix.m01) / w4;
		} else if ((matrix.m00 > matrix.m11) && (matrix.m00 > matrix.m22)) {
			float x4 = (float) (Math.sqrt(1f + matrix.m00 - matrix.m11 - matrix.m22) * 2f);
			this.w = (matrix.m21 - matrix.m12) / x4;
			this.x = x4 / 4f;
			this.y = (matrix.m01 + matrix.m10) / x4;
			this.z = (matrix.m02 + matrix.m20) / x4;
		} else if (matrix.m11 > matrix.m22) {
			float y4 = (float) (Math.sqrt(1f + matrix.m11 - matrix.m00 - matrix.m22) * 2f);
			this.w = (matrix.m02 - matrix.m20) / y4;
			this.x = (matrix.m01 + matrix.m10) / y4;
			this.y = y4 / 4f;
			this.z = (matrix.m12 + matrix.m21) / y4;
		} else {
			float z4 = (float) (Math.sqrt(1f + matrix.m22 - matrix.m00 - matrix.m11) * 2f);
			this.w = (matrix.m10 - matrix.m01) / z4;
			this.x = (matrix.m02 + matrix.m20) / z4;
			this.y = (matrix.m12 + matrix.m21) / z4;
			this.z = z4 / 4f;
		}
		this.normalize();
	}


	public Matrix4f toRotationMatrix() {
		Matrix4f matrix = new Matrix4f();
		final float xy = x * y;
		final float xz = x * z;
		final float xw = x * w;
		final float yz = y * z;
		final float yw = y * w;
		final float zw = z * w;
		final float xSquared = x * x;
		final float ySquared = y * y;
		final float zSquared = z * z;
		matrix.m00 = 1 - 2 * (ySquared + zSquared);
		matrix.m01 = 2 * (xy - zw);
		matrix.m02 = 2 * (xz + yw);
		matrix.m03 = 0;
		matrix.m10 = 2 * (xy + zw);
		matrix.m11 = 1 - 2 * (xSquared + zSquared);
		matrix.m12 = 2 * (yz - xw);
		matrix.m13 = 0;
		matrix.m20 = 2 * (xz - yw);
		matrix.m21 = 2 * (yz + xw);
		matrix.m22 = 1 - 2 * (xSquared + ySquared);
		matrix.m23 = 0;
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		matrix.m33 = 1;
		return matrix;
	}


	public void normalize() {
		float mag = (float) Math.sqrt(w * w + x * x + y * y + z * z);
		w /= mag;
		x /= mag;
		y /= mag;
		z /= mag;
	}

	public static Quaternion slerp(Quaternion start, Quaternion end, float progression) {
		start.normalize();
		end.normalize();
		final float d = start.x * end.x + start.y * end.y + start.z * end.z + start.w * end.w;
		float absDot = d < 0f ? -d : d;
		float scale0 = 1f - progression;
		float scale1 = progression;

		if ((1 - absDot) > 0.1f) {

			final float angle = (float) Math.acos(absDot);
			final float invSinTheta = 1f / (float) Math.sin(angle);
			scale0 = ((float) Math.sin((1f - progression) * angle) * invSinTheta);
			scale1 = ((float) Math.sin((progression * angle)) * invSinTheta);
		}

		if (d < 0f) {
			scale1 = -scale1;
		}
		float newX = (scale0 * start.x) + (scale1 * end.x);
		float newY = (scale0 * start.y) + (scale1 * end.y);
		float newZ = (scale0 * start.z) + (scale1 * end.z);
		float newW = (scale0 * start.w) + (scale1 * end.w);
		return new Quaternion(newX, newY, newZ, newW);
	}

	@Override
	public String toString() {
		return x + ", " + y + ", " + z + ", " + w;
	}

}
