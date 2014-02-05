package javagames.transform;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javagames.util.*;
import javax.swing.*;

public class PolarCoordinateExample extends JFrame implements Runnable {
	
	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 480;
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Point coord;

	public PolarCoordinateExample() {
		
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Polar Coordinate Example");
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
		coord = new Point();
	}

	private void processInput() {
		keyboard.poll();
		mouse.poll();
		coord = mouse.getPosition();
	}

	private void render(Graphics g) {
		g.setFont(new Font("Courier New", Font.BOLD, 24));
		g.setColor(Color.GREEN);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 40);
		int cx = SCREEN_W / 2;
		int cy = SCREEN_H / 2;
		g.setColor(Color.GRAY);
		g.drawLine(0, cy, SCREEN_W, cy);
		g.drawLine(cx, 0, cx, SCREEN_H);
		g.setColor(Color.GREEN);
		g.drawLine(cx, cy, coord.x, coord.y);
		int px = coord.x - cx;
		int py = cy - coord.y;
		double r = Math.sqrt(px * px + py * py);
		double rad = Math.atan2(py, px);
		double degrees = Math.toDegrees(rad);
		if (degrees < 0) {
			degrees = 360 + degrees;
		}
		double sx = r * Math.cos(rad);
		double sy = r * Math.sin(rad);
		String polar = String.format("(%.0f,%.0f\u00b0)", r, degrees);
		g.drawString(polar, 20, 60);
		String cart = String.format("(%.0f,%.0f)", sx, sy);
		g.drawString(cart, 20, 80);
		g.setColor(Color.WHITE);
		g.drawString(String.format("(%s,%s)", px, py), coord.x, coord.y);
		g.setColor(Color.BLUE);
		g.drawArc((int) (cx - r), (int) (cy - r), (int) (2 * r), (int) (2 * r),
				0, (int) degrees);
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
		final PolarCoordinateExample app = new PolarCoordinateExample();
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