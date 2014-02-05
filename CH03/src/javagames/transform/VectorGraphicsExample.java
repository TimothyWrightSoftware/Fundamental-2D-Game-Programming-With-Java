package javagames.transform;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javagames.util.*;
import javax.swing.*;

public class VectorGraphicsExample extends JFrame implements Runnable {
	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 480;
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Vector2f[] polygon;
	private Vector2f[] world;
	private float tx, ty;
	private float vx, vy;
	private float rot, rotStep;
	private float scale, scaleStep;
	private float sx, sxStep;
	private float sy, syStep;
	private boolean doTranslate;
	private boolean doScale;
	private boolean doRotate;
	private boolean doXShear;
	private boolean doYShear;

	public VectorGraphicsExample() {
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(640, 480);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Vector Graphics Example");
		setIgnoreRepaint(true);
		pack();
		// Add key listeners
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		// Add mouse listeners
		// For full screen : mouse = new RelativeMouseInput( this );
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		running = true;
		initialize();
		while (running) {
			gameLoop();
		}
	}

	private void gameLoop() {
		processInput();
		processObjects();
		renderFrame();
		sleep(10L);
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {
		}
	}

	private void initialize() {
		frameRate = new FrameRate();
		frameRate.initialize();
		polygon = new Vector2f[] { new Vector2f(10, 0), new Vector2f(-10, 8),
				new Vector2f(0, 0), new Vector2f(-10, -8), };
		world = new Vector2f[polygon.length];
		reset();
	}

	private void reset() {
		tx = SCREEN_W / 2;
		ty = SCREEN_H / 2;
		vx = vy = 2;
		rot = 0.0f;
		rotStep = (float) Math.toRadians(1.0);
		scale = 1.0f;
		scaleStep = 0.1f;
		sx = sy = 0.0f;
		sxStep = syStep = 0.01f;
		doRotate = doScale = doTranslate = false;
		doXShear = doYShear = false;
	}

	private void processInput() {
		keyboard.poll();
		mouse.poll();
		if (keyboard.keyDownOnce(KeyEvent.VK_R)) {
			doRotate = !doRotate;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_S)) {
			doScale = !doScale;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_T)) {
			doTranslate = !doTranslate;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_X)) {
			doXShear = !doXShear;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_Y)) {
			doYShear = !doYShear;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			reset();
		}
	}

	private void processObjects() {
		// copy data...
		for (int i = 0; i < polygon.length; ++i) {
			world[i] = new Vector2f(polygon[i]);
		}
		if (doScale) {
			scale += scaleStep;
			if (scale < 1.0 || scale > 5.0) {
				scaleStep = -scaleStep;
			}
		}
		if (doRotate) {
			rot += rotStep;
			if (rot < 0.0f || rot > 2 * Math.PI) {
				rotStep = -rotStep;
			}
		}
		if (doTranslate) {
			tx += vx;
			if (tx < 0 || tx > SCREEN_W) {
				vx = -vx;
			}
			ty += vy;
			if (ty < 0 || ty > SCREEN_H) {
				vy = -vy;
			}
		}
		if (doXShear) {
			sx += sxStep;
			if (Math.abs(sx) > 2.0) {
				sxStep = -sxStep;
			}
		}
		if (doYShear) {
			sy += syStep;
			if (Math.abs(sy) > 2.0) {
				syStep = -syStep;
			}
		}
		for (int i = 0; i < world.length; ++i) {
			world[i].shear(sx, sy);
			world[i].scale(scale, scale);
			world[i].rotate(rot);
			world[i].translate(tx, ty);
		}
	}

	private void render(Graphics g) {
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.setColor(Color.GREEN);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("Translate(T): " + doTranslate, 20, 35);
		g.drawString("Rotate(R) : " + doRotate, 20, 50);
		g.drawString("Scale(S) : " + doScale, 20, 65);
		g.drawString("X-Shear(X) : " + doXShear, 20, 80);
		g.drawString("Y-Shear(Y) : " + doYShear, 20, 95);
		g.drawString("Press [SPACE] to reset", 20, 110);
		Vector2f S = world[world.length - 1];
		Vector2f P = null;
		for (int i = 0; i < world.length; ++i) {
			P = world[i];
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final VectorGraphicsExample app = new VectorGraphicsExample();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}