package javagames.images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.util.Random;
import javagames.util.SimpleFramework;

public class ImageSpeedTest extends SimpleFramework {
	
	private Random rand = new Random();
	private GraphicsConfiguration gc;
	private BufferedImage bi;
	private VolatileImage vi;
	private boolean realtime = true;
	private boolean bufferedImage = true;

	public ImageSpeedTest() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 0L;
		appTitle = "Image Speed Test";
	}

	@Override
	protected void initialize() {
		super.initialize();
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(appWidth, appHeight);
		createVolatileImage();
		renderToBufferedImage();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
			bufferedImage = !bufferedImage;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_R)) {
			realtime = !realtime;
		}
	}

	private void createVolatileImage() {
		if (vi != null) {
			vi.flush();
			vi = null;
		}
		vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
	}

	@Override
	protected void render(Graphics g) {
		if (bufferedImage) {
			renderToBufferedImage(g);
		} else if (realtime) {
			renderToVolatileImageEveryFrame(g);
		} else {
			renderToVolatileImage(g);
		}
		super.render(g);
		// spit out help
		g.drawString("(B)uffered Image: " + bufferedImage, 20, 35);
		g.drawString("(R)eal Time Rendering: " + realtime, 20, 65);
	}

	private void renderToVolatileImage(Graphics g) {
		do {
			int returnCode = vi.validate(gc);
			if (returnCode == VolatileImage.IMAGE_RESTORED) {
				// Contents need to be restored
				renderVolatileImage();
			} else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				// incompatible GraphicsConfig
				createVolatileImage();
				renderVolatileImage();
			}
			g.drawImage(vi, 0, 0, null);
		} while (vi.contentsLost());
	}

	private void renderToVolatileImageEveryFrame(Graphics g) {
		do {
			int returnCode = vi.validate(gc);
			if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				// incompatible GraphicsConfig
				createVolatileImage();
			}
			renderVolatileImage();
			g.drawImage(vi, 0, 0, null);
		} while (vi.contentsLost());
	}

	protected void renderVolatileImage() {
		Graphics2D g2d = vi.createGraphics();
		g2d.setColor(new Color(rand.nextInt()));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.dispose();
	}

	private void renderToBufferedImage(Graphics g) {
		if (realtime) {
			renderToBufferedImage();
		}
		g.drawImage(bi, 0, 0, null);
	}

	private void renderToBufferedImage() {
		Graphics2D g2d = bi.createGraphics();
		g2d.setColor(new Color(rand.nextInt()));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.dispose();
	}

	public static void main(String[] args) {
		launchApp(new ImageSpeedTest());
	}
}