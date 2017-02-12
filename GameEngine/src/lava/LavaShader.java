package lava;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class LavaShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/lava/lavaVertex.txt";
	private final static String FRAGMENT_FILE = "src/lava/lavaFragment.txt";

	private int loc_modelMat;
	private int loc_viewMat;
	private int loc_projectionMat;
	private int loc_reflectionText;
	private int loc_refractionText;
	private int loc_dudvMap;
	private int loc_moveFact;

	
	public LavaShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocation() {
		loc_projectionMat = getUniformLocation("projectionMat");
		loc_viewMat = getUniformLocation("viewMat");
		loc_modelMat = getUniformLocation("modelMat");
		loc_reflectionText = getUniformLocation("reflectText");
		loc_refractionText = getUniformLocation("refractText");
		loc_dudvMap = getUniformLocation("dudvMap");
		loc_moveFact = getUniformLocation("moveFact");
	}

	public void connectTextureUnits(){
		super.loadInt(loc_reflectionText, 0);
		super.loadInt(loc_refractionText, 1);
		super.loadInt(loc_dudvMap, 2);
	}
	
	public void loadMoveFact(float factor){
		super.loadFloat(loc_moveFact, factor);
	}
	
	public void loadProjectionMat(Matrix4f projection) {
		loadMatrix(loc_projectionMat, projection);
	}
	
	public void loadViewMat(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(loc_viewMat, viewMatrix);
	}

	public void loadModelMat(Matrix4f modelMatrix){
		loadMatrix(loc_modelMat, modelMatrix);
	}

}
