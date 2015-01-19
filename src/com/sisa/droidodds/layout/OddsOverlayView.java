package com.sisa.droidodds.layout;

import roboguice.RoboGuice;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.inject.Inject;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.R;
import com.sisa.droidodds.calculator.OddsCalculatorFacade;
import com.sisa.droidodds.configuration.ConfigurationPreloader;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.configuration.GameMode;
import com.sisa.droidodds.service.OverlayService;

public class OddsOverlayView extends OverlayView {

	@Inject
	private OddsCalculatorFacade odssCalculatorFacade;
	@Inject
	private ConfigurationSource configurationSource;
	@Inject
	private ConfigurationPreloader configurationPreloader;
	private TextView info;
	private Handler handler;

	public OddsOverlayView(final OverlayService service) {
		super(service, R.layout.odds_activity, 1);
		RoboGuice.injectMembers(service, this);

		// TODO: should this be moved to activty?
		final WindowManager windowManager = (WindowManager) DroidOddsApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		configurationSource.loadConfiguration(GameMode.DROIDHEN, size.x, size.y);
		configurationPreloader.preloadOcrConfigurationValues();
	}

	@Override
	public int getGravity() {
		return Gravity.TOP + Gravity.RIGHT;
	}

	@Override
	protected void onInflateView() {
		info = (TextView) this.findViewById(R.id.odds_display_text);
		startConinousOddsProviderService();
	}

	@Override
	protected void refreshViews() {
	}

	@Override
	protected void onTouchEvent_Press(final MotionEvent event) {
	}

	private void startConinousOddsProviderService() {
		handler = new Handler();
		handler.post(new ContinuousOddsProviderService());
	}

	private class ContinuousOddsProviderService implements Runnable {
		@Override
		public void run() {
			final String odds = odssCalculatorFacade.getOdds(info.getText().toString());
			if (!odds.equals(info.getText().toString())) {
				info.setText(odds);
				refresh();
			}
			// handler.postDelayed(this, 100);
		}
	}

}
