package javagames.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import javagames.util.SimpleFramework;

public class TransparentImageExample extends SimpleFramework {
	
	private BufferedImage img;
	private float shift;

	public TransparentImageExample() {
		appWidth = 400;
		appHeight = 300;
		appSleep = 10L;
		appTitle = "Transparent Image Example";
		appBackground = Color.DARK_GRAY;
	}

	@Override
	protected void initialize() {
		super.initialize();
		img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		int w = 8;
		int h = 8;
		int dx = img.getWidth() / w;
		int dy = img.getHeight() / h;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				if ((i + j) % 2 == 0) {
					g2d.setColor(Color.WHITE);
					g2d.fillRect(i * dx, j * dy, dx, dy);
				}
			}
		}
		g2d.dispose();
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		int ribbonHeight = canvas.getHeight() / 5;
		shift += delta * ribbonHeight;
		if (shift > ribbonHeight) {
			shift -= ribbonHeight;
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		// draw shifting background
		int hx = canvas.getHeight() / 5;
		g.setColor(Color.LIGHT_GRAY);
		for (int i = -1; i < 5; ++i) {
			g.fillRect(0, (int) shift + hx * i, canvas.getWidth(), hx / 2);
		}
		int x = (canvas.getWidth() - img.getWidth()) / 2;
		int y = (canvas.getHeight() - img.getHeight()) / 2;
		g.drawImage(img, x, y, null);
	}

	public static void main(String[] args) {
		launchApp(new TransparentImageExample());
	}
}