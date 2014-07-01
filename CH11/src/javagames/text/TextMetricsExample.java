package javagames.text;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javagames.util.*;

public class TextMetricsExample extends SimpleFramework {
	
	public TextMetricsExample() {
		appWidth = 640;
		appHeight = 480;
		appSleep = 10L;
		appTitle = "Text Metrics Example";
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40);
		g2d.setFont(font);
		g2d.setColor(Color.GREEN);
		String str = "Groovy Baby BLAH";
		int x = 50;
		int y = 50;
		g2d.drawString(str, x, y);
		// Text Layout gives floating point values
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout tl = new TextLayout(str, font, frc);
		// draw another line, should be at
		// y + ascent + decent + leading
		int newY = y
				+ (int) (tl.getAscent() + tl.getDescent() + tl.getLeading());
		g2d.drawString(str, x, newY);
		// draw centered Text
		// first lets draw the center of the window...
		g2d.setColor(Color.GRAY);
		int sw = canvas.getWidth();
		int sh = canvas.getHeight();
		int cx = sw / 2;
		int cy = sh / 2;
		g2d.drawLine(0, cy, sw, cy);
		g2d.drawLine(cx, 0, cx, sh);
		String center = "Should Center: Center Baby's @";
		// to calculate the x, need the width...
		int stringWidth = g2d.getFontMetrics().stringWidth(center);
		float dy = g2d.getFontMetrics().getLineMetrics(center, g2d)
				.getBaselineOffsets()[Font.CENTER_BASELINE];
		g2d.drawString(center, cx - stringWidth / 2, cy - dy);
		// draw the pixel where we are drawing the text...
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x - 1, y - 1, 3, 3);
		ArrayList<String> console = new ArrayList<String>();
		console.add("Baseline: " + tl.getBaseline());
		float[] baselineOffsets = tl.getBaselineOffsets();
		console.add("Baseline-Offset[ ROMAN ]: "
				+ baselineOffsets[Font.ROMAN_BASELINE]);
		console.add("Baseline-Offset[ CENTER ]: "
				+ baselineOffsets[Font.CENTER_BASELINE]);
		console.add("Baseline-Offset[ HANGING ]: "
				+ baselineOffsets[Font.HANGING_BASELINE]);
		console.add("Ascent: " + tl.getAscent());
		console.add("Descent: " + tl.getDescent());
		console.add("Leading: " + tl.getLeading());
		console.add("Advance: " + tl.getAdvance());
		console.add("Visible-Advance: " + tl.getVisibleAdvance());
		console.add("Bounds: " + toString(tl.getBounds()));
		Font propFont = new Font("Courier New", Font.BOLD, 14);
		g2d.setFont(propFont);
		int xLeft = x;
		int xRight = xLeft + (int) tl.getVisibleAdvance();
		// draw baseline
		g2d.setColor(Color.WHITE);
		int baselineY = y + (int) baselineOffsets[Font.ROMAN_BASELINE];
		g2d.drawLine(xLeft, baselineY, xRight, baselineY);
		g2d.drawString("roman baseline", xRight, baselineY);
		// draw center
		g2d.setColor(Color.BLUE);
		int centerY = y + (int) baselineOffsets[Font.CENTER_BASELINE];
		g2d.drawLine(xLeft, centerY, xRight, centerY);
		g2d.drawString("center baseline", xRight, centerY);
		// draw hanging
		g2d.setColor(Color.GRAY);
		int hangingY = y + (int) baselineOffsets[Font.HANGING_BASELINE];
		g2d.drawLine(xLeft, hangingY, xRight, hangingY);
		g2d.drawString("hanging baseline", xRight, hangingY);
		// draw Ascent
		g2d.setColor(Color.YELLOW);
		int propY = y - (int) tl.getAscent();
		g2d.drawLine(xLeft, propY, xRight, propY);
		TextLayout temp = new TextLayout("hanging baseline", propFont,
				g2d.getFontRenderContext());
		g2d.drawString("Ascent", xRight + temp.getVisibleAdvance(), propY);
		// draw Descent
		g2d.setColor(Color.RED);
		propY = y + (int) tl.getDescent();
		g2d.drawLine(xLeft, propY, xRight, propY);
		g2d.drawString("Descent", xRight, propY);
		// draw Leading
		g2d.setColor(Color.GREEN);
		propY = y + (int) tl.getDescent() + (int) tl.getLeading();
		g2d.drawLine(xLeft, propY, xRight, propY);
		temp = new TextLayout("Descent", propFont, g2d.getFontRenderContext());
		g2d.drawString("Leading", xRight + temp.getVisibleAdvance(), propY);
		// draw console output...
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setFont(new Font("Courier New", Font.BOLD, 12));
		Utility.drawString(g2d, 20, 300, console);
	}

	private String toString(Rectangle2D r) {
		return "[ x=" + r.getX() + ", y=" + r.getY() + ", w=" + r.getWidth()
				+ ", h=" + r.getHeight() + " ]";
	}

	public static void main(String[] args) {
		launchApp(new TextMetricsExample());
	}
}