package com.sisa.droidodds.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.loader.LatestScreenshotProvider;
import com.sisa.droidodds.image.recognizer.DeckRecognizer;
import com.sisa.droidodds.image.recognizer.HandRecognizer;

public class DroidHenImageRecognizer implements ImageRecognizer {

	private final LatestScreenshotProvider latestScreenshotProvider;
	private final HandRecognizer handRecognizer;
	private final DeckRecognizer deckRecognizer;

	public DroidHenImageRecognizer() {
		this.latestScreenshotProvider = new LatestScreenshotProvider();
		this.handRecognizer = new HandRecognizer();
		this.deckRecognizer = new DeckRecognizer();
	}

	@Override
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
