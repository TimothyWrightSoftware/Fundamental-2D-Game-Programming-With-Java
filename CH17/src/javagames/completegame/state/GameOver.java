package javagames.completegame.state;

import java.awt.Graphics2D;
import java.util.List;
import javagames.completegame.object.*;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;

public class GameOver extends AttractState {
	
	GameState state;

	public GameOver(List<Asteroid> asteroids, GameState state) {
		super(asteroids);
		this.state = state;
	}

	@Override
	protected float getWaitTime() {
		return 3.0f;
	}

	@Override
	protected AttractState getState() {
		if (highScoreMgr.newHighScore(state)) {
			return new EnterHighScoreName(state);
		} else {
			return new HighScore();
		}
	}

	@Override
	public void render(Graphics2D g, Matrix3x3f view) {
		super.render(g, view);
		acme.drawScore(g, state.getScore());
		Utility.drawCenteredString(g, app.getScreenWidth(),
				app.getScreenHeight() / 3, "G A M E O V E R");
	}
}