package javagames.text;

import java.awt.*;
import javagames.util.SimpleFramework;

public class BoxedTextSolution extends SimpleFramework {
	
	public BoxedTextSolution() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 10L;
		appTitle = "Boxed Text Solution";
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
		// Set the font...
		Font font = new Font("Arial", Font.PLAIN, 24);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int x = 20;
		int y = 50;
		// Draw the top...
		String str = "draw the top line";
		g.setColor(Color.DARK_GRAY);
		g.drawString(str, x, y);
		int width = 100;
		g.setColor(Color.RED);
		g.drawLine(x, y, x + width, y);
		// Calculate the string width...
		y += 40;
		str = "Calculate correct width";
		g.setColor(Color.DARK_GRAY);
		g.drawString(str, x, y);
		width = fm.stringWidth(str);
		g.setColor(Color.GREEN);
		g.drawLine(x, y, x + width, y);
		// Use Ascent to offset y
		y += 40;
		g.setColor(Color.DARK_GRAY);
		str = "offset text with the Ascent";
		g.drawString(str, x, y + fm.getAscent());
		width = fm.stringWidth(str);
		g.setColor(Color.BLUE);
		g.drawLine(x, y, x + width, y);
		// Ascent+Decent+Leading=Height
		y += 40;
		g.setColor(Color.DARK_GRAY);
		str = "Calculate height of font";
		g.drawString(str, x, y + fm.getAscent());
		width = fm.stringWidth(str);
		g.setColor(Color.BLUE);
		g.drawLine(x, y, x + width, y);
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		g.drawLine(x, y + height, x + width, y + height);
		// Box the text
		y += 40;
		g.setColor(Color.DARK_GRAY);
		str = "Groovy, we got it!!!";
		g.drawString(str, x, y + fm.getAscent());
		width = fm.stringWidth(str);
		g.setColor(Color.BLUE);
		height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		g.drawRect(x, y, width, height);
	}

	public static void main(String[] args) {
		launchApp(new BoxedTextSolution());
	}
}