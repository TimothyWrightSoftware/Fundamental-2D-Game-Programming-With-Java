package javagames.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class SafeKeyboardInput implements KeyListener {
	
	enum EventType {
		PRESSED, RELEASED, TYPED;
	}

	class Event {
		KeyEvent event;
		EventType type;

		public Event(KeyEvent event, EventType type) {
			this.event = event;
			this.type = type;
		}
	}

	private LinkedList<Event> eventThread = new LinkedList<Event>();
	private LinkedList<Event> gameThread = new LinkedList<Event>();
	private Event event = null;
	private int[] polled;

	public SafeKeyboardInput() {
		polled = new int[256];
	}

	public boolean keyDown(int keyCode) {
		return keyCode == event.event.getKeyCode() && polled[keyCode] > 0;
	}

	public boolean keyDownOnce(int keyCode) {
		return keyCode == event.event.getKeyCode() && polled[keyCode] == 1;
	}

	public boolean processEvent() {
		event = gameThread.poll();
		if (event != null) {
			int keyCode = event.event.getKeyCode();
			if (keyCode >= 0 && keyCode < polled.length) {
				if (event.type == EventType.PRESSED) {
					polled[keyCode]++;
				} else if (event.type == EventType.RELEASED) {
					polled[keyCode] = 0;
				}
			}
		}
		return event != null;
	}

	public Character getKeyTyped() {
		if (event.type != EventType.TYPED) {
			return null;
		} else {
			return event.event.getKeyChar();
		}
	}

	public synchronized void poll() {
		LinkedList<Event> swap = eventThread;
		eventThread = gameThread;
		gameThread = swap;
	}

	public synchronized void keyPressed(KeyEvent e) {
		eventThread.add(new Event(e, EventType.PRESSED));
	}

	public synchronized void keyReleased(KeyEvent e) {
		eventThread.add(new Event(e, EventType.RELEASED));
	}

	public synchronized void keyTyped(KeyEvent e) {
		eventThread.add(new Event(e, EventType.TYPED));
	}
}