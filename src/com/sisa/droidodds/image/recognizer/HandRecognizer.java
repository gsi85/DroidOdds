package com.sisa.droidodds.image.recognizer;

import java.util.List;

import org.apache.commons.lang3.Validate;

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

	private boolean configurationsLoaded;
	private int rightHandRotateAngel;
	private int leftHandRotateAngel;
	private int rightHandY;
	private int rightHandX;
	private int leftHandY;
	private int leftHandx;
	private int handAreaHight;
	private int handAreaWidth;
	private int handAreaY;
	private int handAreaX;
	private int cornerTrimWidth;
	private int cardHeight;
	private int cardWidth;
	private int edgeTrimWidth;

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
	 * @throws {@link IllegalArgumentException} if called before configuration have been loaded
	 */
	public List<Card> recognizeHand(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		Validate.isTrue(configurationsLoaded);
		recognizeFirstCard(latestScreenshot, cardsInHand);
		return cardsInHand;
	}

	private void recognizeFirstCard(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		final Bitmap handArea = cutHandArea(latestScreenshot);
		final Bitmap leftHand = blackAndWhiteImageTransformer.transformImage(getTrimmedLeftHand(handArea));
		final Card recognizedFirstCard = cardOcr.recognizeImage(leftHand);
		if (recognizedFirstCard != null) {
			cardsInHand.add(recognizedFirstCard);
			recognozieSecondCard(cardsInHand, handArea);
		}
	}

	private void recognozieSecondCard(final List<Card> cardsInHand, final Bitmap handArea) {
		final Bitmap rightHand = blackAndWhiteImageTransformer.transformImage(getTrimmedRightHand(handArea));
		final Card recognizedSecondCard = cardOcr.recognizeImage(rightHand);
		if (recognizedSecondCard != null)
			cardsInHand.add(recognizedSecondCard);
	}

	private Bitmap cutHandArea(final Bitmap latestScreenshot) {
		return Bitmap.createBitmap(latestScreenshot, handAreaX, handAreaY, handAreaWidth, handAreaHight);
	}

	private Bitmap getTrimmedRightHand(final Bitmap handArea) {
		Bitmap rightHand = alignRightHand(handArea);
		rightHand = imageCutter.trimCorners(rightHand, cornerTrimWidth);
		return imageCutter.trimEdges(rightHand, edgeTrimWidth);
	}

	private Bitmap getTrimmedLeftHand(final Bitmap handArea) {
		Bitmap lefthand = alignLeftHand(handArea);
		lefthand = imageCutter.trimCorners(lefthand, cornerTrimWidth);
		return imageCutter.trimEdges(lefthand, edgeTrimWidth);
	}

	private Bitmap alignLeftHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, leftHandRotateAngel), leftHandx, leftHandY, cardWidth, cardHeight);
	}

	private Bitmap alignRightHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, rightHandRotateAngel), rightHandX, rightHandY, cardWidth, cardHeight);
	}

	private Bitmap rotateImage(final Bitmap handArea, final int angel) {
		final Matrix matrix = new Matrix();
		matrix.preRotate(angel);
		final Bitmap leftHand = Bitmap.createBitmap(handArea, 0, 0, handArea.getWidth(), handArea.getHeight(), matrix, true);
		return leftHand;
	}

	/**
	 * Loads values used by this class from {@link ConfigurationSource}, this should be called before calling other methods.
	 */
	public void preLoadConfigurationSourceValues() {
		rightHandRotateAngel = configurationSource.getInt("OCR_IMAGE_RIGHT_HAND_ROTATE");
		leftHandRotateAngel = configurationSource.getInt("OCR_IMAGE_LEFT_HAND_ROTATE");
		rightHandY = configurationSource.getInt("OCR_IMAGE_HAND_RIGHT_Y");
		rightHandX = configurationSource.getInt("OCR_IMAGE_HAND_RIGHT_X");
		leftHandY = configurationSource.getInt("OCR_IMAGE_HAND_LEFT_Y");
		leftHandx = configurationSource.getInt("OCR_IMAGE_HAND_LEFT_X");
		handAreaHight = configurationSource.getInt("OCR_IMAGE_HAND_AREA_HEIGHT");
		handAreaWidth = configurationSource.getInt("OCR_IMAGE_HAND_AREA_WIDTH");
		handAreaY = configurationSource.getInt("OCR_IMAGE_HAND_AREA_Y");
		handAreaX = configurationSource.getInt("OCR_IMAGE_HAND_AREA_X");
		cornerTrimWidth = configurationSource.getInt("OCR_IMAGE_CORNER_TRIM_WIDTH");
		cardHeight = configurationSource.getInt("OCR_IMAGE_CARD_HEIGHT");
		cardWidth = configurationSource.getInt("OCR_IMAGE_CARD_WIDTH");
		edgeTrimWidth = configurationSource.getInt("OCR_IMAGE_EDGE_TRIM_WIDTH");
		configurationsLoaded = true;
	}

}
