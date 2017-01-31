package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import animatedModel.AnimatedModel;
import animation.Animation;
import entities.Camera;
import entities.Light;
import entities.Player;
import entities.Rock;
import guis.GuiRenderer;
import guis.GuiTexture;
import lava.LavaFrameBuffers;
import lava.LavaRenderer;
import lava.LavaShader;
import lava.LavaTile;
import loaders.AnimatedModelLoader;
import loaders.AnimationLoader;
import models.RawModel;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderer.AnimatedModelRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import utils.MyFile;

public class MainGameLoop {

	private static Player player;
	private static Terrain terrain;
	private static Camera camera;
	private static GuiRenderer guiRenderer;
	private static MasterRenderer renderer;
	private static MousePicker mPicker;
	private static String title = "FPS: 0 UPDATES: 0";
	private static List<GuiTexture> guis;
	private static ParticleSystem pSysFireball;
	private static ParticleSystem pSysRock;
	private static List<Light> lights = new ArrayList<Light>();
	private static LavaRenderer lavaRenderer; 
	private static List<LavaTile> lavas;
	private static LavaFrameBuffers buffers;
	private static LavaTile lava;
	//private static RenderEngine engine;
	//private static Scene scene;
	
	
	public static void main(String[] args) {
		DisplayManager.createDisplay(); // Fenster erzeugen
		Loader loader = new Loader();

		// Terrain mit Texturen erstellen
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("dirt_texture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("fire_texture"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("lava_texture"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("stone_texture"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("lavaMap"));
		terrain = new Terrain(-0.5f, -0.5f, loader, texturePack, blendMap, "heightMapTest");

		SkyboxRenderer skyRenderer = new SkyboxRenderer();
		AnimatedModelRenderer entityRenderer = new AnimatedModelRenderer();
		renderer = new MasterRenderer(entityRenderer, skyRenderer);
		
		AnimatedModel animmodel = AnimatedModelLoader.loadEntity(new MyFile("res", GeneralSettings.MODEL_FILE),
				new MyFile("res", GeneralSettings.DIFFUSE_FILE));
		Animation animation = AnimationLoader.loadAnimation(new MyFile("res", GeneralSettings.ANIM_FILE));
		animmodel.doAnimation(animation);
		
		//scene = SceneLoader.loadScene(GeneralSettings.RES_FOLDER);
		
		//Lava 
		buffers = new LavaFrameBuffers();
		LavaShader lavaShader = new LavaShader();
		lavaRenderer = new LavaRenderer(loader, lavaShader, renderer.getProjectionMatrix(), buffers);
		lavas = new ArrayList<LavaTile>();
		lava = new LavaTile(0, 0, 0);
		lavas.add(lava);
		
		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

		ParticleTexture pTexFire = new ParticleTexture(loader.loadTexture("fire"), 8);
		pSysFireball = new ParticleSystem(pTexFire, 350, 43, -0.3f, 0.3f, 3);
		pSysFireball.setLifeError(0.1f);
		pSysFireball.setSpeedError(0.25f);
		pSysFireball.setScaleError(0.5f);
		pSysFireball.randomizeRotation();

		ParticleTexture pTexRock = new ParticleTexture(loader.loadTexture("rock_particle"), 4);
		pSysRock = new ParticleSystem(pTexRock, 3000, 30, 1, 0.2f, 1);
		pSysRock.setDirection(new Vector3f(0,1,0), 0.2f);
		pSysRock.setScaleError(0.5f);
		pSysRock.setLifeError(0.2f);
		pSysRock.setSpeedError(0.2f);

		RawModel modelNova = OBJLoader.loadObjModel("Nova", loader);
		TexturedModel textModelNova = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("pink")));

		RawModel modelRock = OBJLoader.loadObjModel("Rock", loader);
		TexturedModel textModelRock = new TexturedModel(modelRock, new ModelTexture(loader.loadTexture("rock")));

		RawModel modelLavaball = OBJLoader.loadObjModel("lavaball", loader);
		TexturedModel textModelLavaball = new TexturedModel(modelLavaball,
				new ModelTexture(loader.loadTexture("lava")));

		
		Light light = new Light(new Vector3f(0, 1000, 0), new Vector3f(0.3f, 0.3f, 0.3f));
		Light pLight = new Light(new Vector3f(0, 7, 0), new Vector3f(2, 0, 0), new Vector3f(1, 0.0001f, 0.00001f));
		
		lights.add(light);
		lights.add(pLight);

		camera = new Camera();

		player = new Player(textModelNova, pSysFireball, new Vector3f(0, terrain.getHeightOfTerrain(0, 0), 0), 0, 0,
				0, 1, textModelLavaball, 100, animmodel);

