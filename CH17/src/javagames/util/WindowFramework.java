package javagames.util;

import java.awt.*;
import java.awt.event.*;

public class WindowFramework extends GameFramework {
	
	private Canvas canvas;

	@Override
	protected void createFramework() {
		canvas = new Canvas();
		canvas.setBackground(appBackground);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setLocationByPlatform(true);
		if (appMaintainRatio) {
			getContentPane().setBackground(appBorder);
			setSize(appWidth, appHeight);
			setLayout(null);
			getContentPane().addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					onComponentResized(e);
				}
			});
		} else {
			canvas.setSize(appWidth, appHeight);
			pack();
		}
		setTitle(appTitle);
		setupInput(canvas);
		setVisible(true);
		createBufferStrategy(canvas);
		canvas.requestFocus();
	}

	protected void onComponentResized(ComponentEvent e) {
		Dimension size = getContentPane().getSize();
		setupViewport(size.width, size.height);
		canvas.setLocation(vx, vy);
		canvas.setSize(vw, vh);
	}

	public int getScreenWidth() {
		return canvas.getWidth();
	}

	public int getScreenHeight() {
		return canvas.getHeight();
	}

	@Override
	protected void renderFrame(Graphics g) {
		g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
		render(g);
	}
}