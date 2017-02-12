package particles;

public class ParticleTexture {

	private int textureID;
	private int numberOfRows;
	
	public ParticleTexture(int textureID, int numberOfRows) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}

	public int getTextID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	
	
}
