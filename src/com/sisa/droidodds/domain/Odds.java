package com.sisa.droidodds.domain;

/**
 * Domain object representing gaming odds.
 * 
 * @author Laszlo Sisa
 * 
 */
public class Odds {

	private final int winCount;
	private final int splitCount;
	private final int totalDealCount;

	public Odds(final int winCount, final int splitCount, final int totalDealCount) {
		this.winCount = winCount;
		this.splitCount = splitCount;
		this.totalDealCount = totalDealCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public int getSplitCount() {
		return splitCount;
	}

	public int getTotalDealCount() {
		return totalDealCount;
	}

	@Override
	public String toString() {
		return String.format("Odds [winCount=%s, splitCount=%s, totalDealCount=%s]", winCount, splitCount, totalDealCount);
	}

}
