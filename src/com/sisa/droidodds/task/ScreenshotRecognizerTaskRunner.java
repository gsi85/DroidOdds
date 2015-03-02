package com.sisa.droidodds.task;

import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;
import android.os.AsyncTask;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.ocr.ImageRecognizerFacade;
import com.sisa.droidodds.shared.LatestRecognizedCards;

@Singleton
public class ScreenshotRecognizerTaskRunner extends AsyncTask<String, List<Card>, List<Card>> {

	private static final int NUMBER_OF_MINIMUM_KNOWN_CARDS = 5;
	@Inject
	private LatestRecognizedCards latestRecognizedCards;
	@Inject
	private ImageRecognizerFacade imageRecognizerFacade;

	public ScreenshotRecognizerTaskRunner() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	@Override
	protected List<Card> doInBackground(final String... params) {
		Log.i("AsyncTasks", "Screenshot Recognizer Task started");

		final List<Card> testList = (Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS), new Card(Rank.SEVEN,
				Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES)));
		latestRecognizedCards.setRecognizedCards(testList);
		latestRecognizedCards.setCardsUpdated(true);

		while (!isCancelled()) {
			updateLatestRecognizedCards(imageRecognizerFacade.recognizeLatestScreenshot());
			haltThread();
		}

		Log.i("AsyncTasks", "Screenshot Recognizer Task stopped");
		return null;
	}

	private void updateLatestRecognizedCards(final List<Card> recognizeLatestScreenshot) {
		if (recognizeLatestScreenshot != null && recognizeLatestScreenshot.size() >= NUMBER_OF_MINIMUM_KNOWN_CARDS) {
			latestRecognizedCards.setRecognizedCards(recognizeLatestScreenshot);
			latestRecognizedCards.setCardsUpdated(true);
		}
	}

	private void haltThread() {
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
