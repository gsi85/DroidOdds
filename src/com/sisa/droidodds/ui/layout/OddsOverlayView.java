package com.sisa.droidodds.ui.layout;

import roboguice.RoboGuice;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.inject.Inject;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.R;
import com.sisa.droidodds.configuration.ConfigurationPreloader;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.configuration.GameMode;
import com.sisa.droidodds.service.OverlayService;
import com.sisa.droidodds.task.TaskManager;

public class OddsOverlayView extends OverlayView {

	private final OverlayService service;
	private TextView oddsTextView;

	@Inject
	private ConfigurationSource configurationSource;
	@Inject
	private ConfigurationPreloader configurationPreloader;
	@Inject
	private TaskManager taskManager;

	public OddsOverlayView(final OverlayService service) {
		super(service, R.layout.odds_activity, 1);
		this.service = service;
		RoboGuice.injectMembers(service, this);

		// TODO: should this be moved to elsewhere?
		final WindowManager windowManager = (WindowManager) DroidOddsApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		configurationSource.loadConfiguration(GameMode.DROIDHEN, size.x, size.y);
		configurationPreloader.preloadOcrConfigurationValues();
		taskManager.startTasks(this);
	}

	@Override
	public int getGravity() {
		return Gravity.TOP + Gravity.RIGHT;
	}

	@Override
	protected void onInflateView() {
		oddsTextView = (TextView) this.findViewById(R.id.odds_display_text);
		oddsTextView.setWillNotCacheDrawing(true);
	}

	@Override
	protected void onTouchEvent_Press(final MotionEvent event) {
		taskManager.stopTasks();
		service.stopSelf();
	}

	public synchronized TextView getOddsTextView() {
		return oddsTextView;
	}

}
