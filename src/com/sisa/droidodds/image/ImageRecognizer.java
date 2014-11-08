package com.sisa.droidodds.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.loader.LatestScreenshotProvider;
import com.sisa.droidodds.image.recognizer.DeckRecognizer;
import com.sisa.droidodds.image.recognizer.HandRecognizer;

@Singleton
public class ImageRecognizer {

	@Inject
	private LatestScreenshotProvider latestScreenshotProvider;
	@Inject
	private HandRecognizer handRecognizer;
	@Inject
	private DeckRecognizer deckRecognizer;

	public ImageRecognizer() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	public List<Card> recognizeLatestScreenshot() {
		List<Card> cardsInHand = new ArrayList<>();
		cardsInHand = recognizeCardsInHand(cardsInHand);
		return cardsInHand;
	}

	private List<Card> recognizeCardsInHand(List<Card> cardsInHand) {
		final File latestScreenshotFile = latestScreenshotProvider.getLatestScreenshotFile();
		if (latestScreenshotFile != null) {
			final Bitmap latestScreenshot = BitmapFactory.decodeFile(latestScreenshotFile.getPath());
			cardsInHand = handRecognizer.recognizeHand(latestScreenshot, cardsInHand);
			cardsInHand = deckRecognizer.recognizeDeck(latestScreenshot, cardsInHand);
			latestScreenshotFile.delete();
		}
		return cardsInHand;
	}

}
