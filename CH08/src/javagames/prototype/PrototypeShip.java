package javagames.prototype;

import java.awt.*;
import java.util.ArrayList;
import javagames.util.*;

public class PrototypeShip {
	
	private float angle;
	private float acceleration;
	private float friction;
	private float maxVelocity;
	private float rotationDelta;
	private float curAcc;
	private Vector2f position;
	private Vector2f velocity;
	private PolygonWrapper wrapper;
	private boolean damaged;
	private Vector2f[] polyman;
	private ArrayList<Vector2f[]> renderList;

	public PrototypeShip(PolygonWrapper wrapper) {
		this.wrapper = wrapper;
		friction = 0.25f;
		rotationDelta = (float) Math.toRadians(180.0);
		acceleration = 1.0f;
		maxVelocity = 0.5f;
		velocity = new Vector2f();
		position = new Vector2f();
		polyman = new Vector2f[] { 
			new Vector2f(0.0325f, 0.0f),
			new Vector2f(-0.0325f, -0.0325f), 
			new Vector2f(0.0f, 0.0f),
			new Vector2f(-0.0325f, 0.0325f), 
		};
		renderList = new ArrayList<Vector2f[]>();
	}

	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}

	public boolean isDamaged() {
		return damaged;
	}

	public void rotateLeft(float delta) {
		angle += rotationDelta * delta;
	}

	public void rotateRight(float delta) {
		angle -= rotationDelta * delta;
	}

	public void setThrusting(boolean thrusting) {
		curAcc = thrusting ? acceleration : 0.0f;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public PrototypeBullet launchBullet() {
		Vector2f bulletPos = position.add(Vector2f.polar(angle, 0.0325f));
		return new PrototypeBullet(bulletPos, angle);
	}

	public void update(float time) {
		updatePosition(time);
		renderList.clear();
		Vector2f[] world = transformPolygon();
		renderList.add(world);
		wrapper.wrapPolygon(world, renderList);
	}

	private Vector2f[] transformPolygon() {
		Matrix3x3f mat = Matrix3x3f.rotate(angle);
		mat = mat.mul(Matrix3x3f.translate(position));
		return transform(polyman, mat);
	}

	private void updatePosition(float time) {
		Vector2f accel = Vector2f.polar(angle, curAcc);
		velocity = velocity.add(accel.mul(time));
		float maxSpeed = Math.min(maxVelocity / velocity.len(), 1.0f);
		velocity = velocity.mul(maxSpeed);
		float slowDown = 1.0f - friction * time;
		velocity = velocity.mul(slowDown);
		position = position.add(velocity.mul(time));
		position = wrapper.wrapPosition(position);
	}

	private Vector2f[] transform(Vector2f[] poly, Matrix3x3f mat) {
		Vector2f[] copy = new Vector2f[poly.length];
		for (int i = 0; i < poly.length; ++i) {
			copy[i] = mat.mul(poly[i]);
		}
		return copy;
	}

	public void draw(Graphics2D g, Matrix3x3f view) {
		g.setColor(new Color(50, 50, 50));
		for (Vector2f[] poly : renderList) {
			for (int i = 0; i < poly.length; ++i) {
				poly[i] = view.mul(poly[i]);
			}
			g.setColor(Color.DARK_GRAY);
			Utility.fillPolygon(g, poly);
			g.setColor(isDamaged() ? Color.RED : Color.GREEN);
			Utility.drawPolygon(g, poly);
		}
	}

	public boolean isTouching(PrototypeAsteroid asteroid) {
		for (Vector2f[] poly : renderList) {
			for (Vector2f v : poly) {
				if (asteroid.contains(v)) {
					return true;
				}
			}
		}
		return false;
	}
}