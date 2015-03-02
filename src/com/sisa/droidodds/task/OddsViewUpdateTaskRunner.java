package com.sisa.droidodds.task;

import roboguice.RoboGuice;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.shared.DisplayedOdds;
import com.sisa.droidodds.ui.layout.OddsOverlayView;

@Singleton
public class OddsViewUpdateTaskRunner extends AsyncTask<OddsOverlayView, OddsOverlayView, String> {

	@Inject
	private DisplayedOdds displayedOdds;

	public OddsViewUpdateTaskRunner() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	@Override
	protected String doInBackground(final OddsOverlayView... overlayView) {
		Log.i("AsyncTasks", "Odds View Update Task started");

		while (!isCancelled()) {
			publishProgress(overlayView);
			haltThread();
		}

		Log.i("AsyncTasks", "Odds View Update Task stopped");
		return null;
	}

	@Override
	protected void onProgressUpdate(final OddsOverlayView... overlayViews) {
		if (displayedOdds.getOdds() != null) {
			overlayViews[0].setVisibility(View.GONE);
			overlayViews[0].getOddsTextView().setVisibility(View.GONE);
			overlayViews[0].getOddsTextView().setText(displayedOdds.getOdds().toString());
			overlayViews[0].setVisibility(View.VISIBLE);
			overlayViews[0].getOddsTextView().setVisibility(View.VISIBLE);
		}
	}

	private void haltThread() {
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
