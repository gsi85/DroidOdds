package com.sisa.droidodds.calculator;

import java.text.DecimalFormat;
import java.util.List;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.DroidHenImageRecognizer;

/**
 * 
 * TODO: anotate classes with ContextSingleton / Singleton
 * 
 * @author Laca
 * 
 */
public class OddsCalculatorFacade {

	@Inject
	private DroidHenImageRecognizer imageRecognizer;
	@Inject
	private OddsCalculatorService oddsCalculatorService;

	public OddsCalculatorFacade() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
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
