package javagames.threads;

import javagames.threads.FakeHardware.FakeHardwareEvent;

public interface FakeHardwareListener {
	
	public void event(FakeHardware source, FakeHardwareEvent event);
	
}