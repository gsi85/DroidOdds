package com.sisa.droidodds.evaluator;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

public class CompleteDealEvaluatorTest {

	private CompleteDealEvaluator underTest;

	@Before
	public void init() {
		underTest = new CompleteDealEvaluator();
	}

	@Test
	public void testEvaluateShouldReturnSplitWhenCalled() {
		// GIVEN
		final List<Card> cardsInHand = Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS));
		final List<Card> communityCards = Arrays.asList(new Card(Rank.TEN, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS), new Card(
				Rank.QUEEN, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS));

		// WHEN
		final Odds odds = underTest.evaluateOdds(cardsInHand, communityCards);

		// THEN
		Assert.assertEquals(990, odds.getSplitCount());
	}

	@Test
	public void testEvaluateShouldReturn() {
		// GIVEN
		final List<Card> cardsInHand = Arrays.asList(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.DEUCE, Suit.SPADES));
		final List<Card> communityCards = Arrays.asList(new Card(Rank.THREE, Suit.DIAMONDS), new Card(Rank.JACK, Suit.HEARTS), new Card(
				Rank.QUEEN, Suit.CLUBS), new Card(Rank.NINE, Suit.SPADES), new Card(Rank.FIVE, Suit.HEARTS));

		// WHEN
		final Odds odds = underTest.evaluateOdds(cardsInHand, communityCards);

		// THEN
		Assert.assertEquals(700, odds.getWinCount());
	}

}
