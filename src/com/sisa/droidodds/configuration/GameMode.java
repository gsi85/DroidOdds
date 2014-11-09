package com.sisa.droidodds.configuration;

/**
 * Enumeration of supported poker applications.
 * 
 * @author Laszlo Sisa
 * 
 */
public enum GameMode {
	DROIDHEN("droidhen");

	private final String name;

	private GameMode(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
