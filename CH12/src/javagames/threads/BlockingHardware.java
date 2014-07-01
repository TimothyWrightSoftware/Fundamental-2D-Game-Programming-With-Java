package javagames.threads;

import java.util.*;
import java.util.concurrent.locks.*;
import javagames.threads.FakeHardware.FakeHardwareEvent;

public class BlockingHardware {
	
	private final Lock lock = new ReentrantLock();
	private final Condition cond = lock.newCondition();
	private volatile boolean on = false;
	private volatile boolean started = false;
	private FakeHardware hardware;
	private List<BlockingHardwareListener> listeners = 
		Collections.synchronizedList(new ArrayList<BlockingHardwareListener>());

	public BlockingHardware(String name) {
		hardware = new FakeHardware(name);
		hardware.addListener(new FakeHardwareListener() {
			@Override
			public void event(FakeHardware source, FakeHardwareEvent event) {
				handleHardwareEvent(source, event);
			}
		});
	}

	public boolean addListener(BlockingHardwareListener listener) {
		return listeners.add(listener);
	}

	public void start(int ms, int slices) {
		lock.lock();
		try {
			hardware.start(ms, slices);
			while (!started) {
				cond.await();
			}
			System.out.println("It's Started");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void stop() {
		lock.lock();
		try {
			hardware.stop();
			while (started) {
				cond.await();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void turnOn() {
		lock.lock();
		try {
			hardware.turnOn();
			while (!on) {
				cond.await();
			}
			System.out.println("Turned on");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void turnOff() {
		lock.lock();
		try {
			hardware.turnOff();
			while (on) {
				cond.await();
			}
			System.out.println("Turned off");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	protected void handleHardwareEvent(FakeHardware source,
			FakeHardwareEvent event) {
		boolean wasStarted = started;
		lock.lock();
		try {
			if (event == FakeHardwareEvent.ON) {
				on = true;
			} else if (event == FakeHardwareEvent.OFF) {
				on = false;
			} else if (event == FakeHardwareEvent.START) {
				started = true;
			} else if (event == FakeHardwareEvent.STOP) {
				started = false;
			}
			cond.signalAll();
		} finally {
			lock.unlock();
		}
		if (wasStarted && !started) {
			fireTaskFinished();
		}
	}

	private void fireTaskFinished() {
		synchronized (listeners) {
			for (BlockingHardwareListener listener : listeners) {
				listener.taskFinished();
			}
		}
	}
}