package javagames.completegame.state;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import javagames.completegame.admin.*;
import javagames.completegame.object.*;
import javagames.sound.*;
import javagames.util.*;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class GameLoading extends State {
	
	private String[] explosions = { 
		"EXPLOSION_large_01.wav",
		"EXPLOSION_large_02.wav", 
		"EXPLOSION_large_03.wav",
		"EXPLOSION_large_04.wav", 
		"EXPLOSION_large_05.wav", 
	};
	
	private ExecutorService threadPool;
	private List<Callable<Boolean>> loadTasks;
	private List<Future<Boolean>> loadResults;
	private int numberOfTasks;
	private float percent;
	private float wait;

	@Override
	public void enter() {
		
		threadPool = Executors.newCachedThreadPool();
		loadTasks = new ArrayList<Callable<Boolean>>();
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				InputStream stream = ResourceLoader.load( GameLoading.class,
						"res/assets/images/space_background_600x600.png",
						"/images/space_background_600x600.png" );
				BufferedImage image = ImageIO.read( stream );
				Vector2f worldTopLeft = new Vector2f(
					-GameConstants.WORLD_WIDTH / 2.0f,
					GameConstants.WORLD_HEIGHT / 2.0f 
				);
				Vector2f worldBottomRight = new Vector2f(
					GameConstants.WORLD_WIDTH / 2.0f,
					-GameConstants.WORLD_HEIGHT / 2.0f 
				);
				Sprite sprite =
						new Sprite( image, worldTopLeft, worldBottomRight );
				Matrix3x3f viewport =
						(Matrix3x3f)controller.getAttribute( "viewport" );
				sprite.scaleImage( viewport );
				controller.setAttribute( "background", sprite );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				HighScoreMgr mgr = new HighScoreMgr();
				mgr.loadHighScores();
				controller.setAttribute( "score", mgr );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				PolygonWrapper wrapper =
					(PolygonWrapper)controller.getAttribute( "wrapper" );
				AsteroidFactory factory = 
					new AsteroidFactory( wrapper, GameConstants.WORLD_WIDTH );
				factory.loadModels( loadXML( "new_asteroids.xml" ) );
				controller.setAttribute( "factory", factory );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				PolygonWrapper wrapper =
					(PolygonWrapper)controller.getAttribute( "wrapper" );
				ShipFactory factory = new ShipFactory( wrapper );
				Element xml = loadXML( "ship.xml" );
				factory.loadFactory( xml );
				controller.setAttribute( "ship-factory", factory );
				Acme acme = (Acme)controller.getAttribute( "ACME" );
				acme.setShip( factory.createShip() );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				byte[] soundBytes = loadSound( "DRONE9RE.WAV" );
				QuickLooper clip =
					new QuickLooper( new BlockingDataLine( soundBytes ) );
				clip.initialize();
				clip.open();
				controller.setAttribute( "thruster-clip", clip );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				byte[] soundBytes = loadSound( "WEAPON_scifi_fire_02.wav" );
				QuickRestart restartClip =
					new QuickRestart( new BlockingDataLine(soundBytes));
				restartClip.initialize();
				restartClip.open();
				controller.setAttribute( "fire-clip", restartClip );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				ArrayList<QuickRestart> explosion =
					new ArrayList<QuickRestart>();
				for( String path : explosions ) {
					byte[] soundBytes = loadSound( path );
					QuickRestart restartClip = new QuickRestart(
						new BlockingDataLine( soundBytes ) );
					restartClip.initialize();
					restartClip.open();
					explosion.add( restartClip );
				}
				controller.setAttribute( "explosions",
						explosion.toArray( new QuickRestart[ 0 ] ) );
				return Boolean.TRUE;
			}
		});
		
		loadTasks.add( new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				byte[] soundBytes = loadSound( "AMBIENCE_alien.wav" );
				// Java 7.0
				LoopEvent loopEvent = new LoopEvent(
					new BlockingClip( soundBytes ) );
				// Java 6.0
				// LoopEvent loopEvent = new LoopEvent(
				// new BlockingDataLine(
				// soundBytes ) );
				loopEvent.initialize();
				controller.setAttribute( "ambience", loopEvent );
				return Boolean.TRUE;
			} 
		});
		
		loadResults = new ArrayList<Future<Boolean>>();
		for( Callable<Boolean> task : loadTasks ) {
			loadResults.add( threadPool.submit( task ) );
		}
		numberOfTasks = loadResults.size();
		if( numberOfTasks == 0 ) {
			numberOfTasks = 1;
		}
	}

	private Element loadXML(String path) throws IOException, SAXException,
			ParserConfigurationException {
		InputStream model = ResourceLoader.load(GameLoading.class,
				"res/assets/xml/" + path, "/xml/" + path);
		Document document = XMLUtility.parseDocument(model);
		return document.getDocumentElement();
	}

	private byte[] loadSound(String path) {
		InputStream in = ResourceLoader.load(GameLoading.class,
				"res/assets/sound/" + path, "/sound/" + path);
		return readBytes(in);
	}

	private byte[] readBytes(InputStream in) {
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			while ((read = buf.read()) != -1) {
				out.write(read);
			}
			in.close();
			return out.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateObjects(float delta) {
		// remove finished tasks
		Iterator<Future<Boolean>> it = loadResults.iterator();
		while (it.hasNext()) {
			Future<Boolean> next = it.next();
			if (next.isDone()) {
				try {
					if (next.get()) {
						it.remove();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		// update progress bar
		percent = (numberOfTasks - loadResults.size()) / (float) numberOfTasks;
		if (percent >= 1.0f) {
			threadPool.shutdown();
			wait += delta;
		}
		if (wait > 1.0f && threadPool.isShutdown()) {
			LoopEvent loop = (LoopEvent) controller.getAttribute("ambience");
			loop.fire();
			 getController().setState(new PressSpaceToPlay());
		}
	}

	@Override
	public void render(Graphics2D g, Matrix3x3f view) {
		super.render(g, view);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.setColor(Color.GREEN);
		String message = "S P A C E R O C K S";
		Utility.drawCenteredString(g, app.getScreenWidth(),
				app.getScreenHeight() / 3, message);
		int vw = (int) (app.getScreenWidth() * .9f);
		int vh = (int) (app.getScreenWidth() * .05f);
		int vx = (app.getScreenWidth() - vw) / 2;
		int vy = (app.getScreenWidth() - vh) / 2;
		// fill in progress
		g.setColor(Color.GRAY);
		int width = (int) (vw * percent);
		g.fillRect(vx, vy, width, vh);
		// draw border
		g.setColor(Color.GREEN);
		g.drawRect(vx, vy, vw, vh);
	}
}