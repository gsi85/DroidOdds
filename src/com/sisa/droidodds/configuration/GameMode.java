package com.sisa.droidodds.configuration;

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
