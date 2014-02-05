package javagames.sound;

public class LoopEvent extends SoundEvent {
	
	public static final String STATE_WAITING = "waiting";
	public static final String STATE_RUNNING = "running";
	public static final String EVENT_FIRE = "fire";
	public static final String EVENT_DONE = "done";
	private String currentState;

	public LoopEvent(AudioStream audio) {
		super(audio);
		currentState = STATE_WAITING;
	}

	public void fire() {
		put(EVENT_FIRE);
	}

	public void done() {
		put(EVENT_DONE);
	}

	protected void processEvent(String event) throws InterruptedException {
		System.out.println("Got " + event + " event");
		if (currentState == STATE_WAITING) {
			if (event == EVENT_FIRE) {
				audio.open();
				audio.loop(AudioStream.LOOP_CONTINUOUSLY);
				currentState = STATE_RUNNING;
			}
		} else if (currentState == STATE_RUNNING) {
			if (event == EVENT_DONE) {
				audio.stop();
				audio.close();
				currentState = STATE_WAITING;
			}
		}
	}
}