package javagames.text;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javagames.util.*;

public class ConsoleOverlayExample extends SimpleFramework {
	
	private BufferedImage console;
	private int xConsole;
	private int yConsole;
	private float alpha;
	private Font consoleFont;
	private int fontHeight;
	private Vector<String> text;
	private float currY;
	private boolean boxes;
	private boolean hide;
	private float hidden;

	public ConsoleOverlayExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Console Overlay Example";
	}

	@Override
	protected void initialize() {
		super.initialize();
		consoleFont = new Font("Courier New", Font.BOLD, 20);
		FontMetrics fm = getFontMetrics(consoleFont);
		fontHeight = fm.getAscent() + fm.getDescent() + fm.getLeading();
		currY = 0;
		text = new Vector<String>();
		text.add("Press the 'H' key to");
		text.add("show and hide the console.");
		text.add("Hover the mouse over the");
		text.add("text to change transparency.");
		text.add("Press Space Bar to move text.");
		text.add("Press 'B' to toggle hidden boxes.");
		text.add(0, text.lastElement());
		int consoleHeight = fontHeight * (text.size() + 1);
		console = new BufferedImage(canvas.getWidth() - 40, consoleHeight,
				BufferedImage.TYPE_INT_ARGB);
		xConsole = 20;
		yConsole = 50;
		hide = false;
		hidden = 1.0f;
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		Point pos = mouse.getPosition();
		int minX = xConsole;
		int minY = yConsole;
		int maxX = minX + console.getWidth();
		int maxY = minY + console.getHeight();
		if (pos.x > minX && pos.x < maxX && pos.y > minY && pos.y < maxY) {
			alpha = 1.0f;
		} else {
			alpha = 0.75f;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			text.remove(0);
			text.add(text.get(0));
			currY = fontHeight;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
			boxes = !boxes;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_H)) {
			hide = !hide;
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		if (hide && hidden == 1.0f) {
			return; // don't bother
		}
		if (currY > 0) {
			currY -= delta * fontHeight;
		}
		Graphics2D g = console.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, console.getWidth(), console.getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(consoleFont);
		int x = 20;
		int y = (int) currY;
		Utility.drawString(g, x, y, text);
		if (boxes) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, console.getWidth(), fontHeight);
			g.fillRect(0, fontHeight * text.size(), console.getWidth(),
					fontHeight);
		} else {
			g.setColor(Color.GREEN);
			g.drawRect(0, 0, console.getWidth() - 1, fontHeight - 1);
			g.drawRect(0, fontHeight * text.size(), console.getWidth() - 1,
					fontHeight - 1);
		}
		if (hide && hidden < 1.0f) {
			hidden += delta;
			if (hidden > 1.0f)
				hidden = 1.0f;
		} else if (!hide && hidden > 0.0f) {
			hidden -= delta;
			if (hidden < 0.0f)
				hidden = 0.0f;
		}
		if (hidden > 0.0f) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			// clear left
			int xHide = (int) (console.getWidth() * hidden * 0.5f);
			g.fillRect(0, 0, xHide, console.getHeight());
			// clear right
			g.fillRect(console.getWidth() - xHide, 0, console.getWidth(),
					console.getHeight());
			// clear top
			int yHide = (int) (console.getHeight() * hidden * 0.5f);
			g.fillRect(0, 0, console.getWidth(), yHide);
			// clear bottom
			g.fillRect(0, console.getHeight() - yHide, console.getWidth(),
					console.getHeight());
		}
		g.dispose();
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		int dx = (canvas.getWidth()) / 8;
		int dy = (canvas.getHeight()) / 8;
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				g2d.setColor((i + j) % 2 == 0 ? Color.BLACK : Color.WHITE);
				g2d.fillRect(i * dx, j * dy, dx, dy);
			}
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g2d.drawImage(console, xConsole, yConsole, null);
	}

	public static void main(String[] args) {
		launchApp(new ConsoleOverlayExample());
	}
}