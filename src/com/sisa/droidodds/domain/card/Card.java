package com.sisa.droidodds.domain.card;

import org.apache.commons.lang3.Validate;

/**
 * Domain objects describing a playing card.
 * 
 * @author Laszlo Sisa
 * 
 */
public class Card {

	private final Rank rank;
	private final Suit suit;

	/**
	 * Default constructor of a playing card.
	 * 
	 * @param rank
	 *            the {@link Rank} of the card
	 * @param suit
	 *            the {@link Suit} of the card
	 * 
	 * @throws NullPointerException
	 *             if rank or suit is null
	 */
	public Card(final Rank rank, final Suit suit) {
		Validate.notNull(rank);
		Validate.notNull(suit);
		this.rank = rank;
		this.suit = suit;
	}

	public Rank getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public String getAbbreviatedName() {
		return String.format("%s%s", suit.getAbbreviatedName(), rank.getAbbreviatedName());
	}

}