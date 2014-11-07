package com.sisa.droidodds.service;

import roboguice.service.RoboService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;

public class OverlayService extends RoboService {

	protected boolean foreground = false;
	protected boolean cancelNotification = false;
	protected int id = 0;

	protected Notification foregroundNotification(final int notificationId) {
		return null;
	}

	public void moveToForeground(final int id, final boolean cancelNotification) {
		moveToForeground(id, foregroundNotification(id), cancelNotification);
	}

	public void moveToForeground(final int id, final Notification notification, final boolean cancelNotification) {
		if (!this.foreground && notification != null) {
			this.foreground = true;
			this.id = id;
			this.cancelNotification = cancelNotification;

			super.startForeground(id, notification);
		} else if (this.id != id && id > 0 && notification != null) {
			this.id = id;
			((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(id, notification);
		}
	}

	public void moveToBackground(int id, final boolean cancelNotification) {
		foreground = false;
		id = 0;
		super.stopForeground(cancelNotification);
	}

	public void moveToBackground(final int id) {
		moveToBackground(id, cancelNotification);
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

}
