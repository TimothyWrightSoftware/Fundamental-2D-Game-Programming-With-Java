package javagames.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingFramework extends GameFramework {
	protected Canvas canvas;
	private JPanel mainPanel;
	private JPanel centerPanel;

	protected JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getCenterPanel(), BorderLayout.CENTER);
		}
		return mainPanel;
	}

	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setBackground(appBorder);
			centerPanel.setLayout(null);
			centerPanel.add(getCanvas());
		}
		return centerPanel;
	}

	private Canvas getCanvas() {
		if (canvas == null) {
			canvas = new Canvas();
			canvas.setBackground(appBackground);
		}
		return canvas;
	}

	private void setUpLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onCreateAndShowGUI() {
		
	}

	@Override
	protected void createFramework() {
		setUpLookAndFeel();
		getContentPane().add(getMainPanel());
		setLocationByPlatform(true);
		setSize(appWidth, appHeight);
		setTitle(appTitle);
		getContentPane().setBackground(appBorder);
		setSize(appWidth, appHeight);
		canvas.setSize(appWidth, appHeight); // bugfix Jan 2015
		getContentPane().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				onComponentResized(e);
			}
		});
		setupInput(getCanvas());
		onCreateAndShowGUI();
		setVisible(true);
		createBufferStrategy(getCanvas());
		getCanvas().requestFocus();
	}

	protected void onComponentResized(ComponentEvent e) {
		Dimension size = getCenterPanel().getSize();
		setupViewport(size.width, size.height);
		getCanvas().setLocation(vx, vy);
		getCanvas().setSize(vw, vh);
		getCanvas().repaint();
	}

	@Override
	protected void renderFrame(Graphics g) {
		g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
		render(g);
	}

	@Override
	public int getScreenWidth() {
		return getCanvas().getWidth();
	}

	@Override
	public int getScreenHeight() {
		return getCanvas().getHeight();
	}
}