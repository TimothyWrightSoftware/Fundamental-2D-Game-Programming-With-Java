package javagames.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RestartEvent implements Runnable {
	
	private enum State {
		WAITING, 
		RESTART, 
		RUNNING;
	};

	private enum Event {
		FIRE, 
		DONE;
	}

	private BlockingQueue<Event> queue;
	private BlockingHardware hardware;
	private State currentState;
	private Thread consumer;
	private int ms;
	private int slices;

	public RestartEvent(int ms, int slices) {
		this.ms = ms;
		this.slices = slices;
	}

	public void initialize() {
		hardware = new BlockingHardware("restart");
		hardware.addListener(getListener());
		queue = new LinkedBlockingQueue<Event>();
		currentState = State.WAITING; // default state
		// start up the consumer thread
		consumer = new Thread(this);
		consumer.start();
	}

	public void fire() {
		try {
			queue.put(Event.FIRE);
		} catch (InterruptedException e) {
		}
	}

	public void shutDown() {
		Thread temp = consumer;
		consumer = null;
		try {
			// send event to wake up consumer
			// and/or stop.
			queue.put(Event.DONE);
			temp.join(10000L);
			System.out.println("Restart shutdown!!!");
		} catch (InterruptedException ex) {
		}
	}

	@Override
	public void run() {
		while (Thread.currentThread() == consumer) {
			try {
				processEvent(queue.take());
			} catch (InterruptedException e) {
			}
		}
	}

	private void processEvent(Event event) throws InterruptedException {
		System.out.println("Got " + event + " event");
		if (currentState == State.WAITING) {
			if (event == Event.FIRE) {
				hardware.turnOn();
				hardware.start(ms, slices);
				currentState = State.RUNNING;
			}
		} else if (currentState == State.RUNNING) {
			if (event == Event.FIRE) {
				hardware.stop();
				currentState = State.RESTART;
			}
			if (event == Event.DONE) {
				hardware.turnOff();
				currentState = State.WAITING;
			}
		} else if (currentState == State.RESTART) {
			if (event == Event.DONE) {
				hardware.start(ms, slices);
				currentState = State.RUNNING;
			}
		}
	}

	private BlockingHardwareListener getListener() {
		return new BlockingHardwareListener() {
			@Override
			public void taskFinished() {
				try {
					queue.put(Event.DONE);
				} catch (InterruptedException e) {
				}
			}
		};
	}
}