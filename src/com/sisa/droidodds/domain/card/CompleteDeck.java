package com.sisa.droidodds.domain.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompleteDeck {

	private static final List<Card> completeDeck;

	static {
		completeDeck = new ArrayList<>();

		for (final Rank rank : Rank.values()) {
			for (final Suit suit : Suit.values()) {
				completeDeck.add(new Card(rank, suit));
			}
		}

	}

	private CompleteDeck() {

	}

	public static List<Card> getCompleteDeck() {
		return Collections.unmodifiableList(completeDeck);
	}

}
