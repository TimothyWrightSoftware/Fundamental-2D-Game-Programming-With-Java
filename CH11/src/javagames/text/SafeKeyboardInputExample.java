package javagames.text;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javagames.util.*;

public class SafeKeyboardInputExample extends SafeKeyboardFramework {
	
	private int spacesCount;
	private float blink;
	private boolean drawCursor;
	private ArrayList<String> strings = new ArrayList<String>();

	public SafeKeyboardInputExample() {
		appSleep = 10L;
		appTitle = "Safe Keyboard Input Example";
		strings.add("");
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		while (keyboard.processEvent()) {
			if (keyboard.keyDownOnce(KeyEvent.VK_UP)) {
				appSleep += Math.min(appSleep * 2, 1000L);
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
				appSleep -= Math.min(appSleep / 2, 1000L);
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				spacesCount = 0;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
				spacesCount++;
			}
			processTypedChar();
		}
	}

	private void processTypedChar() {
		Character typedChar = keyboard.getKeyTyped();
		if (typedChar != null) {
			if (Character.isISOControl(typedChar)) {
				if (KeyEvent.VK_BACK_SPACE == typedChar) {
					removeCharacter();
				}
				if (KeyEvent.VK_ENTER == typedChar) {
					strings.add("");
				}
			} else {
				addCharacter(typedChar);
			}
			drawCursor = true;
			blink = 0.0f;
		}
	}

	private void addCharacter(Character c) {
		strings.add(strings.remove(strings.size() - 1) + c);
	}

	private void removeCharacter() {
		String line = strings.remove(strings.size() - 1);
		if (!line.isEmpty()) {
			strings.add(line.substring(0, line.length() - 1));
		}
		if (strings.isEmpty()) {
			strings.add("");
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		blink += delta;
		if (blink > 0.5f) {
			blink -= 0.5f;
			drawCursor = !drawCursor;
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		textPos = Utility.drawString(g, 20, textPos, "", "Sleep Value: "
				+ appSleep, "Space Count: " + spacesCount,
				"Press Up to increase sleep", "Press Down to decrease sleep",
				"Press ESC to clear spaces count", "", "", "");
		textPos = Utility.drawString(g, 20, textPos, strings);
		if (drawCursor) {
			FontMetrics fm = g.getFontMetrics();
			int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
			int y = textPos - height;
			int x = 20 + fm.stringWidth(strings.get(strings.size() - 1));
			g.drawString("_", x, y + fm.getAscent());
		}
	}

	public static void main(String[] args) {
		launchApp(new SafeKeyboardInputExample());
	}
}