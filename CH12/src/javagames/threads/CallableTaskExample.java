package javagames.threads;

import java.util.Random;
import java.util.concurrent.*;

public class CallableTaskExample implements Callable<Boolean> {
	
	@Override
	public Boolean call() throws Exception {
		// simulate some stupid long task and maybe fail...
		Random rand = new Random();
		int seconds = rand.nextInt(6);
		if (seconds == 0) {
			// pretend there was an error
			throw new RuntimeException("I love the new thread stuff!!! :)");
		}
		try {
			Thread.sleep(seconds * 100);
		} catch (InterruptedException e) {
		}
		// even = true, odd = false
		return seconds % 2 == 0;
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		try {
			for (int i = 0; i < 50; ++i) {
				try {
					Future<Boolean> result = exec.submit(new CallableTaskExample());
					Boolean success = result.get();
					System.out.println("Result: " + success);
				} catch (ExecutionException ex) {
					Throwable throwable = ex.getCause();
					System.out.println("Error: " + throwable.getMessage());
				} catch (InterruptedException e) {
					System.out.println("Awesome! Thread was canceled");
					e.printStackTrace();
				}
			}
		} finally {
			try {
				exec.shutdown();
				exec.awaitTermination(10, TimeUnit.SECONDS);
				System.out.println("Threadpool Shutdown :)");
			} catch (InterruptedException e) {
				// at this point, just give up...
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}