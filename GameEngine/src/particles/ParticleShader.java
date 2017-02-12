package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "src/particles/particleVShader.txt";
	private static final String FRAGMENT_SHADER = "src/particles/particleFShader.txt";

	private int loc_modelViewMat;
	private int loc_projectionMat;
	private int loc_texOff1;
	private int loc_texOff2;
	private int loc_texCoordInfo;

	
	
	public ParticleShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void getAllUniformLocation() {
		loc_modelViewMat = super.getUniformLocation("modelViewMat");
		loc_projectionMat = super.getUniformLocation("projectionMat");
		loc_texOff1 = super.getUniformLocation("texOff1");
		loc_texOff2 = super.getUniformLocation("texOff2");
		loc_texCoordInfo = super.getUniformLocation("texCoordInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	protected void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend) {
		super.load2DVector(loc_texOff1, offset1);
		super.load2DVector(loc_texOff2, offset2);
		super.load2DVector(loc_texCoordInfo, new Vector2f(numRows, blend));

	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(loc_modelViewMat, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(loc_projectionMat, projectionMatrix);
	}

}
