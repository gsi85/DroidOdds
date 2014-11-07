package com.sisa.droidodds.image.recognizer;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

/**
 * 
 * TODO: yet another big TODO: change all harcoded values with values from congigruation
 * 
 * 
 */
public class CardOcr {

	// TODO: configuration source
	private static final int ACCEPTED_RANK_MATCH_RATE_TRESHOLD = 95;
	private static final int ACCEPTED_SUIT_MATCH_RATE_TRESHOLD = 99;

	private final ImageMatcher imageMatcher;

	public CardOcr() {
		this.imageMatcher = new ImageMatcher();
	}

	public Card recognizeImage(final Bitmap image) {
		Card recognizedCard = null;
		final Suit suit = imageMatcher.recognizeCard(cutSuitFromImage(image), getSuitSampleImageMap(), ACCEPTED_SUIT_MATCH_RATE_TRESHOLD);
		if (suit != null) {
			final Rank rank = imageMatcher.recognizeCard(cutRankFromImage(image), getRankSampleImageMap(),
					ACCEPTED_RANK_MATCH_RATE_TRESHOLD);
			if (rank != null) {
				recognizedCard = new Card(rank, suit);
			}
		}
		return recognizedCard;
	}

	// TODO Move out to configuration source
	private Map<Suit, Bitmap> getSuitSampleImageMap() {
		final Map<Suit, Bitmap> suitSampleImageMap = new HashMap<>();
		for (final Suit suit : Suit.values()) {
			final String resourceString = String.format("card_1280_720_%s", suit.getName());
			final int resourceId = DroidOddsApplication.getAppContext().getResources()
					.getIdentifier(resourceString, "drawable", "com.sisa.droidodds");
			final Bitmap sample = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(DroidOddsApplication.getAppContext().getResources(), resourceId), 12, 22, false);
			suitSampleImageMap.put(suit, sample);
		}
		return suitSampleImageMap;
	}

	// TODO Move out to configuration source
	private Map<Rank, Bitmap> getRankSampleImageMap() {
		final Map<Rank, Bitmap> suitSampleImageMap = new HashMap<>();
		for (final Rank rank : Rank.values()) {
			final String resourceString = String.format("card_1280_720_%s", Integer.toString(rank.getValue()));
			final int resourceId = DroidOddsApplication.getAppContext().getResources()
					.getIdentifier(resourceString, "drawable", "com.sisa.droidodds");
			final Bitmap sample = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(DroidOddsApplication.getAppContext().getResources(), resourceId), 23, 37, false);
			suitSampleImageMap.put(rank, sample);
		}
		return suitSampleImageMap;
	}

	private Bitmap cutSuitFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, 0, 38, 12, 22);
	}

	private Bitmap cutRankFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, 0, 0, 23, 37);
	}

}
