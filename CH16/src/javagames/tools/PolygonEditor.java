package javagames.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javagames.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.w3c.dom.*;

public class PolygonEditor extends SwingFramework {
	
	private static final float BOUNDS = 0.80f;
	private static final float DELTA = BOUNDS / 32.0f;
	private File currentDirectory;
	private ExampleFileFilter imageFilter;
	private ExampleFileFilter xmlFilter;
	private JTextField widthControl;
	private JTextField heightControl;
	private float bounds = BOUNDS / 2.0f;
	private ArrayList<Vector2f> polygon;
	private Vector2f mousePos;
	private boolean closed;
	private BufferedImage sprite;
	private BufferedImage scaled;

	public PolygonEditor() {
		
		appBorder = new Color(0xFFEBCD);
		appBackground = Color.BLACK;
		appFont = new Font("Courier New", Font.PLAIN, 14);
		appFPSColor = Color.GREEN;
		appWidth = 640;
		appHeight = 640;
		appSleep = 20L;
		appMaintainRatio = true;
		appBorderScale = 0.95f;
		appTitle = "Polygon Editor";
		appWorldWidth = BOUNDS;
		appWorldHeight = BOUNDS;
		imageFilter = 
			new ExampleFileFilter("Image File",	new String[] { "png" });
		xmlFilter = new ExampleFileFilter("Model File", new String[] { "xml" });
		currentDirectory = new File(".");
	}

