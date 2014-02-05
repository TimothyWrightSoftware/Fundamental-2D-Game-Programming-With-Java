package javagames.util;

import java.awt.*;
import java.awt.image.VolatileImage;

public class FullScreenFramework extends GameFramework {
	
	private static final int BIT_DEPTH = 32;
	private VolatileImage vi;
	private GraphicsConfiguration gc;
	private DisplayMode currentDisplayMode;

	@Override
	protected void createFramework() {
		setIgnoreRepaint(true);
		setUndecorated(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		currentDisplayMode = gd.getDisplayMode();
		if (!gd.isFullScreenSupported()) {
			System.err.println("ERROR: Not Supported!!!");
			System.exit(0);
		}
		if (appMaintainRatio) {
			setBackground(appBorder);
			setupViewport(appWidth, appHeight);
			createVolatileImage();
		} else {
			setBackground(appBackground);
		}
		gd.setFullScreenWindow(this);
		gd.setDisplayMode( 
			new DisplayMode( appWidth, appHeight, BIT_DEPTH, DisplayMode.REFRESH_RATE_UNKNOWN )
		);
		setupInput(this);
		createBufferStrategy(this);
	}

	public int getScreenWidth() {
		return appMaintainRatio ? vw : getWidth();
	}

	public int getScreenHeight() {
		return appMaintainRatio ? vh : getHeight();
	}

	private void renderVolatileImage(Graphics g) {
		do {
			int returnCode = vi.validate(gc);
			if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				createVolatileImage();
			}
			Graphics2D g2d = null;
			try {
				g2d = vi.createGraphics();
				g2d.setBackground(appBackground);
				g2d.clearRect(0, 0, getScreenWidth(), getScreenHeight());
				render(g2d);
			} finally {
				if (g2d != null)
					g2d.dispose();
			}
			g.drawImage(vi, vx, vy, null);
		} while (vi.contentsLost());
	}

	private void createVolatileImage() {
		if (vi != null) {
			vi.flush();
			vi = null;
		}
		vi = gc.createCompatibleVolatileImage(getScreenWidth(),
				getScreenHeight());
	}

	@Override
	protected void renderFrame(Graphics g) {
		if (appMaintainRatio) {
			g.clearRect(0, 0, getWidth(), getHeight());
			renderVolatileImage(g);
		} else {
			g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
			render(g);
		}
	}

	@Override
	protected void onShutDown() {
		super.onShutDown();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setDisplayMode(currentDisplayMode);
		gd.setFullScreenWindow(null);
	}
}