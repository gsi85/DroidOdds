package com.sisa.droidodds.service;

import android.app.Notification;

import com.sisa.droidodds.R;
import com.sisa.droidodds.layout.OddsOverlayView;

public class OddsOverlayService extends OverlayService {

	public static OddsOverlayService instance;
	private OddsOverlayView overlayView;

	public static void stop() {
		if (instance != null) {
			instance.stopSelf();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		overlayView = new OddsOverlayView(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (overlayView != null) {
			overlayView.destory();
		}

	}

	@Override
	protected Notification foregroundNotification(final int notificationId) {
		Notification notification;

		notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText(getString(R.string.notification_message))
				.build();

		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_ONLY_ALERT_ONCE;

		return notification;
	}
}
