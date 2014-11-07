package com.sisa.droidodds.domain.card;

/**
 * Enumeration of available card suits.
 * 
 * @author Laca
 * 
 */
public enum Suit {
	SPADES("spades"), HEARTS("hearts"), DIAMONDS("diamonds"), CLUBS("clubs");

	private final String name;

	private Suit(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}