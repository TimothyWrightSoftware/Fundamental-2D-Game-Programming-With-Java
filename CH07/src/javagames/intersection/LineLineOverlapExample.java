package javagames.intersection;

import java.awt.*;
import java.awt.event.MouseEvent;
import javagames.util.*;

public class LineLineOverlapExample extends SimpleFramework {
	
	private Vector2f P, Q;
	private Vector2f start, end;
	boolean overlap = false;

	public LineLineOverlapExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Line Line Overlap";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		P = new Vector2f(-0.6f, 0.4f);
		Q = new Vector2f(0.6f, -0.4f);
		start = new Vector2f(0.8f, 0.8f);
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
		overlap = lineLineOverlap(P, Q, start, end);
	}

	private boolean lineLineOverlap(Vector2f A, Vector2f B, Vector2f P,
			Vector2f Q) {
		Vector2f C0 = A.add(B).div(2.0f);
		Vector2f C1 = P.add(Q).div(2.0f);
		Vector2f C = C0.sub(C1);
		Vector2f r0 = A.sub(C0);
		Vector2f r1 = P.sub(C1);
		Vector2f N0 = r0.perp().norm();
		Vector2f N1 = r1.perp().norm();
		float abs1 = Math.abs(N0.dot(C));
		float abs2 = Math.abs(N0.dot(r1));
		if (abs1 > abs2)
			return false;
		abs1 = Math.abs(N1.dot(C));
		abs2 = Math.abs(N1.dot(r0));
		if (abs1 > abs2)
			return false;
		return true;
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		g.drawString("Overlap: " + overlap, 20, 35);
		g.drawString("Left Click for new line", 20, 50);
		g.setColor(overlap ? Color.BLUE : Color.BLACK);
		Matrix3x3f view = getViewportTransform();
		Vector2f v0 = view.mul(P);
		Vector2f v1 = view.mul(Q);
		g.drawLine((int) v0.x, (int) v0.y, (int) v1.x, (int) v1.y);
		v0 = view.mul(start);
		v1 = view.mul(end);
		g.drawLine((int) v0.x, (int) v0.y, (int) v1.x, (int) v1.y);
	}

	public static void main(String[] args) {
		launchApp(new LineLineOverlapExample());
	}
}