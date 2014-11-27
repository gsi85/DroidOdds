package com.sisa.droidodds.evaluator;

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
 * Unit test for {@link FiveCardEvaluator}.
 * 
 * @author Laszlo Sisa
 * 
 */
@RunWith(Parameterized.class)
public class FiveCardEvaluatorTest {

	private FiveCardEvaluator underTest;
	private final List<Card> cardsInHand;
	private final Hand expectedHand;

	public FiveCardEvaluatorTest(final List<Card> cardsInHand, final Hand expectedHand) {
		this.cardsInHand = cardsInHand;
		this.expectedHand = expectedHand;
	}

	@Before
	public void init() {
		underTest = new FiveCardEvaluator();
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
						Arrays.asList(new Card(Rank.DEUCE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS),
								new Card(Rank.THREE, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.HIGH_CARD },
				{
						Arrays.asList(new Card(Rank.DEUCE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.ONE_PAIR },
				{
						Arrays.asList(new Card(Rank.DEUCE, Suit.SPADES), new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.TWO_PAIR },
				{
						Arrays.asList(new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.THREE_OF_A_KIND },
				{
						Arrays.asList(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.DEUCE, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.DIAMONDS), new Card(Rank.THREE, Suit.CLUBS)), Hand.STRAIGHT },
				{
						Arrays.asList(new Card(Rank.DEUCE, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS),
								new Card(Rank.THREE, Suit.CLUBS), new Card(Rank.FOUR, Suit.CLUBS)), Hand.FLUSH },
				{
						Arrays.asList(new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.FIVE, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.FULL_HOUSE },
				{
						Arrays.asList(new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.FIVE, Suit.HEARTS),
								new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.DIAMONDS)), Hand.FOUR_OF_A_KIND },
				{
						Arrays.asList(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS), new Card(Rank.DEUCE, Suit.CLUBS),
								new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.FOUR, Suit.CLUBS)), Hand.STRAIGHT_FLUSH },
				{
						Arrays.asList(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS),
								new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS)), Hand.ROYAL_FLUSH } });
	}

}
