package com.sisa.droidodds.domain.card;

/**
 * Enumeration of available card suits.
 * 
 * @author Laca
 * 
 */
public enum Suit {
	SPADES("spades", "♠"), HEARTS("hearts", "♥"), DIAMONDS("diamonds", "♦"), CLUBS("clubs", "♣");

	private final String name;
	private final String abbreviatedName;

	private Suit(final String name, final String abbreviatedName) {
		this.name = name;
		this.abbreviatedName = abbreviatedName;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviatedName() {
		return abbreviatedName;
	}
}