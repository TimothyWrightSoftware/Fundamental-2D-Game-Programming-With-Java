package javagames.completegame.admin;

import java.awt.*;
import javagames.completegame.CompleteGame;
import javagames.completegame.object.Ship;
import javagames.util.*;

public class Acme {
	
	private CompleteGame app;
	private Ship ship;

	public Acme(CompleteGame app) {
		this.app = app;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public void drawScore(Graphics2D g, int score) {
		g.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);
		String toShow = "" + score;
		while (toShow.length() < 6) {
			toShow = "0" + toShow;
		}
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.GREEN);
		Utility.drawCenteredString(g, app.getScreenWidth(), 0, toShow);
	}

	public void drawLives(Graphics2D g, Matrix3x3f view, int lives) {
		float w = ship.getWidth();
		float h = ship.getHeight();
		float x = -0.95f + w;
		float y = 1.0f - h / 2.0f;
		for (int i = 0; i < lives; ++i) {
			x += w * i;
			ship.setAngle((float) Math.toRadians(90));
			ship.setPotition(new Vector2f(x, y));
			ship.update(0.0f);
			ship.draw(g, view);
		}
	}
}