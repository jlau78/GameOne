package com.king.server;

import java.nio.channels.IllegalSelectorException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Thread pool that last the lifetime of the application i.e. will not shutdown.
 * To avoid expensive instantiation of the ThreadPool at every invocation this
 * thread pool persists and is ready to process submits.
 * 
 * TODO: implement execute(Runnable)
 */
public class LifetimeThreadExcutorService<V> {

	Logger LOGGER = Logger.getLogger(LifetimeThreadExcutorService.class.getName());

	public static ExecutorService executorService;

	@SuppressWarnings("rawtypes")
	public static LifetimeThreadExcutorService lifetimeThreadExecutorService;

	private static final int THREAD_POOL_COUNT = 4;
	private static final int TERMINATE_WAIT_SECONDS = 5;

	private LifetimeThreadExcutorService() {
		initializeThreadPool();
	}

	@SuppressWarnings({ "rawtypes" })
	public static synchronized LifetimeThreadExcutorService getInstance() {
		if (lifetimeThreadExecutorService == null) {
			lifetimeThreadExecutorService = new LifetimeThreadExcutorService();
		}
		return lifetimeThreadExecutorService;
	}

	public boolean submit(final List<Callable<V>> callables, final long futureTimeout) {
		boolean success = true;
		try {
			executorService.invokeAll(callables).stream().map(future -> {
				try {
					return future.get(futureTimeout, TimeUnit.SECONDS);

				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Threads failed to complete");
					throw new IllegalSelectorException();
				}
			});
			;

		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Threads interrupted. Failed to complete");
		}

		return success;
	}

	public boolean execute(final List<Runnable> runnables) {
		LOGGER.log(Level.INFO,
				LifetimeThreadExcutorService.class.getName() + ".execute(List<Runnable> not implemented");
		return true;
	}

	/**
	 * Exposed public to allow the ThreadPool to be restarted if it was ever
	 * shutdown
	 */
	public void initializeThreadPool() {
		if (null == executorService) {
			executorService = Executors.newCachedThreadPool();
		}
	}

	/**
	 * Expose shutdown call to executor
	 */
	public void shutdown() {

		if (null != executorService) {
			try {
				executorService.shutdown();
				executorService.awaitTermination(TERMINATE_WAIT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE,
						"ExecutorService fail to shutdown gracefully in 5 seconds. Will finally shutdownNow.");
			} finally {
				executorService.shutdownNow();
				executorService = null;
			}

			LOGGER.log(Level.INFO, this.getClass().getName() + " threadpool has been shutdown.");
		}
	}

	/**
	 * Get the singleton executor
	 */
	public ExecutorService getExecutor() {
		return executorService;
	}

}
