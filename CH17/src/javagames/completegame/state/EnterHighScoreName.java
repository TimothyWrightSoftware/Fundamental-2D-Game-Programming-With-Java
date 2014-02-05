package javagames.completegame.state;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javagames.util.Matrix3x3f;

public class EnterHighScoreName extends AttractState {
	
	class GameLetter {
		public String letter;
		public int width;

		public GameLetter(String letter, int width) {
			this.letter = letter;
			this.width = width;
		}
	}

	private static final long STEP = (long) 0.10E9;
	private static final int FONT_SIZE = 32;
	private int maxWidth;
	private GameLetter[] letters;
	private int rowCount;
	private int colCount;
	private String name = "";
	private long heldDown = 0;
	private long last = 0;
	private int selectedRow = 0;
	private int selectedCol = 0;
	private GameState state;
	private boolean finished = false;

	public EnterHighScoreName(GameState state) {
		this.state = state;
	}

	@Override
	public void enter() {
		super.enter();
		Font font = new Font("Arial", Font.BOLD, FONT_SIZE);
		FontMetrics fm = app.getFontMetrics(font);
		Vector<GameLetter> temp = new Vector<GameLetter>();
		maxWidth = Integer.MIN_VALUE;
		maxWidth = addLetter(fm, " ", maxWidth, temp);
		maxWidth = addLetters(fm, (int) 'A', (int) 'Z', maxWidth, temp);
		maxWidth = addLetters(fm, (int) 'a', (int) 'z', maxWidth, temp);
		maxWidth = addLetters(fm, (int) '0', (int) '9', maxWidth, temp);
		maxWidth = addLetter(fm, "!", maxWidth, temp);
		maxWidth = addLetter(fm, "_", maxWidth, temp);
		maxWidth = addLetter(fm, "?", maxWidth, temp);
		maxWidth = addLetter(fm, "*", maxWidth, temp);
		maxWidth = addLetter(fm, "$", maxWidth, temp);
		maxWidth = addLetter(fm, "del", maxWidth, temp);
		maxWidth = addLetter(fm, "end", maxWidth, temp);
		letters = temp.toArray(new GameLetter[0]);
		colCount = 10;
		rowCount = letters.length / colCount;
	}

	private int addLetters(FontMetrics fm, int start, int end, int maxWidth,
			Vector<GameLetter> letters) {
		for (int i = start; i <= end; ++i) {
			maxWidth = addLetter(fm, "" + (char) i, maxWidth, letters);
		}
		return maxWidth;
	}

	private int addLetter(FontMetrics fm, String letter, int max,
			Vector<GameLetter> letters) {
		int strWidth = fm.stringWidth(letter);
		max = Math.max(max, strWidth);
		letters.add(new GameLetter(letter, strWidth));
		return max;
	}

	@Override
	public void processInput(float delta) {
		if (keys.keyDownOnce(KeyEvent.VK_LEFT)) {
			selectedCol = roll(selectedCol - 1, 0, colCount - 1);
			resetHeldKey();
		}
		if (keys.keyDown(KeyEvent.VK_LEFT) && updateHeldKey()) {
			selectedCol = roll(selectedCol - 1, 0, colCount - 1);
		}
		if (keys.keyDownOnce(KeyEvent.VK_RIGHT)) {
			selectedCol = roll(selectedCol + 1, 0, colCount - 1);
			resetHeldKey();
		}
		if (keys.keyDown(KeyEvent.VK_RIGHT) && updateHeldKey()) {
			selectedCol = roll(selectedCol + 1, 0, colCount - 1);
		}
		if (keys.keyDownOnce(KeyEvent.VK_UP)) {
			selectedRow = roll(selectedRow - 1, 0, rowCount - 1);
			resetHeldKey();
		}
		if (keys.keyDown(KeyEvent.VK_UP) && updateHeldKey()) {
			selectedRow = roll(selectedRow - 1, 0, rowCount - 1);
		}
		if (keys.keyDownOnce(KeyEvent.VK_DOWN)) {
			selectedRow = roll(selectedRow + 1, 0, rowCount - 1);
			resetHeldKey();
		}
		if (keys.keyDown(KeyEvent.VK_DOWN) && updateHeldKey()) {
			selectedRow = roll(selectedRow + 1, 0, rowCount - 1);
		}
		if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
			int selected = getSelectedIndex(selectedRow, selectedCol);
			updateHighScoreRecord(letters[selected].letter);
		}
	}

	private void resetHeldKey() {
		last = System.nanoTime();
		heldDown = -STEP;
	}

	private boolean updateHeldKey() {
		long current = System.nanoTime();
		heldDown += current - last;
		last = current;
		if (heldDown > STEP) {
			heldDown -= STEP;
			return true;
		}
		return false;
	}

	@Override
	public void render(Graphics2D g, Matrix3x3f view) {
		super.render(g, view);
		g.setColor(Color.GREEN);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font font = new Font("Arial", Font.BOLD, FONT_SIZE);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int sh = fm.getAscent() + fm.getDescent() + fm.getLeading();
		// draw the score
		String score = "Score: " + highScoreMgr.format(state.getScore());
		int scoreX = ((app.getScreenWidth() - fm.stringWidth(score)) / 2);
		int scoreY = sh;
		g.drawString(score, scoreX, scoreY);
		// draw the name
		int nameX = ((app.getScreenWidth() - fm.stringWidth(name)) / 2);
		int nameY = scoreY + sh;
		g.drawString(name, nameX, nameY);
		// lets center the whole thing...
		int xStart = (app.getScreenWidth() - (maxWidth * colCount)) / 2;
		int x = xStart;
		int y = (app.getScreenHeight() - (fm.getAscent() * rowCount)) / 2;
		int count = 0;
		for (int i = 0; i < letters.length; ++i) {
			GameLetter letter = letters[i];
			boolean selected = i == getSelectedIndex(selectedRow, selectedCol);
			g.setColor(selected ? Color.RED : Color.GREEN);
			int xp = x + (maxWidth - letter.width) / 2;
			g.drawString(letter.letter, xp, y);
			if (selected) {
				g.drawRect(x, y - fm.getAscent(), maxWidth, sh);
			}
			x += maxWidth;
			count++;
			if (count % colCount == 0) {
				y += sh;
				x = xStart;
			}
		}
	}

	private void updateHighScoreRecord(String letter) {
		if (letter.equalsIgnoreCase("end")) {
			highScoreMgr.addNewScore(new Score(name, state.getScore()));
			finished = true;
		} else if (letter.equalsIgnoreCase("del")) {
			if (!name.isEmpty()) {
				name = name.substring(0, name.length() - 1);
			}
		} else {
			name += letter;
		}
	}

	private int roll(int value, int min, int max) {
		if (value < min)
			return max;
		if (value > max)
			return min;
		return value;
	}

	private int getSelectedIndex(int row, int col) {
		return row * colCount + col;
	}

	@Override
	protected AttractState getState() {
		return new HighScore();
	}

	@Override
	protected boolean shouldChangeState() {
		return finished;
	}
}