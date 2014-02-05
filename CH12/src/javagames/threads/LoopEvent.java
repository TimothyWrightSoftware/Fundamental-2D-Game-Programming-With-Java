package javagames.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LoopEvent implements Runnable {
	
	private enum State {
		WAITING, 
		RUNNING;
	};

	private enum Event {
		FIRE, 
		RESTART,
		DONE;
	}

	private BlockingQueue<Event> queue;
	private BlockingHardware hardware;
	private State currentState;
	private Thread consumer;
	private int ms;
	private int slices;

	public LoopEvent(int ms, int slices) {
		this.ms = ms;
		this.slices = slices;
	}

	public void initialize() {
		hardware = new BlockingHardware("looper");
		hardware.addListener(getListener());
		queue = new LinkedBlockingQueue<Event>();
		currentState = State.WAITING; // default state
		// startup the consumer thread
		consumer = new Thread(this);
		consumer.start();
	}

	public void fire() {
		try {
			queue.put(Event.FIRE);
		} catch (InterruptedException e) {
		}
	}

	public void done() {
		try {
			queue.put(Event.DONE);
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
			System.out.println("Loop shutdown!!!");
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
			if (event == Event.RESTART) {
				hardware.start(ms, slices);
				currentState = State.RUNNING;
			}
			if (event == Event.DONE) {
				hardware.stop();
				hardware.turnOff();
				currentState = State.WAITING;
			}
		}
	}

	private BlockingHardwareListener getListener() {
		return new BlockingHardwareListener() {
			@Override
			public void taskFinished() {
				try {
					queue.put(Event.RESTART);
				} catch (InterruptedException e) {
				}
			}
		};
	}
}