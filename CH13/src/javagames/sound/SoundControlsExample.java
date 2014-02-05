package javagames.sound;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.*;
import javagames.util.*;

public class SoundControlsExample extends SimpleFramework {
	
	private BlockingClip clip;
	private LoopEvent loopClip;
	private BlockingDataLine dataLine;
	private LoopEvent loopStream;
	private byte[] rawSound;

	public SoundControlsExample() {
		appWidth = 340;
		appHeight = 400;
		appSleep = 10L;
		appTitle = "Sound Controls Example";
		appBackground = Color.WHITE;
		appFPSColor = Color.BLACK;
	}

	@Override
	protected void initialize() {
		super.initialize();
		InputStream in = ResourceLoader.load(SoundControlsExample.class,
				"./res/assets/sound/ELECTRONIC_computer_beep_09.wav", "asdf");
		rawSound = readBytes(in);
		clip = new BlockingClip(rawSound);
		loopClip = new LoopEvent(clip);
		loopClip.initialize();
		dataLine = new BlockingDataLine(rawSound);
		loopStream = new LoopEvent(dataLine);
		loopStream.initialize();
	}

	private byte[] readBytes(InputStream in) {
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			while ((read = buf.read()) != -1) {
				out.write(read);
			}
			in.close();
			return out.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void shutDownClips() {
		if (loopClip != null)
			loopClip.shutDown();
		if (loopStream != null)
			loopStream.shutDown();
	}

	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_1)) {
			loopClip.fire();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_2)) {
			loopClip.done();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_3)) {
			loopStream.fire();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_4)) {
			loopStream.done();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_W)) {
			increaseGain(clip);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_S)) {
			decreaseGain(clip);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_A)) {
			panLeft(clip);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_D)) {
			panRight(clip);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_I)) {
			increaseGain(dataLine);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_K)) {
			decreaseGain(dataLine);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_J)) {
			panLeft(dataLine);
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_L)) {
			panRight(dataLine);
		}
	}

	private void increaseGain(AudioStream audio) {
		float current = audio.getGain();
		if (current < 10.0f) {
			audio.setGain(current + 3.0f);
		}
	}

	private void decreaseGain(AudioStream audio) {
		float current = audio.getGain();
		if (current > -20.0f) {
			audio.setGain(current - 3.0f);
		}
	}

	private void panLeft(AudioStream audio) {
		float current = audio.getPan();
		float precision = audio.getPrecision();
		audio.setPan(current - precision * 10.0f);
	}

	private void panRight(AudioStream audio) {
		float current = audio.getPan();
		float precision = audio.getPrecision();
		audio.setPan(current + precision * 10.0f);
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		textPos = Utility.drawString(g, 20, textPos, "",
				"Clip Gain: " + clip.getGain(), "Clip Pan: " + clip.getPan(),
				"", "Stream Gain: " + dataLine.getGain(), "Stream Pan: "
						+ dataLine.getPan(), "", "(1) Start Loop (clip)",
				"(2) Stop Loop (clip)", "(3) Start Loop (stream)",
				"(4) Stop Loop (stream)", "", "(W) Raise Gain (clip)",
				"(S) Lower Gain (clip)", "(A) Pan Left (clip)",
				"(D) Pan Right (clip)", "", "(I) Raise Gain (stream)",
				"(K) Lower Gain (stream)", "(J) Pan Left (stream)",
				"(L) Pan Right (stream)");
	}

	@Override
	protected void terminate() {
		super.terminate();
		shutDownClips();
	}

	public static void main(String[] args) {
		launchApp(new SoundControlsExample());
	}
}