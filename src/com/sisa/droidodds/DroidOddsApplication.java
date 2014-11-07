package com.sisa.droidodds;

import java.util.Date;

import android.app.Application;
import android.content.Context;

public class DroidOddsApplication extends Application {

	public static final Date APPLICATION_START_UP_DATE = new Date();

	// TODO remove this
	public static int scrneeshotCounter;

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		DroidOddsApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return DroidOddsApplication.context;
	}

}
