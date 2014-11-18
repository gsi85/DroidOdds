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
import com.sisa.droidodds.domain.hand.Hand;

@Singleton
public class HandEvaluator {

	public Hand evaluateHand(final List<Card> cardsInHand) {
		Validate.isTrue(cardsInHand.size() == 5);
		Hand hand = Hand.HIGH_CARD;

		final Set<Suit> suits = EnumSet.noneOf(Suit.class);
		final Multiset<Rank> ranks = EnumMultiset.create(Rank.class);
		final LinkedList<Rank> distinctRanks = new LinkedList<>();

		for (final Card card : cardsInHand) {
			suits.add(card.getSuit());
			ranks.add(card.getRank());
		}

		for (final Entry<Rank> entry : byCountThenRank.immutableSortedCopy(ranks.entrySet())) {
			distinctRanks.addFirst(entry.getElement());
		}

		final Rank first = distinctRanks.getFirst();
		final int distinctCount = distinctRanks.size();
		if (distinctCount == 5) {
			final boolean flush = suits.size() == 1;
			if (first.ordinal() - distinctRanks.getLast().ordinal() == 4) {
				hand = flush ? Hand.STRAIGHT_FLUSH : Hand.STRAIGHT;
				// TODO: ADD ROYAL FLUSH EVALUATOR
			} else if (first == Rank.ACE && distinctRanks.get(1) == Rank.FIVE) {
				hand = flush ? Hand.STRAIGHT_FLUSH : Hand.STRAIGHT;
				// ace plays low, move to end
				distinctRanks.addLast(distinctRanks.removeFirst());
			} else {
				hand = flush ? Hand.FLUSH : Hand.HIGH_CARD;
			}
		} else if (distinctCount == 4) {
			hand = Hand.ONE_PAIR;
		} else if (distinctCount == 3) {
			hand = ranks.count(first) == 2 ? Hand.TWO_PAIR : Hand.THREE_OF_A_KIND;
		} else {
			hand = ranks.count(first) == 3 ? Hand.FULL_HOUSE : Hand.FOUR_OF_A_KIND;
		}

		return hand;

	}

	private static final Ordering<Entry<Rank>> byCountThenRank;

	static {
		byCountThenRank = new Ordering<Multiset.Entry<Rank>>() {

			@Override
			public int compare(@Nullable final Entry<Rank> arg0, @Nullable final Entry<Rank> arg1) {
				if (arg0.getCount() > arg1.getCount())
					return 1;
				else if (arg0.getCount() < arg1.getCount())
					return -1;
				else {
					if (arg0.getElement().getValue() > arg1.getElement().getValue())
						return 1;
					else if (arg0.getElement().getValue() < arg1.getElement().getValue())
						return -1;
					else
						return 0;
				}
			}
		};
	}

}
