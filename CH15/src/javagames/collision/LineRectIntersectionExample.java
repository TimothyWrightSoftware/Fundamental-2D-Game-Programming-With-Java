package javagames.collision;

import java.awt.*;
import java.awt.event.MouseEvent;
import javagames.util.*;

public class LineRectIntersectionExample extends SimpleFramework {
	
	private static final float EPSILON = 0.00001f;
	private Vector2f[] rect;
	private Vector2f[] rectCpy;
	private Vector2f start;
	private Vector2f end;
	private Vector2f intersection;
	private float angle;
	private float rot;

	public LineRectIntersectionExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Line Rect Intersection";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		angle = 0.0f;
		rot = (float) (Math.PI / 6.0);
		rect = new Vector2f[] { 
			new Vector2f(-0.25f, 0.25f),
			new Vector2f(0.25f, 0.25f),
			new Vector2f(0.25f, -0.25f),
			new Vector2f(-0.25f, -0.25f),
		};
		rectCpy = new Vector2f[rect.length];
		start = new Vector2f();
		end = new Vector2f();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		end = getWorldMousePosition();
		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			start = new Vector2f(end);
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		angle += delta * rot;
		Matrix3x3f mat = Matrix3x3f.rotate(angle);
		for (int i = 0; i < rect.length; ++i) {
			rectCpy[i] = mat.mul(rect[i]);
		}
		Vector2f d = end.sub(start);
		float len = d.len();
		d = d.norm();
		Float t = lineRectIntersection(start, d, rectCpy);
		if (t != null && t > 0.0f && t < len) {
			intersection = start.add(d.mul(t));
		} else {
			intersection = null;
		}
	}

	private Float lineRectIntersection(Vector2f O, Vector2f d, Vector2f[] rect) {
		float largestMin = -Float.MAX_VALUE;
		float smallestMax = Float.MAX_VALUE;
		float swap;
		for (int i = 0; i < 2; ++i) {
			Vector2f n = rect[i].sub(rect[i + 1]);
			n = n.norm();
			float e0 = n.dot(rect[i].sub(O));
			float e1 = n.dot(rect[i + 1].sub(O));
			float f = n.dot(d);
			if (Math.abs(f) > EPSILON) {
				float t0 = e0 / f;
				float t1 = e1 / f;
				if (t0 > t1) {
					swap = t0;
					t0 = t1;
					t1 = swap;
				}
				largestMin = Math.max(largestMin, t0);
				smallestMax = Math.min(smallestMax, t1);
				if (largestMin > smallestMax)
					return null;
				if (smallestMax < 0)
					return null;
			} else if (e0 * e1 > 0) {
				return null;
			}
		}
		return largestMin > 0 ? largestMin : smallestMax;
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		boolean hasIntersection = intersection != null;
		g.drawString("Intersection: " + hasIntersection, 20, 35);
		g.drawString("Left-Click to change start position", 20, 50);
		Matrix3x3f view = getViewportTransform();
		for (int i = 0; i < rectCpy.length; ++i) {
			rectCpy[i] = view.mul(rectCpy[i]);
		}
		Utility.drawPolygon(g, rectCpy);
		Vector2f startCpy = view.mul(start);
		Vector2f endCpy = view.mul(end);
		g.drawLine((int) startCpy.x, (int) startCpy.y, (int) endCpy.x,
				(int) endCpy.y);
		if (hasIntersection) {
			g.setColor(Color.BLUE);
			Vector2f temp = view.mul(intersection);
			g.drawLine((int) temp.x - 20, (int) temp.y, (int) temp.x + 20,
					(int) temp.y);
			g.drawLine((int) temp.x, (int) temp.y - 20, (int) temp.x,
					(int) temp.y + 20);
		}
	}

	public static void main(String[] args) {
		launchApp(new LineRectIntersectionExample());
	}
}