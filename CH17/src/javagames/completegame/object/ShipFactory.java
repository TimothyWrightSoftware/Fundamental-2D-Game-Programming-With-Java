package javagames.completegame.object;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;
import javax.imageio.ImageIO;
import javagames.util.*;
import org.w3c.dom.Element;

public class ShipFactory {
	
	private PolygonWrapper wrapper;
	private Vector2f[] polygon;
	private Sprite shipRegular;
	private Sprite shipGlow;

	public ShipFactory(PolygonWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public void loadFactory(Element xml) {
		Vector<Vector2f> points = new Vector<Vector2f>();
		String spritePath = xml.getAttribute("sprite");
		String glowPath = xml.getAttribute("glow");
		String bounds = xml.getAttribute("bounds");
		for (Element coords : XMLUtility.getAllElements(xml, "coord")) {
			float x = Float.parseFloat(coords.getAttribute("x"));
			float y = Float.parseFloat(coords.getAttribute("y"));
			points.add(new Vector2f(x, y));
		}
		polygon = points.toArray(new Vector2f[0]);
		float bound = Float.parseFloat(bounds);
		Vector2f topLeft = new Vector2f(-bound / 2.0f, bound / 2.0f);
		Vector2f bottomRight = new Vector2f(bound / 2.0f, -bound / 2.0f);
		BufferedImage image = loadSprite(spritePath);
		shipRegular = new Sprite(image, topLeft, bottomRight);
		image = loadSprite(glowPath);
		shipGlow = new Sprite(image, topLeft, bottomRight);
	}

	public Ship createShip() {
		Ship ship = new Ship(wrapper);
		ship.setAlive(true);
		ship.setPolygon(polygon);
		ship.setGlowSprite(shipGlow);
		ship.setShipSprite(shipRegular);
		return ship;
	}

	private BufferedImage loadSprite(String path) {
		InputStream stream = ResourceLoader.load(
			ShipFactory.class, "res/assets/images/" + path, "/images/" + path
		);
		try {
			return ImageIO.read(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}