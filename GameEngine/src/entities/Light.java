package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position; 
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(0.7f,0,0);
	//attenFac = x + y*d + z * d^2 -> lightBright/attenFac | d - distance
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation){
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAtten(){
		return attenuation;
	}

	public Vector3f getPos() {
		return position;
	}

	public void setPos(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}


}
