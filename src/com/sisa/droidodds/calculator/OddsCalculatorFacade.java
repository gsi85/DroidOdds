package com.sisa.droidodds.calculator;

import java.text.DecimalFormat;
import java.util.List;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.ImageRecognizerFacade;

@Singleton
public class OddsCalculatorFacade {

	@Inject
	private ImageRecognizerFacade imageRecognizerFacade;
	@Inject
	private OddsCalculatorService oddsCalculatorService;

	public OddsCalculatorFacade() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	public String getOdds(final String currentTetxt) {
		final long start = System.nanoTime();
		final List<Card> recognizedCards = imageRecognizerFacade.recognizeLatestScreenshot();
		final long stop = System.nanoTime();
		final long duration = stop - start;
		final double seconds = duration / 1000000000.0;

		if (recognizedCards.size() != 0) {
			return buildResultStrgin(recognizedCards, seconds);
		} else {
			return (currentTetxt);
		}

		// return oddsCalculatorService.getOdds(imageRecognizer.recognizeLatestScreenshot());
	}

	private String buildResultStrgin(final List<Card> recognizedCards, final double seconds) {
		final DecimalFormat df = new DecimalFormat("0.00##");

		final StringBuilder resultStringBulider = new StringBuilder();
		for (final Card card : recognizedCards) {
			resultStringBulider.append(card.getSuit().getAbbreviatedName()).append(card.getRank().getAbbreviatedName());
			resultStringBulider.append(" ");
		}

		return resultStringBulider.toString() + "\n Took: " + df.format(seconds) + " sec(s)";
	}

}
