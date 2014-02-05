package javagames.sound;

import java.util.*;
import java.util.concurrent.locks.*;
import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.*;

public abstract class AudioStream implements LineListener {
	
	public static final int LOOP_CONTINUOUSLY = -1;
	protected final Lock lock = new ReentrantLock();
	protected final Condition cond = lock.newCondition();
	protected volatile boolean open = false;
	protected volatile boolean started = false;
	// UPDATES
	protected FloatControl gainControl;
	protected FloatControl panControl;
	// UPDATES
	protected byte[] soundData;
	private List<BlockingAudioListener> listeners = Collections
			.synchronizedList(new ArrayList<BlockingAudioListener>());

	public AudioStream(byte[] soundData) {
		this.soundData = soundData;
	}

	public abstract void open();

	public abstract void close();

	public abstract void start();

	public abstract void loop(int count);

	public abstract void restart();

	public abstract void stop();

	public boolean addListener(BlockingAudioListener listener) {
		return listeners.add(listener);
	}

	protected void fireTaskFinished() {
		synchronized (listeners) {
			for (BlockingAudioListener listener : listeners) {
				listener.audioFinished();
			}
		}
	}

	@Override
	public void update(LineEvent event) {
		boolean wasStarted = started;
		lock.lock();
		try {
			if (event.getType() == Type.OPEN) {
				open = true;
			} else if (event.getType() == Type.CLOSE) {
				open = false;
			} else if (event.getType() == Type.START) {
				started = true;
			} else if (event.getType() == Type.STOP) {
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

	// UPDATES
	public void clearControls() {
		gainControl = null;
		panControl = null;
	}

	public void createControls(Line line) {
		if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			gainControl = (FloatControl) line
					.getControl(FloatControl.Type.MASTER_GAIN);
		}
		if (line.isControlSupported(FloatControl.Type.PAN)) {
			panControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
		}
	}

	public boolean hasGainControl() {
		return gainControl != null;
	}

	public void setGain(float fGain) {
		if (hasGainControl()) {
			gainControl.setValue(fGain);
		}
	}

	public float getGain() {
		return hasGainControl() ? gainControl.getValue() : 0.0f;
	}

	public float getMaximum() {
		return hasGainControl() ? gainControl.getMaximum() : 0.0f;
	}

	public float getMinimum() {
		return hasGainControl() ? gainControl.getMinimum() : 0.0f;
	}

	public boolean hasPanControl() {
		return panControl != null;
	}

	public float getPrecision() {
		return hasPanControl() ? panControl.getPrecision() : 0.0f;
	}

	public float getPan() {
		return hasPanControl() ? panControl.getValue() : 0.0f;
	}

	public void setPan(float pan) {
		if (hasPanControl()) {
			panControl.setValue(pan);
		}
	}
	// UPDATES
}