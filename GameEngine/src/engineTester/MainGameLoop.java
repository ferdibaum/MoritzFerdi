package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import Models.TexturedModel;
import entities.Camera;
import entities.Enemy;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {

	private static Player player;
	private static Terrain terrain;
	private static Camera camera;
	private static GuiRenderer guiRenderer;
	private static MasterRenderer renderer;
	private static Light light;
	private static MousePicker mPicker;
	private static String title = "FPS: 0 UPDATES: 0";
	private static List<GuiTexture> guis;
	private static ParticleSystem system;

	public static void main(String[] args) {
		DisplayManager.createDisplay(); // Fenster erzeugen
		Loader loader = new Loader();

		// Terrain mit Texturen erstellen
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(-0.5f, 0, loader, texturePack, blendMap, "heightMapTest");

		renderer = new MasterRenderer();

		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4);


		system = new ParticleSystem(particleTexture, 50, 0.5f, -0.1f, 0.3f, 3);


		RawModel modelNova = OBJLoader.loadObjModel("Nova", loader);
		TexturedModel textModelNova = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("pink")));

		TexturedModel textModelEnemy = new TexturedModel(modelNova, new ModelTexture(loader.loadTexture("blue")));

		RawModel modelTree = OBJLoader.loadObjModel("tree", loader);
		TexturedModel textModelTree = new TexturedModel(modelTree, new ModelTexture(loader.loadTexture("tree")));

		light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

		camera = new Camera();

		player = new Player(textModelNova,system, new Vector3f(0, terrain.getHeightOfTerrain(0, -50), -50), 0, 0, 0, 1,
				textModelTree, 100);

		mPicker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		// GUI
		guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("path"), new Vector2f(0.5f, 0.5f),
				new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		guiRenderer = new GuiRenderer(loader);

		// Paar Enemys spawnen
		
		Random random = new Random();
		for (int i = 0; i < 200; i++) {
			float x = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			float y = terrain.getHeightOfTerrain(x, z);
			new Enemy(textModelEnemy, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f, 1f, 0, 1);
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
		}
		/*
		 * END MAIN GAME LOOP ---------------------------
		 */

		// Beim Schliessen aufraeumen
		ParticleMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	// Physikalische Berechnungen und den ganzen Kram machen
	private static void update() {
		camera.move(player);
		player.move(terrain);
		mPicker.update();
		player.update();
		//system.generateParticles(player.getPosition());
		ParticleMaster.update(camera);
		Vector3f mousePos = mPicker.getCurrentTerrainPoint();
		for (int i = 0; i < Enemy.enemies.size(); i++) {
			Enemy enemy = Enemy.enemies.get(i);
			enemy.update();
		}
		if (mousePos != null) {
//			 System.out.println(mousePos.x + "\t" + mousePos.y + "\t" +
//			 mousePos.z);
		}
	}

	// Alles rendern und anzeigen
	private static void render(String s) {
		renderer.render(light, camera);
		renderer.processEntity(player);
		renderer.processTerrain(terrain);
		player.render(renderer);
		for (int i = 0; i < Enemy.enemies.size(); i++) {
			Enemy enemy = Enemy.enemies.get(i);
			renderer.processEntity(enemy);
		}
		ParticleMaster.renderParticles(camera);
		guiRenderer.render(guis);
		DisplayManager.updateDisplay(s);
	}

	public static MousePicker getMPicker() {
		return mPicker;
	}
}
