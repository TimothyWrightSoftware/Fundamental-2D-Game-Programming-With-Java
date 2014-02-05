package javagames.completegame;

import java.awt.*;
import javagames.completegame.admin.*;
import javagames.completegame.object.*;
import javagames.completegame.state.*;
import javagames.sound.*;
import javagames.util.*;

public class CompleteGame extends WindowFramework {
	
	private StateController controller;

	public CompleteGame() {
		
		appBorder = GameConstants.APP_BORDER;
		appWidth = GameConstants.APP_WIDTH;
		appHeight = GameConstants.APP_HEIGHT;
		appSleep = GameConstants.APP_SLEEP;
		appTitle = GameConstants.APP_TITLE;
		appWorldWidth = GameConstants.WORLD_WIDTH;
		appWorldHeight = GameConstants.WORLD_HEIGHT;
		appBorderScale = GameConstants.BORDER_SCALE;
		appDisableCursor = GameConstants.DISABLE_CURSOR;
		appMaintainRatio = GameConstants.MAINTAIN_RATIO;
		
	}

	@Override
	protected void initialize() {
		super.initialize();
		controller = new StateController();
		controller.setAttribute("app", this);
		controller.setAttribute("keys", keyboard);
		controller.setAttribute("ACME", new Acme(this));
		controller.setAttribute("wrapper", 
			new PolygonWrapper(appWorldWidth, appWorldHeight));
		controller.setAttribute("viewport", getViewportTransform());
		controller.setState(new GameLoading());
	}

	public void shutDownGame() {
		shutDown();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		controller.processInput(delta);
	}

	@Override
	protected void updateObjects(float delta) {
		controller.updateObjects(delta);
	}

	@Override
	protected void render(Graphics g) {
		controller.render((Graphics2D) g, getViewportTransform());
		super.render(g);
	}

	@Override
	protected void terminate() {
		super.terminate();
		QuickRestart event = 
			(QuickRestart) controller.getAttribute("fire-clip");
		if (event != null) {
			System.out.println("Sound: fire-clip");
			event.close();
			event.shutDown();
			System.out.println("Done: fire-clip");
		}
		LoopEvent loop = (LoopEvent) controller.getAttribute("ambience");
		if (loop != null) {
			System.out.println("Sound: ambience");
			loop.done();
			loop.shutDown();
			System.out.println("Done: ambience");
		}
		QuickRestart[] explosions = 
			(QuickRestart[]) controller.getAttribute("explosions");
		for (int i = 0; i < explosions.length; ++i) {
			System.out.println("Sound: explosions: " + i);
			explosions[i].close();
			explosions[i].shutDown();
			System.out.println("Done: explosions");
		}
		QuickLooper thruster = 
			(QuickLooper) controller.getAttribute("thruster-clip");
		if (thruster != null) {
			System.out.println("Sound: thruster-clip");
			thruster.close();
			thruster.shutDown();
			System.out.println("Done: thruster-clip");
		}
	}

	public static void main(String[] args) {
		launchApp(new CompleteGame());
	}
}