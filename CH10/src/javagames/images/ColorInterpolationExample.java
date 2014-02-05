package javagames.images;

import java.awt.Graphics;
import java.awt.image.*;
import javagames.util.SimpleFramework;

public class ColorInterpolationExample extends SimpleFramework {
	
	private BufferedImage img;
	private int[] pixels;
	private int[] clear;

	public ColorInterpolationExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 0L;
		appTitle = "Color Interpolation Example";
	}

	@Override
	protected void initialize() {
		super.initialize();
		img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
		// get pixels
		WritableRaster raster = img.getRaster();
		DataBuffer dataBuffer = raster.getDataBuffer();
		DataBufferInt data = (DataBufferInt) dataBuffer;
		pixels = data.getData();
		clear = new int[pixels.length];
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		createColorSquare();
	}

	private void createColorSquare() {
		int w = img.getWidth();
		float w0 = 0.0f;
		float w1 = w - 1.0f;
		int h = img.getHeight();
		float h0 = 0.0f;
		float h1 = h - 1.0f;
		System.arraycopy(clear, 0, pixels, 0, pixels.length);
		// Top-Left
		float tlr = 255.0f;
		float tlg = 0.0f;
		float tlb = 0.0f;
		// Bottom-Left
		float blr = 0.0f;
		float blg = 0.0f;
		float blb = 255.0f;
		// Top-Right
		float trr = 0.0f;
		float trg = 255.0f;
		float trb = 0.0f;
		// Bottom-Right
		float brr = 0.0f;
		float brg = 0.0f;
		float brb = 0.0f;
		float h1h0 = h1 - h0;
		float w1w0 = w1 - w0;
		for (int row = 0; row < h; ++row) {
			// left pixel
			int lr = (int) (tlr + (row - h0) * (blr - tlr) / h1h0);
			int lg = (int) (tlg + (row - h0) * (blg - tlg) / h1h0);
			int lb = (int) (tlb + (row - h0) * (blb - tlb) / h1h0);
			// right pixel
			int rr = (int) (trr + (row - h0) * (brr - trr) / h1h0);
			int rg = (int) (trg + (row - h0) * (brg - trg) / h1h0);
			int rb = (int) (trb + (row - h0) * (brb - trb) / h1h0);
			for (int col = 0; col < w; ++col) {
				int r = (int) (lr + (col - w0) * (rr - lr) / w1w0);
				int g = (int) (lg + (col - w0) * (rg - lg) / w1w0);
				int b = (int) (lb + (col - w0) * (rb - lb) / w1w0);
				int index = row * w + col;
				pixels[index] = 0xFF << 24 | r << 16 | g << 8 | b;
			}
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		int xPos = (canvas.getWidth() - img.getWidth()) / 2;
		int yPos = (canvas.getHeight() - img.getHeight()) / 2;
		g.drawImage(img, xPos, yPos, null);
	}

	public static void main(String[] args) {
		launchApp(new ColorInterpolationExample());
	}
}