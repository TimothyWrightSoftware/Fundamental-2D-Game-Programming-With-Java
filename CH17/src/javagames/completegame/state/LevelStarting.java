package javagames.completegame.state;

import java.awt.*;
import javagames.completegame.admin.Acme;
import javagames.util.*;

public class LevelStarting extends State {
	
	double time;
	private Sprite background;
	private GameState state;
	private Acme acme;

	public LevelStarting(GameState state) {
		this.state = state;
	}

	@Override
	public void enter() {
		background = (Sprite) controller.getAttribute("background");
		acme = (Acme) controller.getAttribute("ACME");
		time = 0.0;
	}

	@Override
	public void updateObjects(float delta) {
		time += delta;
		if (time > 2.0) {
			getController().setState(new LevelPlaying(state));
		}
	}

	@Override
	public void render(Graphics2D g, Matrix3x3f view) {
		super.render(g, view);
		background.render(g, view);
		acme.drawScore(g, state.getScore());
		acme.drawLives(g, view, state.getLives());
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.setColor(Color.GREEN);
		Utility.drawCenteredString(g, app.getScreenWidth(),
				app.getScreenHeight() / 3, "L E V E L " + state.getLevel());
	}
}