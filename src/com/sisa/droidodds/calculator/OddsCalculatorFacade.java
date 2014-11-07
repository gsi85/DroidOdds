package com.sisa.droidodds.calculator;

import java.text.DecimalFormat;
import java.util.List;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.DroidHenImageRecognizer;
import com.sisa.droidodds.image.ImageRecognizer;

public class OddsCalculatorFacade {

	private final ImageRecognizer imageRecognizer;
	private final OddsCalculatorService oddsCalculatorService;

	public OddsCalculatorFacade() {
		imageRecognizer = new DroidHenImageRecognizer();
		oddsCalculatorService = new OddsCalculatorService();
	}

	public String getOdds(final String currentTetxt) {
		final long start = System.nanoTime();
		final List<Card> recognizedCards = imageRecognizer.recognizeLatestScreenshot();
		final long stop = System.nanoTime();
		final long duration = stop - start;

		final double seconds = duration / 1000000000.0;
		final DecimalFormat df = new DecimalFormat("0.00##");

		if (recognizedCards.size() != 0) {
			return recognizedCards.toString() + "\n Took: " + df.format(seconds) + " sec(s)";
		} else {
			return (currentTetxt);
		}

		// return oddsCalculatorService.getOdds(imageRecognizer.recognizeLatestScreenshot());
	}

}
