package javagames.completegame.object;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import javagames.completegame.object.Asteroid.Size;
import javagames.util.*;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;

public class AsteroidFactory {
	
	private Vector<Asteroid> small;
	private Vector<Asteroid> medium;
	private Vector<Asteroid> large;
	private float worldWidth;
	private PolygonWrapper wrapper;
	private Random rand;

	public AsteroidFactory(PolygonWrapper wrapper, float worldWidth) {
		this.worldWidth = worldWidth;
		this.wrapper = wrapper;
		small = new Vector<Asteroid>();
		medium = new Vector<Asteroid>();
		large = new Vector<Asteroid>();
		rand = new Random();
	}

	public void loadModels(Element root) {
		for (Element model : XMLUtility.getAllElements(root, "model")) {
			parseModel(model);
		}
	}

	private void parseModel(Element model) {
		Asteroid asteroid = new Asteroid(wrapper);
		Vector<Vector2f> polygon = new Vector<Vector2f>();
		String modelSize = model.getAttribute("size");
		String image = model.getAttribute("sprite");
		String bounds = model.getAttribute("bounds");
		for (Element coords : XMLUtility.getAllElements(model, "coord")) {
			float x = Float.parseFloat(coords.getAttribute("x"));
			float y = Float.parseFloat(coords.getAttribute("y"));
			polygon.add(new Vector2f(x, y));
		}
		asteroid.setPolygon(polygon.toArray(new Vector2f[0]));
		BufferedImage bi = null;
		InputStream stream = ResourceLoader.load(
			AsteroidFactory.class,"res/assets/images/" + image, "/images/" + image
		);
		try {
			bi = ImageIO.read(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		float bound = Float.parseFloat(bounds);
		Vector2f topLeft = new Vector2f(-bound / 2.0f, bound / 2.0f);
		Vector2f bottomRight = new Vector2f(bound / 2.0f, -bound / 2.0f);
		Sprite sprite = new Sprite(bi, topLeft, bottomRight);
		asteroid.setSprite(sprite);
		Size size = Size.valueOf(modelSize);
		asteroid.setSize(size);
		if (size == Size.Large) {
			large.add(asteroid);
		} else if (size == Size.Medium) {
			medium.add(asteroid);
		} else if (size == Size.Small) {
			small.add(asteroid);
		}
	}

	public Asteroid getLargeAsteroid() {
		return getLargeAsteroid(getAstroidStartPosition());
	}

	public Asteroid getLargeAsteroid(Vector2f position) {
		return copy(getRandomAsteroid(large), position);
	}

	public Asteroid getMediumAsteroid() {
		return getMediumAsteroid(getAstroidStartPosition());
	}

	public Asteroid getMediumAsteroid(Vector2f position) {
		return copy(getRandomAsteroid(medium), position);
	}

	public Asteroid getSmallAsteroid() {
		return getSmallAsteroid(getAstroidStartPosition());
	}

	public Asteroid getSmallAsteroid(Vector2f position) {
		return copy(getRandomAsteroid(small), position);
	}

	public Asteroid copy(Asteroid template, Vector2f position) {
		Asteroid asteroid = new Asteroid(wrapper);
		asteroid.setPosition(position);
		asteroid.setSprite(template.getSprite());
		asteroid.setSize(template.getSize());
		asteroid.setPolygon(template.getPolygon());
		return asteroid;
	}

	private Vector2f getAstroidStartPosition() {
		float angle = (float) Math.toRadians(rand.nextInt(360));
		float minimum = worldWidth / 4.0f;
		float extra = rand.nextFloat() * minimum;
		float radius = minimum + extra;
		return Vector2f.polar(angle, radius);
	}

	private Asteroid getRandomAsteroid(List<Asteroid> asteroids) {
		return asteroids.get(rand.nextInt(asteroids.size()));
	}
}