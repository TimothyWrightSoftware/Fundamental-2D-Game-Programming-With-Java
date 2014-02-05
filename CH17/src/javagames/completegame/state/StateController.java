package javagames.completegame.state;

import java.awt.*;
import java.util.*;
import javagames.util.Matrix3x3f;

public class StateController {
	
	private Map<String, Object> attributes;
	private State currentState;

	public StateController() {
		attributes = Collections.synchronizedMap(new HashMap<String, Object>());
	}

	public void setState(State newState) {
		if (currentState != null) {
			currentState.exit();
		}
		if (newState != null) {
			newState.setController(this);
			newState.enter();
		}
		currentState = newState;
	}

	public void processInput(float delta) {
		currentState.processInput(delta);
	}

	public void updateObjects(float delta) {
		currentState.updateObjects(delta);
	}

	public void render(Graphics2D g, Matrix3x3f view) {
		g.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
		);
		currentState.render(g, view);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public void setAttribute(String name, Object attribute) {
		attributes.put(name, attribute);
	}

	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}
}