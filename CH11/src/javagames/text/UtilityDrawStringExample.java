package javagames.text;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javagames.util.*;

public class UtilityDrawStringExample extends SimpleFramework {
	
	public UtilityDrawStringExample() {
		appFont = new Font("Courier New", Font.BOLD, 48);
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Utility Draw String Example";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_UP)) {
			int fontSize = appFont.getSize();
			appFont = new Font(appFont.getFamily(), appFont.getStyle(),
					fontSize + 2);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
			int fontSize = appFont.getSize();
			appFont = new Font(appFont.getFamily(), appFont.getStyle(),
					fontSize - 2);
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		textPos = Utility.drawString(g2d, 20, textPos, "Font Size: "
				+ g2d.getFont().getSize());
		textPos = Utility.drawString(g2d, 20, textPos, "Use the arrow keys",
				"to tweak the font size", "");
		g2d.setColor(Color.WHITE);
		textPos = Utility.drawString(g2d, 20, textPos, "Single String");
		g2d.setColor(Color.BLUE);
		textPos = Utility.drawString(g2d, 20, textPos, "Strings ", "With",
				"Commas");
		g2d.setColor(Color.DARK_GRAY);
		String[] array = new String[] { "Strings", "With", "Arrays", };
		textPos = Utility.drawString(g2d, 20, textPos, array);
		g2d.setColor(Color.RED);
		Vector<String> list = new Vector<String>();
		list.add("Strings");
		list.add("With");
		list.add("Lists");
		textPos = Utility.drawString(g2d, 20, textPos, list);
	}

	public static void main(String[] args) {
		launchApp(new UtilityDrawStringExample());
	}
}