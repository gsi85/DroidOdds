package com.sisa.droidodds.calculator;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.EnumMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Ordering;
import com.google.inject.Singleton;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.domain.hand.Hand;

/**
 * Class for evaluating the best hand of given cards.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class HandEvaluator {

	private static final int EXPECTED_NUMBER_OF_CARDS_IN_HAND = 5;

	private final Ordering<Entry<Rank>> byCountThenRank;

	/**
	 * DI constructor.
	 */
	public HandEvaluator() {
		byCountThenRank = new CountThenRankOrdering();
	}

	/**
	 * Determines the best {@link Hand} of given {@link Card} list.
	 * 
	 * @param cardsInHand
	 *            the {@link Card} list to be evaluated
	 * @return the highest ranked {@link Hand} possible for given list
	 * @throws {@link IllegalArgumentException} if not exactly 5 cards are evaluated
	 */
	public EvaluatedHand evaluateHand(final List<Card> cardsInHand) {
		Validate.isTrue(cardsInHand.size() == EXPECTED_NUMBER_OF_CARDS_IN_HAND);

		Hand hand;
		final Set<Suit> suits = EnumSet.noneOf(Suit.class);
		final Multiset<Rank> ranks = EnumMultiset.create(Rank.class);
		final LinkedList<Rank> ascendingSortedDistinctRanks = new LinkedList<>();

		preProcessCardsInHand(cardsInHand, suits, ranks);
		sortRanks(ranks, ascendingSortedDistinctRanks);

		final Rank first = ascendingSortedDistinctRanks.getFirst();
		final int distinctCount = ascendingSortedDistinctRanks.size();
		if (distinctCount == 5) {
			hand = evaluateFiveDistinctCount(suits, ascendingSortedDistinctRanks, first);
		} else if (distinctCount == 4) {
			hand = Hand.ONE_PAIR;
		} else if (distinctCount == 3) {
			hand = evaluateThreeDistinctCount(ranks, first);
		} else {
			hand = evaluateTwoDistinctCount(ranks, first);
		}

		return new EvaluatedHand(hand, ascendingSortedDistinctRanks);

	}

	private void preProcessCardsInHand(final List<Card> cardsInHand, final Set<Suit> suits, final Multiset<Rank> ranks) {
		for (final Card card : cardsInHand) {
			suits.add(card.getSuit());
			ranks.add(card.getRank());
		}
	}

	private void sortRanks(final Multiset<Rank> ranks, final LinkedList<Rank> ascendingSortedDistinctRanks) {
		for (final Entry<Rank> entry : byCountThenRank.immutableSortedCopy(ranks.entrySet())) {
			ascendingSortedDistinctRanks.addFirst(entry.getElement());
		}
	}

	private Hand evaluateFiveDistinctCount(final Set<Suit> suits, final LinkedList<Rank> ascendingSortedDistinctRanks, final Rank first) {
		Hand hand;
		final boolean flush = suits.size() == 1;
		if (first.ordinal() - ascendingSortedDistinctRanks.getLast().ordinal() == 4) {
			hand = flush ? Hand.STRAIGHT_FLUSH : Hand.STRAIGHT;
			if (hand == Hand.STRAIGHT_FLUSH && first == Rank.ACE) {
				hand = Hand.ROYAL_FLUSH;
			}
		} else if (first == Rank.ACE && ascendingSortedDistinctRanks.get(1) == Rank.FIVE) {
			hand = flush ? Hand.STRAIGHT_FLUSH : Hand.STRAIGHT;
			ascendingSortedDistinctRanks.addLast(ascendingSortedDistinctRanks.removeFirst());
		} else {
			hand = flush ? Hand.FLUSH : Hand.HIGH_CARD;
		}
		return hand;
	}

	private Hand evaluateThreeDistinctCount(final Multiset<Rank> ranks, final Rank first) {
		return ranks.count(first) == 2 ? Hand.TWO_PAIR : Hand.THREE_OF_A_KIND;
	}

	private Hand evaluateTwoDistinctCount(final Multiset<Rank> ranks, final Rank first) {
		return ranks.count(first) == 3 ? Hand.FULL_HOUSE : Hand.FOUR_OF_A_KIND;
	}

	private class CountThenRankOrdering extends Ordering<Entry<Rank>> {

		@Override
		public int compare(@Nullable final Entry<Rank> rankBag1, @Nullable final Entry<Rank> rankBag2) {
			return rankBag1.getCount() > rankBag2.getCount() ? 1 : rankBag1.getCount() < rankBag2.getCount() ? -1 : rankBag1.getElement()
					.getValue() > rankBag2.getElement().getValue() ? 1 : rankBag1.getElement().getValue() > rankBag2.getElement()
					.getValue() ? -1 : 0;
		}
	}

}
