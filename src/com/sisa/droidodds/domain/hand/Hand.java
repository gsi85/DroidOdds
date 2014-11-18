package com.sisa.droidodds.domain.hand;

/**
 * Enumeration of all possible hands in game.
 * 
 * @author Laszlo Sisa
 * 
 */
public enum Hand {
	HIGH_CARD(1, "High card"), ONE_PAIR(2, "One pair"), TWO_PAIR(3, "Two pair"), THREE_OF_A_KIND(4, "Three of a king"), STRAIGHT(5,
			"Straight"), FLUSH(6, "Flush"), FULL_HOUSE(7, "Full house"), FOUR_OF_A_KIND(8, "Four of a kind"), STRAIGHT_FLUSH(9,
			"Straight flush"), ROYAL_FLUSH(10, "Royal flush");

	private final int value;
	private final String name;

	private Hand(final int value, final String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}
