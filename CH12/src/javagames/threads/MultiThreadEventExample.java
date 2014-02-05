package javagames.threads;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javagames.util.SimpleFramework;
import javagames.util.Utility;

public class MultiThreadEventExample extends SimpleFramework {
	
	private OneShotEvent oneShot;
	private LoopEvent loop;
	private RestartEvent restart;

	public MultiThreadEventExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Multi-Thread Event Example";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		oneShot = new OneShotEvent(5000, 10);
		oneShot.initialize();
		loop = new LoopEvent(1000, 4);
		loop.initialize();
		restart = new RestartEvent(5000, 10);
		restart.initialize();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_1)) {
			oneShot.fire();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_2)) {
			oneShot.done();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_3)) {
			loop.fire();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_4)) {
			loop.done();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_5)) {
			restart.fire();
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		textPos = Utility.drawString(g, 20, textPos, "", "(1) Fire One Shot",
				"(2) Cancel One Shot", "(3) Start Loop", "(4) Stop Loop",
				"(5) Reusable");
	}

	@Override
	protected void terminate() {
		super.terminate();
		oneShot.shutDown();
		loop.shutDown();
		restart.shutDown();
	}

	public static void main(String[] args) {
		launchApp(new MultiThreadEventExample());
	}
}