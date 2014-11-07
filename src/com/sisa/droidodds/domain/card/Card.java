package com.sisa.droidodds.domain.card;

import org.apache.commons.lang3.Validate;

/**
 * Domain objects describing a playing card.
 * 
 * @author Laca
 * 
 */
public class Card implements Comparable<Card> {

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

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Card other = (Card) obj;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	@Override
	public int compareTo(final Card another) {
		return this.rank.compareTo(another.rank);
	}

	@Override
	public String toString() {
		return String.format("%s_of_%s", rank, suit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}
}