	@Override
	protected void onCreateAndShowGUI() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem(new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				PolygonEditor.this.dispatchEvent(new WindowEvent(
						PolygonEditor.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		menu.add(item);
		menuBar.add(menu);
		menu = new JMenu("Help");
		item = new JMenuItem(new AbstractAction("About") {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(PolygonEditor.this,
						"About this app!!!", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		// Lets make a toolbar...
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		JButton b = new JButton(UIManager.getIcon("FileChooser.fileIcon"));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onNew();
			}
		});
		bar.add(b);
		b = new JButton(UIManager.getIcon("FileChooser.directoryIcon"));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLoadXML();
			}
		});
		bar.add(b);
		b = new JButton(UIManager.getIcon("FileChooser.floppyDriveIcon"));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveXML();
			}
		});
		bar.add(b);
		getMainPanel().add(bar, BorderLayout.NORTH);
		JPanel p = new JPanel();
		JButton increase = new JButton("++");
		increase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				increaseBounds();
			}
		});
		p.add(increase);
		JButton decrease = new JButton("--");
		decrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decreaseBounds();
			}
		});
		p.add(decrease);
		p.add(new JLabel("Width"));
		widthControl = new JTextField(3);
		widthControl.setHorizontalAlignment(JTextField.CENTER);
		widthControl.setText("256");
		p.add(widthControl);
		p.add(new JLabel("Height"));
		heightControl = new JTextField(3);
		heightControl.setHorizontalAlignment(JTextField.CENTER);
		heightControl.setText("256");
		p.add(heightControl);
		JButton button = new JButton("Export");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportImage();
			}
		});
		p.add(button);
		button = new JButton("Import");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importImage();
			}
		});
		p.add(button);
		getMainPanel().add(p, BorderLayout.SOUTH);
	}

	protected void onNew() {
		polygon.clear();
		sprite = null;
		scaled = null;
	}

	protected void onLoadXML() {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		chooser.setFileFilter(xmlFilter);
		int retVal = chooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			currentDirectory = file;
			parseModel(file);
		}
	}

	private void parseModel(File file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			Document root = XMLUtility.parseDocument(reader);
			parseModel(root.getDocumentElement());
			closed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
	}

	protected void onSaveXML() {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		chooser.setFileFilter(xmlFilter);
		int retVal = chooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			currentDirectory = file;
			if (file.exists()) {
				int overwrite = JOptionPane.showConfirmDialog(this,
						"Overwrite existing file?");
				if (overwrite == JOptionPane.YES_OPTION) {
					writeXML(file);
				}
			} else {
				writeXML(file);
			}
		}
	}

	private void writeXML(File file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			writeXML(out);
		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	private void writeXML(PrintWriter out) {
		out.println("<model bounds=\"" + bounds + "\">");
		for (Vector2f point : polygon) {
			out.println("\t<coord x=\"" + point.x + "\" y=\"" + point.y
					+ "\" />");
		}
		out.println("</model>");
	}

	protected void increaseBounds() {
		bounds += DELTA;
		if (bounds > BOUNDS) {
			bounds = BOUNDS;
		}
	}

	protected void decreaseBounds() {
		bounds -= DELTA;
		if (bounds < DELTA) {
			bounds = DELTA;
		}
	}

	protected void exportImage() {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		chooser.setFileFilter(imageFilter);
		int retVal = chooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			currentDirectory = file;
			if (file.exists()) {
				int overwrite = JOptionPane.showConfirmDialog(this,
						"Overwrite existing file?");
				if (overwrite == JOptionPane.YES_OPTION) {
					exportImage(file);
				}
			} else {
				exportImage(file);
			}
		}
	}

	private void exportImage(File file) {
		int imageWidth = Integer.parseInt(widthControl.getText());
		int imageHeight = Integer.parseInt(heightControl.getText());
		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		Matrix3x3f view = Utility.createViewport(bounds, bounds, imageWidth,
				imageHeight);
		Graphics g = image.getGraphics();
		drawPolygon(g, view);
		g.dispose();
		try {
			System.out.println("Export: " + ImageIO.write(image, "png", file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void importImage() {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		chooser.setFileFilter(imageFilter);
		int retVal = chooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			currentDirectory = file;
			try {
				sprite = ImageIO.read(file);
				scaled = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseModel(Element model) {
		bounds = Float.parseFloat(model.getAttribute("bounds"));
		polygon.clear();
		for (Element coords : XMLUtility.getAllElements(model, "coord")) {
			float x = Float.parseFloat(coords.getAttribute("x"));
			float y = Float.parseFloat(coords.getAttribute("y"));
			polygon.add(new Vector2f(x, y));
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		polygon = new ArrayList<Vector2f>();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		mousePos = getWorldMousePosition();
		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			polygon.add(mousePos);
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON2)) {
			closed = !closed;
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
			if (!polygon.isEmpty()) {
				polygon.remove(polygon.size() - 1);
			}
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		textPos = Utility.drawString(g, 20, textPos, "Left Mouse: Add point",
				"Right Mouse: Remove point", "Center Mouse: Toggle Close");
		drawSprite(g);
		drawAxisLines(g);
		drawPolygon(g, getViewportTransform());
		drawBoundingBox(g);
	}

	private void drawSprite(Graphics g) {
		if (sprite == null) {
			return;
		}
		Vector2f topLeft = new Vector2f(-bounds / 2.0f, bounds / 2.0f);
		Vector2f bottomRight = new Vector2f(bounds / 2.0f, -bounds / 2.0f);
		Matrix3x3f view = getViewportTransform();
		topLeft = view.mul(topLeft);
		bottomRight = view.mul(bottomRight);
		int width = (int) Math.abs(topLeft.x - bottomRight.x);
		int height = (int) Math.abs(topLeft.y - bottomRight.y);
		if (scaled == null || scaled.getWidth() != width
				|| scaled.getHeight() != height) {
			scaled = Utility.scaleImage(sprite, width, height);
		}
		g.drawImage(scaled, (int) topLeft.x, (int) topLeft.y, null);
	}

	private void drawAxisLines(Graphics g) {
		g.setColor(Color.WHITE);
		Vector2f left = new Vector2f(appWorldWidth / 2.0f, 0.0f);
		Vector2f right = new Vector2f(-left.x, 0.0f);
		drawLine(g, getViewportTransform(), left, right);
		Vector2f top = new Vector2f(0.0f, appWorldHeight / 2.0f);
		Vector2f bottom = new Vector2f(0.0f, -top.y);
		drawLine(g, getViewportTransform(), top, bottom);
	}

	private void drawPolygon(Graphics g, Matrix3x3f view) {
		g.setColor(Color.GREEN);
		if (polygon.size() == 1) {
			drawPoint(g, view, polygon.get(0));
		}
		for (int i = 0; i < polygon.size() - 1; ++i) {
			drawLine(g, view, polygon.get(i), polygon.get(i + 1));
		}
		if (closed && polygon.size() > 1) {
			Vector2f P = polygon.get(polygon.size() - 1);
			Vector2f S = polygon.get(0);
			drawLine(g, view, S, P);
		}
		if (!(polygon.isEmpty() || closed)) {
			Vector2f P = polygon.get(polygon.size() - 1);
			Vector2f S = mousePos;
			drawLine(g, view, S, P);
		}
	}

	private void drawPoint(Graphics g, Matrix3x3f view, Vector2f v) {
		Vector2f s = view.mul(v);
		g.drawRect((int) s.x, (int) s.y, 1, 1);
	}

	private void drawLine(Graphics g, Matrix3x3f view, Vector2f v0, Vector2f v1) {
		Vector2f S = view.mul(v0);
		Vector2f P = view.mul(v1);
		g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
	}

	private void drawBoundingBox(Graphics g) {
		Vector2f[] bb = { 
			new Vector2f(-bounds / 2.0f, bounds / 2.0f),
			new Vector2f(bounds / 2.0f, bounds / 2.0f),
			new Vector2f(bounds / 2.0f, -bounds / 2.0f),
			new Vector2f(-bounds / 2.0f, -bounds / 2.0f), 
		};
		Matrix3x3f view = getViewportTransform();
		for (int i = 0; i < bb.length; ++i) {
			bb[i] = view.mul(bb[i]);
		}
		g.setColor(Color.WHITE);
		Utility.drawPolygon(g, bb);
	}

	public static void main(String[] args) {
		launchApp(new PolygonEditor());
	}
}