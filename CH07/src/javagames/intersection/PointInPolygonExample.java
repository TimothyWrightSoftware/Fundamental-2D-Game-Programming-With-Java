package javagames.intersection;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javagames.util.*;

public class PointInPolygonExample extends SimpleFramework {
	
	private static final int MAX_POINTS = 10000;
	private ArrayList<Vector2f> poly;
	private ArrayList<Vector2f> polyCpy;
	private ArrayList<Vector2f> inside;
	private ArrayList<Vector2f> outside;
	private Vector2f mousePos;
	private boolean selected;
	private boolean winding;

	public PointInPolygonExample() {
		appWidth = 640;
		appHeight = 640;
		appTitle = "Point In Polygon Example";
		appBackground = Color.BLACK;
		appFPSColor = Color.GREEN;
	}

	@Override
	protected void initialize() {
		super.initialize();
		// polygon points and lists of point inside
		// and outside the polygon
		poly = new ArrayList<Vector2f>();
		polyCpy = new ArrayList<Vector2f>();
		inside = new ArrayList<Vector2f>();
		outside = new ArrayList<Vector2f>();
		mousePos = new Vector2f();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		mousePos = getWorldMousePosition();
		// draw polygon for algorithm testing
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			winding = !winding;
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			poly.add(mousePos);
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
			poly.clear();
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		// see if the mouse is inside the polygon
		selected = pointInPolygon(mousePos, poly, winding);
		// test random points against the polygon
		Random rand = new Random();
		inside.clear();
		outside.clear();
		for (int i = 0; i < MAX_POINTS; ++i) {
			float x = rand.nextFloat() * 2.0f - 1.0f;
			float y = rand.nextFloat() * 2.0f - 1.0f;
			Vector2f point = new Vector2f(x, y);
			if (pointInPolygon(point, poly, winding)) {
				inside.add(point);
			} else {
				outside.add(point);
			}
		}
	}

	private boolean pointInPolygon(Vector2f point, List<Vector2f> poly,
			boolean winding) {
		// point in polygon algorithm
		int inside = 0;
		if (poly.size() > 2) {
			Vector2f start = poly.get(poly.size() - 1);
			boolean startAbove = start.y >= point.y;
			for (int i = 0; i < poly.size(); ++i) {
				Vector2f end = poly.get(i);
				boolean endAbove = end.y >= point.y;
				if (startAbove != endAbove) {
					float m = (end.y - start.y) / (end.x - start.x);
					float x = start.x + (point.y - start.y) / m;
					if (x >= point.x) {
						if (winding) {
							inside += startAbove ? 1 : -1;
						} else {
							inside = inside == 1 ? 0 : 1;
						}
					}
				}
				startAbove = endAbove;
				start = end;
			}
		}
		return inside != 0;
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		// render instructions
		g.drawString("Winding: " + (winding ? "ON" : "OFF"), 20, 35);
		String mouse = String.format("Mouse: (%.2f,%.2f)", mousePos.x,
				mousePos.y);
		g.drawString(mouse, 20, 50);
		g.drawString("Left-Click to add points", 20, 65);
		g.drawString("Right-Click to clear points", 20, 80);
		g.drawString("Space Bar to toggle winding", 20, 95);
		Matrix3x3f view = getViewportTransform();
		// draw test polygon
		if (poly.size() > 1) {
			polyCpy.clear();
			for (Vector2f vector : poly) {
				polyCpy.add(view.mul(vector));
			}
			g.setColor(selected ? Color.GREEN : Color.BLUE);
			Utility.drawPolygon(g, polyCpy);
		}
		// draw inside point blue, outside points red
		g.setColor(Color.BLUE);
		for (Vector2f vector : inside) {
			Vector2f point = view.mul(vector);
			g.fillRect((int) point.x, (int) point.y, 1, 1);
		}
		g.setColor(Color.RED);
		for (Vector2f vector : outside) {
			Vector2f point = view.mul(vector);
			g.fillRect((int) point.x, (int) point.y, 1, 1);
		}
	}

	public static void main(String[] args) {
		launchApp(new PointInPolygonExample());
	}
}