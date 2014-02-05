package javagames.collision;

import java.awt.*;
import javagames.util.*;

public class CircleLineIntersectionExample extends SimpleFramework {
	
	private Vector2f p0;
	private Vector2f p1;
	private Vector2f center;
	private float radius;
	private Float t0, t1;
	private Vector2f plane0;
	private Vector2f plane1;
	private Vector2f segment0;
	private Vector2f segment1;

	public CircleLineIntersectionExample() {
		appWidth = 640;
		appHeight = 640;
		appTitle = "Circle Line Intersection";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		radius = 0.125f;
		center = new Vector2f();
		p0 = new Vector2f(-0.65f, -0.2f);
		p1 = new Vector2f(0.50f, 0.2f);
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		center = getWorldMousePosition();
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		Vector2f d = p1.sub(p0);
		float len = d.len();
		d = d.norm();
		plane0 = plane1 = null;
		segment0 = segment1 = null;
		t0 = t1 = null;
		float[] intersections = lineCircleIntersection(p0, d, center, radius);
		if (intersections != null) {
			t0 = intersections[0];
			plane0 = p0.add(d.mul(t0));
			if (t0 >= 0.0f && t0 <= len) {
				segment0 = plane0;
			}
			t1 = intersections[1];
			plane1 = p0.add(d.mul(t1));
			if (t1 >= 0.0f && t1 <= len) {
				segment1 = plane1;
			}
		}
	}

	private float[] lineCircleIntersection(Vector2f O, Vector2f d, Vector2f C,
			float r) {
		Vector2f V = O.sub(C);
		float b = d.dot(V);
		float bb = b * b;
		float rr = r * r;
		float VV = V.dot(V);
		float c = VV - rr;
		if (bb < c) {
			return null;
		}
		float root = (float) Math.sqrt(bb - c);
		return new float[] { -b - root, -b + root };
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		g.drawString("T0: " + t0, 20, 35);
		g.drawString("T1: " + t1, 20, 50);
		drawLine(g, p0, p1);
		drawOval(g, center, radius);
		drawIntersections(g, plane0, segment0);
		drawIntersections(g, plane1, segment1);
	}

	private void drawLine(Graphics g, Vector2f p0, Vector2f p1) {
		Matrix3x3f view = getViewportTransform();
		Vector2f p0Cpy = view.mul(p0);
		Vector2f p1Cpy = view.mul(p1);
		g.drawLine((int) p0Cpy.x, (int) p0Cpy.y, (int) p1Cpy.x, (int) p1Cpy.y);
	}

	private void drawOval(Graphics g, Vector2f center, float radius) {
		Matrix3x3f view = getViewportTransform();
		Vector2f topLeft = new Vector2f(center.x - radius, center.y + radius);
		topLeft = view.mul(topLeft);
		Vector2f bottomRight = new Vector2f(center.x + radius, center.y
				- radius);
		bottomRight = view.mul(bottomRight);
		int circleX = (int) topLeft.x;
		int circleY = (int) topLeft.y;
		int circleWidth = (int) (bottomRight.x - topLeft.x);
		int circleHeight = (int) (bottomRight.y - topLeft.y);
		g.drawOval(circleX, circleY, circleWidth, circleHeight);
	}

	private void drawIntersections(Graphics g, Vector2f planeIntersection,
			Vector2f lineIntersection) {
		Matrix3x3f view = getViewportTransform();
		if (planeIntersection != null) {
			g.setColor(Color.BLACK);
			Vector2f intCpy = view.mul(planeIntersection);
			g.drawLine((int) intCpy.x - 20, (int) intCpy.y,
					(int) intCpy.x + 20, (int) intCpy.y);
			g.drawLine((int) intCpy.x, (int) intCpy.y - 20, (int) intCpy.x,
					(int) intCpy.y + 20);
		}
		if (lineIntersection != null) {
			g.setColor(Color.BLUE);
			Vector2f intCpy = view.mul(lineIntersection);
			g.drawLine((int) intCpy.x - 20, (int) intCpy.y + 20,
					(int) intCpy.x + 20, (int) intCpy.y - 20);
		}
	}

	public static void main(String[] args) {
		launchApp(new CircleLineIntersectionExample());
	}
}