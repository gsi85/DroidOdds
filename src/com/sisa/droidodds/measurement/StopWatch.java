package com.sisa.droidodds.measurement;

import java.math.BigDecimal;

/**
 * Stop watch class to measure elapsed time in nanoseconds.
 * 
 * @author Laszlo Sisa
 * 
 */
public class StopWatch {

	private static final Long ZERO = Long.valueOf(0);
	private static final BigDecimal MILLION = BigDecimal.valueOf(1000000);

	private long startTime;
	private long stopTime;

	private boolean running;

	/**
	 * DI constructor
	 */
	public StopWatch() {
		startTime = ZERO;
		stopTime = ZERO;
	}

	/**
	 * Starts the stop watch.
	 * 
	 * @throws {@link IllegalStateException} if called when stop watch already running.
	 * 
	 */
	public void start() {
		if (!running) {
			running = true;
			stopTime = ZERO;
			startTime = System.nanoTime();
		} else {
			throw new IllegalStateException("Stop watch already running");
		}
	}

	/**
	 * Stop the stop watch.
	 * 
	 * @throws {@link IllegalStateException} if called when stop watch isn't running.
	 * 
	 */
	public void stop() {
		if (running) {
			stopTime = System.nanoTime();
			running = false;
		} else {
			throw new IllegalStateException("Stop watch hasn't been started");
		}
	}

	/**
	 * Read elapsed time between start and stop calls in nanoseconds.
	 * 
	 * @return the elapsed time in nanoseconds
	 * @throws {@link IllegalStateException} if called when stop watch hasn't been started or still running
	 */
	public long elapsedInNanoSeconds() {
		validateFinishedState();
		return stopTime - startTime;
	}

	/**
	 * Read elapsed time between start and stop calls in milliseconds.
	 * 
	 * @return the elapsed time in milliseconds
	 * @throws {@link IllegalStateException} if called when stop watch hasn't been started or still running
	 */
	public long elapsedInMiliSeconds() {
		validateFinishedState();
		final BigDecimal elapsedTime = BigDecimal.valueOf(stopTime - startTime);
		return elapsedTime.divide(MILLION).longValue();
	}

	private void validateFinishedState() {
		if (running) {
			throw new IllegalStateException("Stop watch is still running");
		}
		if (stopTime == ZERO) {
			throw new IllegalStateException("Stop watch hasn't been started");
		}
	}
}
