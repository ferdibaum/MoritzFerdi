package abilitys;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.Loader;

public class Wall {

	private Vector2f pos1;
	private Vector2f pos2;
	private Vector2f vec;

	private int densityfactor = 3;
	private Loader loader = new Loader();
	private ParticleTexture pTexFire = new ParticleTexture(loader.loadTexture("cosmic"), 4);
	private ParticleSystem pSys = new ParticleSystem(pTexFire, 400, 5, .3f, 0.3f, 4);

	public Wall(Vector2f wall1, Vector2f wall2) {

		pos1 = new Vector2f(wall1.x, wall1.y);
		pos2 = new Vector2f(wall2.x, wall2.y);
		vec = Vector2f.sub(pos1, pos2, null);

	}

	public Vector2f getPos1() {
		return pos1;
	}

	public void setPos1(Vector2f pos1) {
		this.pos1 = pos1;
	}

	public Vector2f getPos2() {
		return pos2;
	}

	public void setPos2(Vector2f pos2) {
		this.pos2 = pos2;
	}

	public void update() {
		float density = vec.length() / densityfactor;
		// pSys.generateParticles(new Vector3f( pos1.x, 0,pos1.y));
		// pSys.generateParticles(new Vector3f( pos2.x, 0,pos2.y));
		for (int i = 0; i < density; i++) {
			pSys.generateParticles(Vector3f.sub(new Vector3f(pos1.x, 5, pos1.y),
					new Vector3f(i * (vec.x / density), 0, i * (vec.y / density)), null));
		}
	}
}
