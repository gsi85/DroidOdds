package com.sisa.droidodds.evaluator;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.measurement.StopWatch;

public class RecognizedCardEvaluatorTest {

	private RecognizedCardEvaluator underTest;
	private StopWatch stopWatch;

	@Before
	public void init() {
		underTest = new RecognizedCardEvaluator();
		stopWatch = new StopWatch();
	}

	@Test
	public void testEvaluateRecognizedCardOdds() {
		stopWatch.start();
		underTest.evaluateRecognizedCardOdds(Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS), new Card(
				Rank.SEVEN, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES)));
		stopWatch.stop();
		System.out.println(stopWatch.elapsedInMiliSeconds());
	}
}
