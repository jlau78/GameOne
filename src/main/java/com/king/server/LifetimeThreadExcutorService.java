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

	/**
	 * Exposed public to allow the ThreadPool to be restarted if it was ever
	 * shutdown
	 */
	private void initializeThreadPool() {
		if (null == executorService) {
			executorService = Executors.newCachedThreadPool();
		}
	}

	/**
	 * Get the singleton executor
	 */
	public ExecutorService getExecutor() {
		return executorService;
	}

}
