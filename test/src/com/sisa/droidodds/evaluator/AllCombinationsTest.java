package com.sisa.droidodds.evaluator;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

public class AllCombinationsTest {

	private AllCombinations underTest;

	@Before
	public void init() {
		underTest = new AllCombinations();
	}

	@Test
	public void test() {
		final long result = underTest.evaluateRecognizedCardOdds(Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE,
				Suit.CLUBS), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES)));

		Assert.assertEquals(178365, result);
	}
}
