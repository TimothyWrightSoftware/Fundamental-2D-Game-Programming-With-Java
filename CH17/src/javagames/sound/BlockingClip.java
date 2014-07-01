package javagames.sound;

import java.io.*;
import javax.sound.sampled.*;

public class BlockingClip extends AudioStream {
	
	private Clip clip;
	private boolean restart;

	public BlockingClip(byte[] soundData) {
		super(soundData);
	}

	/*
	 * This guy could throw a bunch of exceptions. We're going to wrap them all
	 * in a custom exception handler that is a RuntimeException so we don't have
	 * to catch and throw all these exceptions.
	 */
	@Override
	public void open() {
		lock.lock();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(soundData);
			AudioInputStream ais = AudioSystem.getAudioInputStream(in);
			clip = AudioSystem.getClip();
			clip.addLineListener(this);
			clip.open(ais);
			while( !open ) {
				cond.await();
			}
			//UPDATE
			createControls( clip );
			//UPDATE
			System.out.println( "open" );
		} catch (UnsupportedAudioFileException ex) {
			throw new SoundException(ex.getMessage(), ex);
		} catch (LineUnavailableException ex) {
			throw new SoundException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new SoundException(ex.getMessage(), ex);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void start() {
		lock.lock();
		try {
			clip.flush();
			clip.setFramePosition(0);
			clip.start();
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

	@Override
	public void loop(int count) {
		lock.lock();
		try {
			clip.flush();
			clip.setFramePosition(0);
			clip.loop(count);
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

	@Override
	public void restart() {
		restart = true;
		stop();
		restart = false;
		start();
	}

	@Override
	protected void fireTaskFinished() {
		if (!restart) {
			super.fireTaskFinished();
		}
	}

	@Override
	public void stop() {
		lock.lock();
		try {
			clip.stop();
			while (started) {
				cond.await();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
		lock.lock();
		try {
			clip.close();
			while (open) {
				cond.await();
			}
			clip = null;
			//UPDATE
			clearControls();
			//UPDATE
			System.out.println("Turned off");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}