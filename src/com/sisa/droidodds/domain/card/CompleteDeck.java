package com.sisa.droidodds.domain.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton domain object representing a complete deck of French cards.
 * 
 * @author Laszlo Sisa
 * 
 */
@SuppressWarnings("serial")
public class CompleteDeck {

	private static final List<Card> completeDeck;

	static {
		completeDeck = new ArrayList<Card>() {
			{
				for (final Rank rank : Rank.values()) {
					for (final Suit suit : Suit.values()) {
						add(new Card(rank, suit));
					}
				}
			}
		};

	}

	private CompleteDeck() {

	}

	public static List<Card> getCompleteDeck() {
		return Collections.unmodifiableList(completeDeck);
	}

}