		mPicker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		// GUI
		guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("lava"), new Vector2f(0, -1), new Vector2f(0.5f, 0.3f));
		guis.add(gui);
		guiRenderer = new GuiRenderer(loader);
		
		/**TEST
		GuiTexture lavaRefra = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.5f,0.5f), new Vector2f(0.25f,0.25f));
		GuiTexture lavaRefle = new GuiTexture(buffers.getRefractionTexture(), new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
		guis.add(lavaRefra);
		guis.add(lavaRefle);
		*/
		
		// Paar Sachen spawnen
		Random random = new Random();
		for (int i = 0; i < terrain.getEnemys().length; i++) {
			for (int j = 0; j < terrain.getEnemys().length; j++) {
				if (terrain.getEnemys()[i][j] != null) {
					float x = -200f + (float) i * (400f / 255f);
					float z = -200f + (float) j * (400f / 255f);
					float y = terrain.getHeightOfTerrain(x, z);
					new Rock(textModelRock, pSysRock, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f, 1f, 2, 1);
				}
			}
		}

		/*----------------------------
		 *   MAIN GAME LOOP
		 */
		long lastTime = System.nanoTime();
		final double AMOUNT_OF_TICKS = 60.0;
		final double NS = 1000000000 / AMOUNT_OF_TICKS;
		double delta = 0;

		// Updates und Frames fuer den Fenstertitel nachhalten
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		// Solange das Fenster noch nicht geschlossen wurde
		while (!Display.isCloseRequested()) {
			// Physikalische Berechnungen machen, rendern aufschieben
			final long NOW = System.nanoTime();
			delta += (NOW - lastTime) / NS;
			lastTime = NOW;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			// Alles rendern
			render(title);

			// Updates und Frames im Fenstertitel anzeigen
			frames++;
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				title = "FPS: " + frames + " UPDATES: " + updates;
				frames = 0;
				updates = 0;
			}
			//System.out.println(player.getPosition().x + "\t" + player.getPosition().y + "\t" + player.getPosition().z);
		}
		/*
		 * END MAIN GAME LOOP ---------------------------
		 */

		// Beim Schliessen aufraeumen
		ParticleMaster.cleanUp();
		buffers.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		lavaShader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	// Physikalische Berechnungen etc machen
	private static void update() {
		camera.move(player);
		player.update();
		player.move(terrain);
		mPicker.update();
		// system.generateParticles(player.getPosition());
		ParticleMaster.update(camera);
		Vector3f mousePos = mPicker.getCurrentTerrainPoint();
		for (int i = 0; i < Rock.rocks.size(); i++) {
			Rock rock = Rock.rocks.get(i);
			rock.update();
		}
		if (mousePos != null) {
			// System.out.println(mousePos.x + "\t" + mousePos.y + "\t" +
			// mousePos.z);
		}
		//noch ordentlich machen nur für lava TODO
		buffers.bindReflectionFrameBuffer();
		float distance = 2* (camera.getPosition().y - lava.getHeight());
		camera.getPosition().y -= distance;
		camera.invPitch();
		renderer.render(lights, camera, new Vector4f(0, 1, 0, -lava.getHeight()));
		//renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer, camera);
		for (int i = 0; i < Rock.rocks.size(); i++) {
			Rock rock = Rock.rocks.get(i);
			renderer.processEntity(rock);
		}
		ParticleMaster.renderParticles(camera);
		camera.getPosition().y += distance;
		camera.invPitch();
		//fbos.unbindCurrentFrameBuffer();
		
		buffers.bindRefractionFrameBuffer();
		renderer.render(lights, camera, new Vector4f(0, -1, 0, lava.getHeight()));
		//renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer, camera);
		for (int i = 0; i < Rock.rocks.size(); i++) {
			Rock rock = Rock.rocks.get(i);
			renderer.processEntity(rock);
		}
		ParticleMaster.renderParticles(camera);
		buffers.unbindCurrentFrameBuffer();
	}

	// Alles rendern und anzeigen
	private static void render(String s) {
		renderer.render(lights, camera, new Vector4f(0, -1, 0, 10000));
		//renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer, camera);
		for (int i = 0; i < Rock.rocks.size(); i++) {
			Rock rock = Rock.rocks.get(i);
			renderer.processEntity(rock);
		}
		ParticleMaster.renderParticles(camera);
		lavaRenderer.render(lavas, camera);
		guiRenderer.render(guis);
		
		DisplayManager.updateDisplay(s);
	}

	public static MousePicker getMPicker() {
		return mPicker;
	}
}