package javagames.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javagames.util.SimpleFramework;

public class BoxedTextProblem extends SimpleFramework {
	
	public BoxedTextProblem() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Boxed Text Problem";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		// Box this text...
		g.setColor(Color.BLACK);
		String box = "great Java, now what?";
		Font font = new Font("Arial", Font.PLAIN, 24);
		g.setFont(font);
		g.drawString(box, 20, 50);
		g.setColor(Color.RED);
		g.drawRect(20, 50, 200, 20);
	}

	public static void main(String[] args) {
		launchApp(new BoxedTextProblem());
	}
}