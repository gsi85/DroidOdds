package com.sisa.droidodds.evaluator;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

/**
 * Module test for {@link SevenCardEvaluator}.
 * 
 * @author Laszlo Sisa
 * 
 */
public class SevenCardEvaluatorTest {

	private SevenCardEvaluator underTest;

	@Before
	public void init() {
		underTest = new SevenCardEvaluator();
	}

	@Test
	public void testEvaluate() throws ExecutionException {
		underTest.evaluateBestHand(Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS), new Card(Rank.TEN,
				Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(
				Rank.ACE, Suit.CLUBS)));
	}

}
