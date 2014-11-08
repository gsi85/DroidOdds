package com.sisa.droidodds.image.recognizer;

import java.util.List;

import roboguice.RoboGuice;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.transformer.BlackAndWhiteImageTransformer;

/**
 * 
 * TODO: yet another big TODO: change all harcoded values with values from congigruation
 * 
 */
@Singleton
public class DeckRecognizer {

	private static final int EXPECTED_SIZE_AFTER_HANDS_RECOGNIZED = 2;
	private static final int EXPECTED_SIZE_AFTER_FIRST_CARD_RECOGNIZED = 3;
	private static final int EXPECTED_SIZE_AFTER_TURN_CARD_RECOGNIZED = 6;

	private static final int WHITE = Color.WHITE;

	@Inject
	private BlackAndWhiteImageTransformer blackAndWhiteImageTransformer;
	@Inject
	private CardOcr cardOcr;

	public DeckRecognizer() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	public List<Card> recognizeDeck(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		recognizeFirstCardInFlop(latestScreenshot, cardsInHand);
		return cardsInHand;
	}

	private void recognizeFirstCardInFlop(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isHandRecognized(cardsInHand)) {
			final Bitmap firstCard = blackAndWhiteImageTransformer.transformImage(cutFirstCardInFlop(latestScreenshot));
			final Card recognizedCard = cardOcr.recognizeImage(firstCard);
			if (recognizedCard != null) {
				cardsInHand.add(recognizedCard);
				recognizeRestOfFlop(latestScreenshot, cardsInHand);
			}
		}
	}

	private void recognizeRestOfFlop(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isFlopOnDeck(cardsInHand)) {
			final Bitmap secondCard = blackAndWhiteImageTransformer.transformImage(cutSecondCardInFlop(latestScreenshot));
			final Bitmap thirdCard = blackAndWhiteImageTransformer.transformImage(cutThirdCardInFlop(latestScreenshot));
			final Card recognizedSecondCard = cardOcr.recognizeImage(secondCard);
			if (recognizedSecondCard != null) {
				cardsInHand.add(recognizedSecondCard);
				final Card recognizedThirdCard = cardOcr.recognizeImage(thirdCard);
				if (recognizedThirdCard != null) {
					cardsInHand.add(recognizedThirdCard);
					recognizeTurn(latestScreenshot, cardsInHand);
				}
			}
		}
	}

	private void recognizeTurn(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		final Bitmap turn = blackAndWhiteImageTransformer.transformImage(cutTurn(latestScreenshot));
		final Card recognizedTurn = cardOcr.recognizeImage(turn);
		if (recognizedTurn != null) {
			cardsInHand.add(recognizedTurn);
			recognizeRiver(latestScreenshot, cardsInHand);
		}
	}

	private void recognizeRiver(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isTurnRecognized(cardsInHand)) {
			final Bitmap river = blackAndWhiteImageTransformer.transformImage(cutRiver(latestScreenshot));
			final Card recognizedTurn = cardOcr.recognizeImage(river);
			if (recognizedTurn != null)
				cardsInHand.add(recognizedTurn);
		}
	}

	private Bitmap cutFirstCardInFlop(final Bitmap latestScreenshot) {
		return trimEdges(Bitmap.createBitmap(latestScreenshot, 414, 281, 23, 60));
	}

	private Bitmap cutSecondCardInFlop(final Bitmap latestScreenshot) {
		return trimEdges(Bitmap.createBitmap(latestScreenshot, 498, 281, 23, 60));
	}

	private Bitmap cutThirdCardInFlop(final Bitmap latestScreenshot) {
		return trimEdges(Bitmap.createBitmap(latestScreenshot, 582, 281, 23, 60));
	}

	private Bitmap cutTurn(final Bitmap latestScreenshot) {
		return trimEdges(Bitmap.createBitmap(latestScreenshot, 666, 281, 23, 60));
	}

	private Bitmap cutRiver(final Bitmap latestScreenshot) {
		return trimEdges(Bitmap.createBitmap(latestScreenshot, 750, 281, 23, 60));
	}

	// TODO: could be extracted to util class
	private Bitmap trimEdges(final Bitmap image) {
		final Canvas canvas = new Canvas(image);
		final Paint paint = new Paint();
		final int width = image.getWidth();
		final int height = image.getHeight();
		paint.setColor(WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(0, 0, 2, height, paint);
		canvas.drawRect(width - 2, 0, width, height, paint);
		canvas.drawRect(0, 0, width, 2, paint);
		return image;
	}

	private boolean isHandRecognized(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_HANDS_RECOGNIZED;
	}

	private boolean isFlopOnDeck(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_FIRST_CARD_RECOGNIZED;
	}

	private boolean isTurnRecognized(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_TURN_CARD_RECOGNIZED;
	}
}
