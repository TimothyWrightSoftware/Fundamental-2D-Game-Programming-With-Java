package javagames.completegame.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.*;
import javagames.completegame.admin.*;
import javagames.completegame.object.*;
import javagames.util.*;

public abstract class AttractState extends State {
	
	private List<Asteroid> asteroids;
	private float time;
	private Sprite background;
	private AsteroidFactory factory;
	protected Acme acme;
	protected KeyboardInput keys;
	protected HighScoreMgr highScoreMgr;

	public AttractState() {
		
	}

	public AttractState(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	@Override
	public void enter() {
		highScoreMgr = (HighScoreMgr) controller.getAttribute("score");
		keys = (KeyboardInput) controller.getAttribute("keys");
		background = (Sprite) controller.getAttribute("background");
		factory = (AsteroidFactory) controller.getAttribute("factory");
		acme = (Acme) controller.getAttribute("ACME");
		if (asteroids == null) {
			asteroids = new Vector<Asteroid>();
			asteroids.add(factory.getLargeAsteroid());
			asteroids.add(factory.getMediumAsteroid());
			asteroids.add(factory.getSmallAsteroid());
		}
		time = 0.0f;
	}

	@Override
	public void updateObjects(float delta) {
		time += delta;
		if (shouldChangeState()) {
			AttractState state = getState();
			state.setAsteroids(asteroids);
			getController().setState(state);
			return;
		}
		for (Asteroid a : asteroids) {
			a.update(delta);
		}
	}

	protected boolean shouldChangeState() {
		return time > getWaitTime();
	}

	protected float getWaitTime() {
		return 5.0f;
	}

	private void setAsteroids(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	protected abstract AttractState getState();

	public List<Asteroid> getAsteroids() {
		return asteroids;
	}

	@Override
	public void processInput(float delta) {
		if (keys.keyDownOnce(KeyEvent.VK_ESCAPE)) {
			app.shutDownGame();
		}
		if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
			GameState state = new GameState();
			state.setLevel(1);
			state.setLives(2);
			getController().setState(new LevelStarting(state));
		}
	}

	@Override
	public void render(Graphics2D g, Matrix3x3f view) {
		background.render(g, view);
		for (Asteroid a : asteroids) {
			a.draw(g, view);
		}
	}
}