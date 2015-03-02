package com.sisa.droidodds.task;

import roboguice.RoboGuice;
import android.os.AsyncTask;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.calculator.CompositeKnownCardsOddsCalculator;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.shared.DisplayedOdds;
import com.sisa.droidodds.shared.LatestRecognizedCards;

@Singleton
public class OddsCalculatorTaskRunner extends AsyncTask<String, DisplayedOdds, Odds> {

	@Inject
	private CompositeKnownCardsOddsCalculator oddsCalculator;
	@Inject
	private LatestRecognizedCards latestRecognizedCards;

	public OddsCalculatorTaskRunner() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	@Override
	protected Odds doInBackground(final String... values) {
		Log.i("AsyncTasks", "Odds Calculator Task started");
		while (!isCancelled()) {
			if (latestRecognizedCards.isCardsUpdated()) {
				latestRecognizedCards.setCardsUpdated(false);
				if (latestRecognizedCards.getRecognizedCards() != null) {
					oddsCalculator.evaluateRecognizedCardOdds(latestRecognizedCards.getRecognizedCards());
				}
			}
		}
		Log.i("AsyncTasks", "Odds Calculator Task stopped");
		return null;
	}
}
