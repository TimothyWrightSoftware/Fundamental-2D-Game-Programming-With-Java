package javagames.threads;

import java.util.*;

/*
 * This will simulate some fake hardware.
 */
public class FakeHardware {
	
	private static final int SLEEP_MIN = 100;
	private static final int SLEEP_MAX = 500;

	public enum FakeHardwareEvent {
		START, 
		STOP, 
		ON, 
		OFF;
	}

	private volatile boolean on = false;
	private volatile boolean running = false;
	private String name;
	private List<FakeHardwareListener> listeners = 
		Collections.synchronizedList(new ArrayList<FakeHardwareListener>());

	public FakeHardware(String name) {
		this.name = name;
	}

	public boolean addListener(FakeHardwareListener listener) {
		return listeners.add(listener);
	}

	public boolean isOn() {
		return on;
	}

	public boolean isRunning() {
		return running;
	}

	private void sleep() {
		int rand = new Random().nextInt(SLEEP_MAX - SLEEP_MIN + 1);
		sleep(rand + SLEEP_MIN);
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
		}
	}

	public void turnOn() {
		new Thread(new Runnable() {
			public void run() {
				sleep();
				setOn();
			}
		}).start();
	}

	public void turnOff() {
		new Thread(new Runnable() {
			public void run() {
				sleep();
				setOff();
			}
		}).start();
	}

	public void start(final int timeMS, final int slices) {
		new Thread(new Runnable() {
			public void run() {
				sleep();
				setStart(timeMS, slices);
			}
		}).start();
	}

	public void stop() {
		new Thread(new Runnable() {
			public void run() {
				sleep();
				setStop();
			}
		}).start();
	}

	private synchronized void setOn() {
		if (!on) {
			on = true;
			fireEvent(FakeHardwareEvent.ON);
		}
	}

	private synchronized void setOff() {
		if (on) {
			setStop();
			on = false;
			fireEvent(FakeHardwareEvent.OFF);
		}
	}

	/*
	 * There is a problem with this method. If the lock running is set to false
	 * after the lock is released but before the next if statement, the task
	 * will never run...
	 * 
	 * Let's pretend this Hardware driver doesn't work well, even though that
	 * NEVER happens :)
	 */
	private void setStart(int timeMS, int slices) {
		synchronized (this) {
			if (on && !running) {
				running = true;
				fireEvent(FakeHardwareEvent.START);
			}
		}
		if (running) {
			runTask(timeMS, slices);
			running = false;
			fireEvent(FakeHardwareEvent.STOP);
		}
	}

	private synchronized void setStop() {
		if (running) {
			running = false;
			// don't send the event
			// not actually done yet :)
		}
	}

	private void runTask(int timeMS, int slices) {
		int sleep = timeMS / slices;
		for (int i = 0; i < slices; ++i) {
			if (!running) {
				return;
			}
			System.out.println(name + "[" + (i + 1) + "/" + slices + "]");
			sleep(sleep);
		}
	}

	private void fireEvent(FakeHardwareEvent event) {
		synchronized (listeners) {
			for (FakeHardwareListener listener : listeners) {
				listener.event(this, event);
			}
		}
	}
}