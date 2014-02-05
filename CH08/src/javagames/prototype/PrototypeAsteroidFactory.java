package javagames.prototype;

import java.util.Random;
import javagames.prototype.PrototypeAsteroid.Size;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

public class PrototypeAsteroidFactory {
	private static final Vector2f[][] LARGE = { { // Large 0
		new Vector2f(-0.029733956f, 0.283255100f),
				new Vector2f(-0.183098610f, 0.111111104f),
				new Vector2f(-0.230046930f, -0.057902932f),
				new Vector2f(-0.092331770f, -0.139280080f),
				new Vector2f(0.117370844f, -0.142410040f),
				new Vector2f(0.161189320f, -0.048513293f),
				new Vector2f(0.151799680f, 0.067292630f),
				new Vector2f(0.195618150f, 0.129890440f),
				new Vector2f(0.017214417f, 0.158059480f), }, { // Large 1
		new Vector2f(-0.001763641f, 0.325800420f),
				new Vector2f(-0.082892360f, 0.220339000f),
				new Vector2f(-0.227513200f, 0.065913380f),
				new Vector2f(-0.206349200f, -0.141242860f),
				new Vector2f(-0.061728360f, -0.080979230f),
				new Vector2f(0.061728418f, -0.167608260f),
				new Vector2f(0.192239940f, -0.092278720f),
				new Vector2f(0.167548480f, 0.126177010f),
				new Vector2f(0.107583820f, 0.269303200f), }, { // Large 2
		new Vector2f(0.176838760f, -0.107981205f),
				new Vector2f(-0.070422530f, -0.076682330f),
				new Vector2f(-0.220657290f, -0.123630640f),
				new Vector2f(-0.273865400f, 0.048513293f),
				new Vector2f(-0.186228510f, 0.086071970f),
				new Vector2f(-0.214397490f, 0.223787190f),
				new Vector2f(-0.026604056f, 0.148669780f),
				new Vector2f(0.104851365f, 0.220657290f),
				new Vector2f(0.211267590f, 0.032863855f), }, 
	};
	private static final Vector2f[][] MEDIUM = { { // Medium 0
		new Vector2f(-0.045383394f, 0.186228510f),
				new Vector2f(-0.167449180f, 0.123630700f),
				new Vector2f(-0.067292630f, 0.039123654f),
				new Vector2f(-0.107981205f, -0.073552370f),
				new Vector2f(0.057902932f, -0.073552370f),
				new Vector2f(0.133020280f, 0.098591566f), }, { // Medium 1
		new Vector2f(-0.023474216f, 0.189358350f),
				new Vector2f(-0.107981205f, 0.107981205f),
				new Vector2f(-0.129890440f, -0.098591566f),
				new Vector2f(0.020344257f, -0.120500800f),
				new Vector2f(0.139280080f, -0.001564979f),
				new Vector2f(0.076682330f, 0.092331770f),
				new Vector2f(-0.007824719f, 0.095461670f), }, { // Medium 2
		new Vector2f(-0.064162790f, 0.158059480f),
				new Vector2f(-0.173708920f, 0.126760600f),
				new Vector2f(-0.142410040f, 0.023474216f),
				new Vector2f(-0.039123654f, 0.029733956f),
				new Vector2f(0.010954618f, -0.035993695f),
				new Vector2f(0.117370844f, 0.023474216f),
				new Vector2f(0.117370844f, 0.120500800f),
				new Vector2f(-0.001564979f, 0.092331770f), }, 
	};
	private static final Vector2f[][] SMALL = { { // Small 0
		new Vector2f(-0.048513293f, 0.057902990f),
				new Vector2f(-0.073552430f, -0.042253494f),
				new Vector2f(0.004694819f, -0.035993695f),
				new Vector2f(0.042253494f, 0.026604056f),
				new Vector2f(-0.001564979f, 0.082942130f), }, { // Small 1
		new Vector2f(0.067292690f, 0.007824719f),
				new Vector2f(-0.029733956f, -0.076682330f),
				new Vector2f(-0.067292630f, -0.042253494f),
				new Vector2f(-0.061032890f, 0.082942130f),
				new Vector2f(0.032863855f, 0.111111104f), }, { // Small 2
		new Vector2f(-0.007824719f, 0.089201870f),
				new Vector2f(-0.114241004f, 0.001564979f),
				new Vector2f(-0.004694819f, -0.067292690f),
				new Vector2f(0.039123654f, -0.039123654f),
				new Vector2f(-0.014084518f, 0.020344317f), }, 
	};
	private PolygonWrapper wrapper;
	private Random rand;

	public PrototypeAsteroidFactory(PolygonWrapper wrapper) {
		this.wrapper = wrapper;
		this.rand = new Random();
	}

	public PrototypeAsteroid createLargeAsteroid(Vector2f position) {
		PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
		asteroid.setPosition(position);
		asteroid.setPolygon(getRandomAsteroid(LARGE));
		asteroid.setSize(Size.Large);
		return asteroid;
	}

	public PrototypeAsteroid createMediumAsteroid(Vector2f position) {
		PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
		asteroid.setPosition(position);
		asteroid.setPolygon(getRandomAsteroid(MEDIUM));
		asteroid.setSize(Size.Medium);
		return asteroid;
	}

	public PrototypeAsteroid createSmallAsteroid(Vector2f position) {
		PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
		asteroid.setPosition(position);
		asteroid.setPolygon(getRandomAsteroid(SMALL));
		asteroid.setSize(Size.Small);
		return asteroid;
	}

	private Vector2f[] getRandomAsteroid(Vector2f[][] asteroids) {
		return mirror(asteroids[rand.nextInt(asteroids.length)]);
	}

	private Vector2f[] mirror(Vector2f[] polygon) {
		Vector2f[] mirror = new Vector2f[polygon.length];
		float x = rand.nextBoolean() ? 1.0f : -1.0f;
		float y = rand.nextBoolean() ? 1.0f : -1.0f;
		Matrix3x3f mat = Matrix3x3f.scale(x, y);
		for (int i = 0; i < polygon.length; ++i) {
			mirror[i] = mat.mul(polygon[i]);
		}
		return mirror;
	}
}