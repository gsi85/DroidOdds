package com.sisa.droidodds.calculator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.domain.hand.Hand;

/**
 * Unit test for {@link HandEvaluator}.
 * 
 * @author Laszlo Sisa
 * 
 */
@RunWith(Parameterized.class)
public class HandEvaluatorTest {

	private HandEvaluator underTest;
	private final List<Card> cardsInHand;
	private final Hand expectedHand;

	public HandEvaluatorTest(final List<Card> cardsInHand, final Hand expectedHand) {
		this.cardsInHand = cardsInHand;
		this.expectedHand = expectedHand;
	}

	@Before
	public void init() {
		underTest = new HandEvaluator();
	}

	@Test
	public void testEvaluteWhenCalledShouldEveluateHand() {
		final EvaluatedHand hand = underTest.evaluateHand(cardsInHand);
		Assert.assertEquals(expectedHand, hand.getHand());
	}

	@Parameterized.Parameters
	public static Collection<Object[]> provideCardsInHandAndExpectedHand() {
		return Arrays.asList(new Object[][] {
				{
						Arrays.asList(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS),
								new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS)), Hand.ROYAL_FLUSH },
				{
						Arrays.asList(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.DEUCE, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.DIAMONDS), new Card(Rank.THREE, Suit.CLUBS)), Hand.STRAIGHT } });
	}

}
