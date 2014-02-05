package javagames.collision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;
import javagames.util.*;

public class BouncingPointExample extends SimpleFramework {
	
	private static final float EPSILON = 0.000001f;
	private static final int MAX_POINTS = 4096;
	private static final int MIN_POINTS = 8;
	private static final int MULTIPLE = 2;
	private int pointCount;
	private Vector2f[] points;
	private Vector2f[] velocities;
	private Vector2f[] polygon;
	private Vector2f[] polygonCpy;

	public BouncingPointExample() {
		appTitle = "Bouncing Point Example";
		appWidth = 640;
		appHeight = 640;
		appSleep = 1L;
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	protected void initialize() {
		super.initialize();
		polygon = new Vector2f[] {
			new Vector2f(0.09233177f, -0.22378719f),
			new Vector2f(-0.21752739f, 0.16431928f),
			new Vector2f(-0.37089205f, 0.14553994f),
			new Vector2f(-0.46478873f, 0.32707357f),
			new Vector2f(-0.21439749f, 0.41784036f),
			new Vector2f(-0.05790299f, 0.31768388f),
			new Vector2f(-0.107981205f, 0.17370892f),
			new Vector2f(0.08607197f, -0.101721406f),
			new Vector2f(0.2832551f, -0.042253494f),
			new Vector2f(0.12989044f, 0.29577464f),
			new Vector2f(0.4522692f, 0.042253554f),
			new Vector2f(0.5054773f, 0.32707357f),
			new Vector2f(0.5899843f, -0.098591566f),
			new Vector2f(0.85289514f, -0.08920181f),
			new Vector2f(0.5117371f, -0.18935835f),
			new Vector2f(0.458529f, -0.5649452f),
			new Vector2f(0.3552426f, -0.08920181f),
			new Vector2f(0.16744912f, -0.22378719f),
			new Vector2f(0.33959305f, -0.342723f),
			new Vector2f(0.26760566f, -0.5962441f),
			new Vector2f(-0.17370892f, -0.56807506f),
			new Vector2f(-0.22065729f, -0.26134586f), 
		};
		polygonCpy = new Vector2f[polygon.length];
		pointCount = MIN_POINTS;
		createPoints();
	}

	private void createPoints() {
		Random rand = new Random();
		points = new Vector2f[pointCount];
		velocities = new Vector2f[points.length];
		for (int i = 0; i < points.length; ++i) {
			points[i] = new Vector2f();
			double rad = Math.toRadians(rand.nextInt(360));
			double distancePerSecond = rand.nextFloat() + 0.5f;
			float vx = (float) (distancePerSecond * Math.cos(rad));
			float vy = (float) (distancePerSecond * Math.sin(rad));
			velocities[i] = new Vector2f(vx, vy);
		}
	}

	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			createPoints();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_UP)) {
			pointCount = clamp(pointCount * MULTIPLE);
			createPoints();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
			pointCount = clamp(pointCount / MULTIPLE);
			createPoints();
		}
	}

	private int clamp(int count) {
		if (count < MIN_POINTS)
			return MIN_POINTS;
		if (count > MAX_POINTS)
			return MAX_POINTS;
		return count;
	}

	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		for (int i = 0; i < points.length; ++i) {
			Vector2f velocity = velocities[i];
			Vector2f start = points[i];
			Vector2f end = start.add(velocity.mul(delta));
			Vector2f newVelocity = getNewVelocity(start, end, velocity);
			if (newVelocity != null) {
				velocities[i] = newVelocity;
			} else {
				points[i] = end;
			}
		}
	}

	private Vector2f getNewVelocity(Vector2f start, Vector2f end,
			Vector2f velocity) {
		Vector2f P = null;
		Vector2f S = polygon[polygon.length - 1];
		for (int j = 0; j < polygon.length; ++j) {
			P = polygon[j];
			if (hitPolygon(start, end, S, P)) {
				return calculateReflection(velocity, S, P);
			}
			S = P;
		}
		return null;
	}

	private boolean hitPolygon(Vector2f start, Vector2f end, Vector2f S,
			Vector2f P) {
		if (isZero(getPointLineDistance(end, S, P))) {
			return true;
		} else if (!isZero(getPointLineDistance(start, S, P))) {
			return lineLineIntersection(start, end, S, P);
		} else {
			return false;
		}
	}

	private boolean isZero(float value) {
		return Math.abs(value) < EPSILON;
	}

	private float getPointLineDistance(Vector2f P, Vector2f S, Vector2f Q) {
		Vector2f v = Q.sub(S);
		Vector2f n = v.perp().norm();
		return n.dot(P.sub(Q));
	}

	private boolean lineLineIntersection(Vector2f A, Vector2f B, Vector2f C,
			Vector2f D) {
		Vector2f DsubC = D.sub(C);
		Vector2f DsubCperp = DsubC.perp();
		Vector2f AsubB = A.sub(B);
		float f = DsubCperp.dot(AsubB);
		if (Math.abs(f) < EPSILON) {
			return false; // zero denom
		}
		Vector2f AsubC = A.sub(C);
		float d = DsubCperp.dot(AsubC);
		if (f > 0) {
			if (d < 0 || d > f)
				return false;
		} else {
			if (d > 0 || d < f)
				return false;
		}
		Vector2f BsubA = B.sub(A);
		Vector2f BsubAperp = BsubA.perp();
		float e = BsubAperp.dot(AsubC);
		if (f > 0) {
			if (e < 0 || e > f)
				return false;
		} else {
			if (e > 0 || e < f)
				return false;
		}
		return true;
	}

	private Vector2f calculateReflection(Vector2f V, Vector2f P0, Vector2f P1) {
		Vector2f Pv = P0.sub(P1);
		Vector2f n = Pv.perp().norm();
		Vector2f Vn = n.mul(V.dot(n));
		Vector2f Vp = V.sub(Vn);
		return Vp.sub(Vn);
	}

	protected void render(Graphics g) {
		super.render(g);
		g.drawString("Points: " + pointCount, 20, 35);
		g.drawString("Up arrow to increase points", 20, 50);
		g.drawString("Up arrow to decrease points", 20, 65);
		g.drawString("Space Bar to reset points", 20, 80);
		Matrix3x3f view = getViewportTransform();
		for (int i = 0; i < polygon.length; ++i) {
			polygonCpy[i] = view.mul(polygon[i]);
		}
		Utility.drawPolygon(g, polygonCpy);
		for (int i = 0; i < points.length; ++i) {
			Vector2f copy = view.mul(points[i]);
			g.drawRect((int) copy.x, (int) copy.y, 1, 1);
		}
	}

	public static void main(String[] args) {
		launchApp(new BouncingPointExample());
	}
}