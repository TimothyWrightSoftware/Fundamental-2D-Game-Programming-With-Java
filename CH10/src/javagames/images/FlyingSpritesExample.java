package javagames.images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.*;
import javagames.util.*;


public class FlyingSpritesExample extends SimpleFramework {
	private static final int IMG_WIDTH = 256;
	private static final int IMG_HEIGHT = 256;

	private enum Interpolation {
		NearestNeighbor, BiLinear, BiCubic;
	}

	private enum RotationMethod {
		AffineTransform, AffineTransformOp, TexturePaint;
	}

	private boolean antialiased;
	private boolean transparent;
	private boolean greenBorder;
	private Interpolation interpolation;
	private RotationMethod rotationMethod;
	private BufferedImage sprite;
	private Vector2f[] positions;
	private float[] angles;
	private Vector2f[] velocities;
	private float[] rotations;

	public FlyingSpritesExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 0L;
		appTitle = "Flying Sprites Example";
		appBackground = Color.DARK_GRAY;
	}

	@Override
	protected void initialize() {
		super.initialize();
		positions = new Vector2f[] { 
			new Vector2f(-0.15f, 0.3f),
			new Vector2f(0.15f, 0.0f), 
			new Vector2f(0.25f, -0.3f),
			new Vector2f(-0.25f, -0.6f), 
		};
		velocities = new Vector2f[] { 
			new Vector2f(-0.04f, 0.0f),
			new Vector2f(-0.05f, 0.0f),
			new Vector2f(0.06f, 0.0f),
			new Vector2f(0.07f, -0.0f), 
		};
		angles = new float[] { 
			(float) Math.toRadians(0),
			(float) Math.toRadians(0), 
			(float) Math.toRadians(0),
			(float) Math.toRadians(0), 
		};
		rotations = new float[] { 1.0f, 0.75f, 0.5f, 0.25f };
		antialiased = false;
		transparent = false;
		greenBorder = false;
		interpolation = Interpolation.NearestNeighbor;
		rotationMethod = RotationMethod.AffineTransform;
		createSprite();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_A)) {
			antialiased = !antialiased;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_I)) {
			Interpolation[] values = Interpolation.values();
			int index = (interpolation.ordinal() + 1) % values.length;
			interpolation = values[index];
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_T)) {
			transparent = !transparent;
			createSprite();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_R)) {
			RotationMethod[] methods = RotationMethod.values();
			int index = (rotationMethod.ordinal() + 1) % methods.length;
			rotationMethod = methods[index];
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_G)) {
			greenBorder = !greenBorder;
			createSprite();
		}
	}

	private void createSprite() {
		createCheckerboard();
		if (transparent) {
			addTransparentBorder();
		}
		if (greenBorder) {
			drawGreenBorder();
		}
	}

	private void createCheckerboard() {
		sprite = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		int dx = IMG_WIDTH / 8;
		int dy = IMG_HEIGHT / 8;
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if ((i + j) % 2 == 0) {
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setColor(Color.BLACK);
				}
				g2d.fillRect(i * dx, j * dy, dx, dy);
			}
		}
		g2d.dispose();
	}

	private void addTransparentBorder() {
		int borderWidth = IMG_WIDTH + 8;
		int borderHeight = IMG_HEIGHT + 8;
		BufferedImage newSprite = new BufferedImage(borderWidth, borderHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newSprite.createGraphics();
		g2d.drawImage(sprite, 4, 4, null);
		g2d.dispose();
		sprite = newSprite;
	}

	private void drawGreenBorder() {
		Graphics2D g2d = sprite.createGraphics();
		g2d.setColor(Color.GREEN);
		g2d.drawRect(0, 0, sprite.getWidth() - 1, sprite.getHeight() - 1);
		g2d.dispose();
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		for (int i = 0; i < positions.length; ++i) {
			positions[i] = positions[i].add(velocities[i].mul(delta));
			if (positions[i].x >= 1.0f) {
				positions[i].x = -1.0f;
			} else if (positions[i].x <= -1.0f) {
				positions[i].x = 1.0f;
			}
			if (positions[i].y <= -1.0f) {
				positions[i].y = 1.0f;
			} else if (positions[i].y >= 1.0f) {
				positions[i].y = -1.0f;
			}
			angles[i] += rotations[i] * delta;
		}
	}

	@Override
	protected void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		setAntialasing(g2d);
		setInterpolation(g2d);
		switch (rotationMethod) {
		case AffineTransform:
			doAffineTransform(g2d);
			break;
		case AffineTransformOp:
			doAffineTransformOp(g2d);
			break;
		case TexturePaint:
			doTexturePaint(g2d);
			break;
		}
		super.render(g);
		g.drawString("(A)ntialiased: " + antialiased, 20, 35);
		g.drawString("(I)nterpolation: " + interpolation, 20, 50);
		g.drawString("(T)ransparent Border: " + transparent, 20, 65);
		g.drawString("(R)otation Method: " + rotationMethod, 20, 80);
		g.drawString("(G)reen Border: " + greenBorder, 20, 95);
	}

	private void setAntialasing(Graphics2D g2d) {
		if (antialiased) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	private void setInterpolation(Graphics2D g2d) {
		if (interpolation == Interpolation.NearestNeighbor) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		} else if (interpolation == Interpolation.BiLinear) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		} else if (interpolation == Interpolation.BiCubic) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
	}

	private AffineTransform createTransform(Vector2f position, float angle) {
		Vector2f screen = getViewportTransform().mul(position);
		AffineTransform transform = AffineTransform.getTranslateInstance(
				screen.x, screen.y);
		transform.rotate(angle);
		transform.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		return transform;
	}

	private void doAffineTransform(Graphics2D g2d) {
		for (int i = 0; i < positions.length; ++i) {
			AffineTransform tranform = createTransform(positions[i], angles[i]);
			g2d.drawImage(sprite, tranform, null);
		}
	}

	private AffineTransformOp createTransformOp(Vector2f position, float angle) {
		AffineTransform transform = createTransform(position, angle);
		if (interpolation == Interpolation.NearestNeighbor) {
			return new AffineTransformOp(transform,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		} else if (interpolation == Interpolation.BiLinear) {
			return new AffineTransformOp(transform,
					AffineTransformOp.TYPE_BILINEAR);
		} else { // interpolation == Interpolation.BiCubic
			return new AffineTransformOp(transform,
					AffineTransformOp.TYPE_BICUBIC);
		}
	}

	private void doAffineTransformOp(Graphics2D g2d) {
		for (int i = 0; i < positions.length; ++i) {
			AffineTransformOp op = createTransformOp(positions[i], angles[i]);
			g2d.drawImage(op.filter(sprite, null), 0, 0, null);
		}
	}

	private void doTexturePaint(Graphics2D g2d) {
		for (int i = 0; i < positions.length; ++i) {
			Rectangle2D anchor = new Rectangle2D.Float(0, 0, sprite.getWidth(),
					sprite.getHeight());
			TexturePaint paint = new TexturePaint(sprite, anchor);
			g2d.setPaint(paint);
			AffineTransform transform = createTransform(positions[i], angles[i]);
			g2d.setTransform(transform);
			g2d.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
		}
		// very important!!!
		g2d.setTransform(new AffineTransform());
	}

	public static void main(String[] args) {
		launchApp(new FlyingSpritesExample());
	}
}