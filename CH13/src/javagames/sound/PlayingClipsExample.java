package javagames.sound;

import java.io.*;
import javagames.util.ResourceLoader;
import javax.sound.sampled.*;

public class PlayingClipsExample implements LineListener {
	
	private volatile boolean open = false;
	private volatile boolean started = false;

	public byte[] readBytes(InputStream in) {
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

	public void runTestWithoutWaiting() throws Exception {
		System.out.println("runTestWithoutWaiting()");
		Clip clip = AudioSystem.getClip();
		clip.addLineListener(this);
		InputStream resource = ResourceLoader.load(PlayingClipsExample.class,
				"res/assets/sound/WEAPON_scifi_fire_02.wav", "notneeded");
		byte[] rawBytes = readBytes(resource);
		ByteArrayInputStream in = new ByteArrayInputStream(rawBytes);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
		clip.open(audioInputStream);
		for (int i = 0; i < 10; ++i) {
			clip.start();
			while (!clip.isActive()) {
				Thread.sleep(100);
			}
			clip.stop();
			clip.flush();
			clip.setFramePosition(0);
			clip.start();
			clip.drain();
		}
		clip.close();
	}

	public void runTestWithWaiting() throws Exception {
		System.out.println("runTestWithWaiting()");
		Clip clip = AudioSystem.getClip();
		clip.addLineListener(this);
		InputStream resource = ResourceLoader.load(PlayingClipsExample.class,
				"res/assets/sound/WEAPON_scifi_fire_02.wav", "notneeded");
		byte[] rawBytes = readBytes(resource);
		ByteArrayInputStream in = new ByteArrayInputStream(rawBytes);
		in = new ByteArrayInputStream(rawBytes);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
		synchronized (this) {
			clip.open(audioInputStream);
			while (!open) {
				wait();
			}
		}
		for (int i = 0; i < 10; ++i) {
			clip.setFramePosition(0);
			synchronized (this) {
				clip.start();
				while (!started) {
					wait();
				}
			}
			clip.drain();
			synchronized (this) {
				clip.stop();
				while (started) {
					wait();
				}
			}
		}
		synchronized (this) {
			clip.close();
			while (open) {
				wait();
			}
		}
	}

	@Override
	public synchronized void update(LineEvent lineEvent) {
		System.out.println("Got Event: " + lineEvent.getType());
		LineEvent.Type type = lineEvent.getType();
		if (type == LineEvent.Type.OPEN) {
			open = true;
		} else if (type == LineEvent.Type.START) {
			started = true;
		} else if (type == LineEvent.Type.STOP) {
			started = false;
		} else if (type == LineEvent.Type.CLOSE) {
			open = false;
		}
		notifyAll();
	}

	public static void main(String[] args) throws Exception {
		PlayingClipsExample lineListenerExample = new PlayingClipsExample();
		lineListenerExample.runTestWithWaiting();
		lineListenerExample.runTestWithoutWaiting();
	}
}