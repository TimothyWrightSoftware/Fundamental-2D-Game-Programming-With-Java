package javagames.intersection;

import java.awt.*;
import java.awt.event.KeyEvent;
import javagames.util.*;

public class RectRectOverlapExample extends SimpleFramework {
	
	private Vector2f[] rect;
	private Vector2f[] rect0;
	private Vector2f rect0Pos;
	private float rect0Angle;
	private Vector2f[] rect1;
	private Vector2f rect1Pos;
	private float rect1Angle;
	private boolean intersection;

	public RectRectOverlapExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Rect Rect Overlap";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		// set up rectangles for testing
		rect = new Vector2f[] { new Vector2f(-0.25f, 0.25f),
				new Vector2f(0.25f, 0.25f), new Vector2f(0.25f, -0.25f),
				new Vector2f(-0.25f, -0.25f), };
		rect0 = new Vector2f[rect.length];
		rect0Pos = new Vector2f();
		rect0Angle = 0.0f;
		rect1 = new Vector2f[rect.length];
		rect1Pos = new Vector2f();
		rect1Angle = 0.0f;
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		// convert mouse coordinate for testing
		rect1Pos = getWorldMousePosition();
		// rotate rectangles
		if (keyboard.keyDown(KeyEvent.VK_A)) {
			rect0Angle += (float) (Math.PI / 4.0 * delta);
		}
		if (keyboard.keyDown(KeyEvent.VK_S)) {
			rect0Angle -= (float) (Math.PI / 4.0 * delta);
		}
		if (keyboard.keyDown(KeyEvent.VK_Q)) {
			rect1Angle += (float) (Math.PI / 4.0 * delta);
		}
		if (keyboard.keyDown(KeyEvent.VK_W)) {
			rect1Angle -= (float) (Math.PI / 4.0 * delta);
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		// translate objects
		Matrix3x3f mat = Matrix3x3f.identity();
		mat = mat.mul(Matrix3x3f.rotate(rect0Angle));
		mat = mat.mul(Matrix3x3f.translate(rect0Pos));
		for (int i = 0; i < rect.length; ++i) {
			rect0[i] = mat.mul(rect[i]);
		}
		mat = Matrix3x3f.identity();
		mat = mat.mul(Matrix3x3f.rotate(rect1Angle));
		mat = mat.mul(Matrix3x3f.translate(rect1Pos));
		for (int i = 0; i < rect.length; ++i) {
			rect1[i] = mat.mul(rect[i]);
		}
		// test for intersection
		intersection = rectRectIntersection(rect0, rect1);
	}

	private boolean rectRectIntersection(Vector2f[] A, Vector2f[] B) {
		
		// separating axis intersection algorithm
		Vector2f N0 = A[0].sub(A[1]).div(2.0f);
		Vector2f N1 = A[1].sub(A[2]).div(2.0f);
		Vector2f CA = A[0].add(A[2]).div(2.0f);
		
		float D0 = N0.len();
		float D1 = N1.len();
		N1 = N1.div(D1);
		N0 = N0.div(D0);
		
		Vector2f N2 = B[0].sub(B[1]).div(2.0f);
		Vector2f N3 = B[1].sub(B[2]).div(2.0f);
		Vector2f CB = B[0].add(B[2]).div(2.0f);
		
		float D2 = N2.len();
		float D3 = N3.len();
		N2 = N2.div(D2);
		N3 = N3.div(D3);
		
		Vector2f C = CA.sub(CB);
		
		float DA = D0;
		float DB = D2 * Math.abs(N2.dot(N0));
		DB += D3 * Math.abs(N3.dot(N0));
		
		if (DA + DB < Math.abs(C.dot(N0)))
			return false;
		
		DA = D1;
		DB = D2 * Math.abs(N2.dot(N1));
		DB += D3 * Math.abs(N3.dot(N1));
		
		if (DA + DB < Math.abs(C.dot(N1)))
			return false;
		
		DA = D2;
		DB = D0 * Math.abs(N0.dot(N2));
		DB += D1 * Math.abs(N1.dot(N2));
		
		if (DA + DB < Math.abs(C.dot(N2)))
			return false;
		
		DA = D3;
		DB = D0 * Math.abs(N0.dot(N3));
		DB += D1 * Math.abs(N1.dot(N3));
		
		if (DA + DB < Math.abs(C.dot(N3)))
			return false;
		
		return true;
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		// render instructions
		g.drawString("Intersection: " + intersection, 20, 35);
		g.drawString("A,S keys to rotate rect 1", 20, 50);
		g.drawString("Q,W keys to rotate rect 2", 20, 65);
		// draw rectangles
		g.setColor(intersection ? Color.BLUE : Color.BLACK);
		Matrix3x3f view = getViewportTransform();
		for (int i = 0; i < rect0.length; ++i) {
			rect0[i] = view.mul(rect0[i]);
		}
		Utility.drawPolygon(g, rect0);
		for (int i = 0; i < rect1.length; ++i) {
			rect1[i] = view.mul(rect1[i]);
		}
		Utility.drawPolygon(g, rect1);
	}

	public static void main(String[] args) {
		launchApp(new RectRectOverlapExample());
	}
}