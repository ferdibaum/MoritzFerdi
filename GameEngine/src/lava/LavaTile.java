package lava;

public class LavaTile {
	
	public static final float SIZE = 65;
	
	private float height;
	private float x,z;
	
	public LavaTile(float centerX, float centerZ, float height){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
