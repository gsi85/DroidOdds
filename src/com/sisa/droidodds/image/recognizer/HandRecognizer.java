package com.sisa.droidodds.image.recognizer;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.transformer.BlackAndWhiteImageTransformer;

public class HandRecognizer {

	private static final int WHITE = Color.WHITE;

	private final BlackAndWhiteImageTransformer blackAndWhiteImageTransformer;
	private final CardOcr cardOcr;

	/***
	 * 
	 * TODO: A BIG BIG TODO: replace all hardcoded number with values from configuration
	 * 
	 */
	public HandRecognizer() {
		this.blackAndWhiteImageTransformer = new BlackAndWhiteImageTransformer();
		this.cardOcr = new CardOcr();
	}

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
		return Bitmap.createBitmap(latestScreenshot, 673, 504, 71, 64);
	}

	private Bitmap getTrimmedRightHand(final Bitmap handArea) {
		final Bitmap rightHand = cutAndRotateRightHand(handArea);
		trimCorners(rightHand);
		return trimEdges(rightHand);
	}

	private Bitmap getTrimmedLeftHand(final Bitmap handArea) {
		final Bitmap lefthand = cutAndRotateLeftHand(handArea);
		trimCorners(lefthand);
		return trimEdges(lefthand);
	}

	private Bitmap cutAndRotateLeftHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, 15), 11, 6, 23, 60);
	}

	private Bitmap cutAndRotateRightHand(final Bitmap handArea) {
		return Bitmap.createBitmap(rotateImage(handArea, -15), 51, 0, 23, 60);
	}

	private void trimCorners(final Bitmap handImage) {
		// TODO: consider whether drawing rectangle would perform better
		final int width = handImage.getWidth();
		final int height = handImage.getHeight();
		for (int y = 5; y >= 1; y--) {
			for (int x = 1; x <= y; x++) {
				handImage.setPixel(x - 1, 5 - y, WHITE);
				handImage.setPixel(width - x, 5 - y, WHITE);
				handImage.setPixel(x - 1, height - 6 + y, WHITE);
				handImage.setPixel(width - 6 + y, height - x, WHITE);
			}
		}
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

	private Bitmap rotateImage(final Bitmap handArea, final int angel) {
		final Matrix matrix = new Matrix();
		matrix.preRotate(angel);
		final Bitmap leftHand = Bitmap.createBitmap(handArea, 0, 0, handArea.getWidth(), handArea.getHeight(), matrix, true);
		return leftHand;
	}

}
