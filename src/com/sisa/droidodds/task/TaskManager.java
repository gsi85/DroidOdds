package com.sisa.droidodds.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roboguice.RoboGuice;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.shared.LatestRecognizedCards;
import com.sisa.droidodds.ui.layout.OddsOverlayView;

@Singleton
public class TaskManager {

	private final ExecutorService threadPool;
	@Inject
	private OddsCalculatorTaskRunner oddsCalculatorTaskRunner;
	@Inject
	private OddsViewUpdateTaskRunner oddsViewUpdateTaskRunner;
	@Inject
	private ScreenshotRecognizerTaskRunner screenshotRecognizerTaskRunner;
	@Inject
	private LatestRecognizedCards latestRecognizedCards;

	public TaskManager() {
		threadPool = Executors.newCachedThreadPool();
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	public void startTasks(final OddsOverlayView oddsOverlayView) {
		Log.i("AsyncTasks", "Starting background tasks...");
		oddsCalculatorTaskRunner.executeOnExecutor(threadPool, "");
		oddsViewUpdateTaskRunner.executeOnExecutor(threadPool, oddsOverlayView);
		screenshotRecognizerTaskRunner.executeOnExecutor(threadPool, "");
	}

	public void stopTasks() {
		Log.i("AsyncTasks", "Stopping background tasks...");
		resetLatestRecognizedCards();
		oddsCalculatorTaskRunner.cancel(true);
		oddsViewUpdateTaskRunner.cancel(true);
		screenshotRecognizerTaskRunner.cancel(true);
	}

	private void resetLatestRecognizedCards() {
		latestRecognizedCards.setRecognizedCards(null);
		latestRecognizedCards.setCardsUpdated(true);
	}
}
