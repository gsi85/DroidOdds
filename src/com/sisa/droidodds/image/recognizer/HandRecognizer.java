package com.sisa.droidodds.image.recognizer;

import java.util.List;

import roboguice.RoboGuice;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.transformer.BlackAndWhiteImageTransformer;
import com.sisa.droidodds.image.transformer.ImageCutter;

/**
 * Recognizer for the cards in player's hand.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class HandRecognizer {

	private static final String RIGHT_HAND_ROTATE = "OCR_IMAGE_RIGHT_HAND_ROTATE";
	private static final String LEFT_HAND_ROTATE = "OCR_IMAGE_LEFT_HAND_ROTATE";
	private static final String HAND_RIGHT_Y = "OCR_IMAGE_HAND_RIGHT_Y";
	private static final String HAND_RIGHT_X = "OCR_IMAGE_HAND_RIGHT_X";
	private static final String HAND_LEFT_Y = "OCR_IMAGE_HAND_LEFT_Y";
	private static final String HAND_LEFT_X = "OCR_IMAGE_HAND_LEFT_X";
	private static final String HAND_AREA_HEIGHT = "OCR_IMAGE_HAND_AREA_HEIGHT";
	private static final String HAND_AREA_WIDTH = "OCR_IMAGE_HAND_AREA_WIDTH";
	private static final String HAND_AREA_Y = "OCR_IMAGE_HAND_AREA_Y";
	private static final String HAND_AREA_X = "OCR_IMAGE_HAND_AREA_X";
	private static final String CORNER_TRIM_WIDTH = "OCR_IMAGE_CORNER_TRIM_WIDTH";
	private static final String CARD_HEIGHT = "OCR_IMAGE_CARD_HEIGHT";
	private static final String CARD_WIDTH = "OCR_IMAGE_CARD_WIDTH";
	private static final String EDGE_TRIM_WIDTH = "OCR_IMAGE_EDGE_TRIM_WIDTH";

	@Inject
	private BlackAndWhiteImageTransformer blackAndWhiteImageTransformer;
	@Inject
	private CardOcr cardOcr;
	@Inject
	private ImageCutter imageCutter;
	@Inject
	private ConfigurationSource configurationSource;

	/**
	 * DI constructor.
	 */
	public HandRecognizer() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Recognizes all cards on in player's hand.
	 * 
	 * @param latestScreenshot
	 *            the {@link Bitmap} to be recognized
	 * @param cardsInHand
	 *            the initialized, empty list in which the recognized {@link Card}s are colected
	 * @return the lit of recognized {@link Card}
	 */
	public List<Card> recognizeHand(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		final Bitmap handArea = cutHandArea(latestScreenshot);
		final Bitmap leftHand = blackAndWhiteImageTransformer.transformImage(getTrimmedLeftHand(handArea));
		final Bitmap rightHand = blackAndWhiteImageTransformer.transformImage(getTrimmedRightHand(handArea));

		final Card recognizedFirstCard = cardOcr.recognizeImage(leftHand);
		if (recognizedFirstCard != null) {
			cardsInHand.add(recognizedFirstCard);
			final Card recognizedSecondCard = cardOcr.recognizeImage(rightHand);
			if (recognizedSecondCard != null)
				cardsInHand.add(recognizedSecondCard);
		}

		return cardsInHand;
	}

	private Bitmap cutHandArea(final Bitmap latestScreenshot) {
		return Bitmap.createBitmap(latestScreenshot, getInt(HAND_AREA_X), getInt(HAND_AREA_Y), getInt(HAND_AREA_WIDTH),
				getInt(HAND_AREA_HEIGHT));
	}

	private Bitmap getTrimmedRightHand(final Bitmap handArea) {
		Bitmap rightHand = alignRightHand(handArea);
		rightHand = imageCutter.trimCorners(rightHand, getInt(CORNER_TRIM_WIDTH));
		return imageCutter.trimEdges(rightHand, getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap getTrimmedLeftHand(final Bitmap handArea) {
		Bitmap lefthand = alignLeftHand(handArea);
		lefthand = imageCutter.trimCorners(lefthand, getInt(CORNER_TRIM_WIDTH));
		return imageCutter.trimEdges(lefthand, getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap alignLeftHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, getInt(LEFT_HAND_ROTATE)), getInt(HAND_LEFT_X), getInt(HAND_LEFT_Y),
				getInt(CARD_WIDTH), getInt(CARD_HEIGHT));
	}

	private Bitmap alignRightHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, getInt(RIGHT_HAND_ROTATE)), getInt(HAND_RIGHT_X), getInt(HAND_RIGHT_Y),
				getInt(CARD_WIDTH), getInt(CARD_HEIGHT));
	}

	private Bitmap rotateImage(final Bitmap handArea, final int angel) {
		final Matrix matrix = new Matrix();
		matrix.preRotate(angel);
		final Bitmap leftHand = Bitmap.createBitmap(handArea, 0, 0, handArea.getWidth(), handArea.getHeight(), matrix, true);
		return leftHand;
	}

	private int getInt(final String configurationKey) {
		return configurationSource.getInt(configurationKey);
	}

}
