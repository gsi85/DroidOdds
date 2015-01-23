package com.sisa.droidodds.calculator;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

public class SevenKnownCardsOddsCalculatorClassTest {

	private KnownCardsOddsCalculator underTest;

	@Before
	public void init() {
		underTest = new SevenKnownCardsOddsCalculator();
	}

	@Test
	public void test() {
		final Odds result = underTest.evaluateRecognizedCardOdds(Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE,
				Suit.CLUBS), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES),
				new Card(Rank.DEUCE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS)));

		Assert.assertEquals(990, result.getTotalDealCount());
	}

}
