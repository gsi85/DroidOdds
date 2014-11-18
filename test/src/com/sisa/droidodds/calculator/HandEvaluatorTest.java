package com.sisa.droidodds.calculator;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.domain.hand.Hand;

/**
 * Unit test for {@link HandEvaluator}.
 * 
 * @author Laszlo Sisa
 * 
 */
public class HandEvaluatorTest {

	private static HandEvaluator underTest;

	@BeforeClass
	public static void init() {
		underTest = new HandEvaluator();
	}

	@Test
	public void testEvaluateHandWhenCalledShouldReturnStraight() {
		final Hand hand = underTest.evaluateHand(Arrays.asList(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS), new Card(
				Rank.QUEEN, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.KING, Suit.DIAMONDS)));
		Assert.assertEquals(Hand.STRAIGHT, hand);
	}

}
