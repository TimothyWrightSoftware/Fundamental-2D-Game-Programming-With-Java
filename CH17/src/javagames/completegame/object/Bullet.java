package javagames.completegame.object;

import java.awt.Color;
import java.awt.Graphics2D;
import javagames.util.*;

public class Bullet {
	
	private Vector2f velocity;
	private Vector2f position;
	private Color color;
	private float radius;

	public Bullet(Vector2f position, float angle) {
		this.position = position;
		velocity = Vector2f.polar(angle, 1.0f);
		radius = 0.006f;
		color = Color.GREEN;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void draw(Graphics2D g, Matrix3x3f view) {
		g.setColor(color);
		Vector2f topLeft = new Vector2f(position.x - radius, position.y
				+ radius);
		topLeft = view.mul(topLeft);
		Vector2f bottomRight = new Vector2f(position.x + radius, position.y
				- radius);
		bottomRight = view.mul(bottomRight);
		int circleX = (int) topLeft.x;
		int circleY = (int) topLeft.y;
		int circleWidth = (int) (bottomRight.x - topLeft.x);
		int circleHeight = (int) (bottomRight.y - topLeft.y);
		g.fillOval(circleX, circleY, circleWidth, circleHeight);
	}

	public void update(float time) {
		position = position.add(velocity.mul(time));
	}
}
