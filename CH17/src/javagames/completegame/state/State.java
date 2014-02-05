package javagames.completegame.state;

import java.awt.Graphics2D;
import javagames.completegame.CompleteGame;
import javagames.util.Matrix3x3f;

public class State {
	
	protected StateController controller;
	protected CompleteGame app;

	public void setController(StateController controller) {
		this.controller = controller;
		app = (CompleteGame) controller.getAttribute("app");
	}

	protected StateController getController() {
		return controller;
	}

	public void enter() {
		
	}

	public void processInput(float delta) {
	}

	public void updateObjects(float delta) {
	}

	public void render(Graphics2D g, Matrix3x3f view) {
	}

	public void exit() {
		
	}
}