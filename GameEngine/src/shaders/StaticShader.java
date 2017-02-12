package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 12;

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	private int loc_transformationMat;
	private int loc_projectionMat;
	private int loc_viewMat;
	private int loc_lightPos[];
	private int loc_lightCol[];
	private int loc_attenuation[];
	private int loc_shineDamper;
	private int loc_reflectivity;
	private int loc_useFakeLight;
	private int loc_plane;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocation() {
		loc_viewMat = super.getUniformLocation("viewMatrix");
		loc_projectionMat = super.getUniformLocation("projectionMatrix");
		loc_transformationMat = super.getUniformLocation("transfomationMatrix");
		loc_shineDamper = super.getUniformLocation("shineDamper");
		loc_reflectivity = super.getUniformLocation("reflectivity");
		loc_useFakeLight = super.getUniformLocation("useFakeLighting");
		loc_plane = super.getUniformLocation("plane");
		
		loc_lightPos = new int[MAX_LIGHTS];
		loc_lightCol = new int[MAX_LIGHTS];
		loc_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			loc_lightPos[i] = super.getUniformLocation("lightPosition[" + i + "]");
			loc_lightCol[i] = super.getUniformLocation("lightColour[" + i + "]");
			loc_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	public void loadClipPlane(Vector4f plane){
		super.load4DVector(loc_plane, plane);
	}
	
	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(loc_useFakeLight, useFake);
	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(loc_shineDamper, damper);
		super.loadFloat(loc_reflectivity, reflectivity);
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.load3DVector(loc_lightPos[i], lights.get(i).getPos());
				super.load3DVector(loc_lightCol[i], lights.get(i).getColour());
				super.load3DVector(loc_attenuation[i], lights.get(i).getAtten());
			} else {
				super.load3DVector(loc_lightPos[i], new Vector3f(0,0,0));
				super.load3DVector(loc_lightCol[i], new Vector3f(0,0,0));
				super.load3DVector(loc_attenuation[i], new Vector3f(1,0,0));
			}
		}
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMat, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(loc_viewMat, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(loc_projectionMat, projection);
	}

}
