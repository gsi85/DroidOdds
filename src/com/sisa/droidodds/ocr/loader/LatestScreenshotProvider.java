package com.sisa.droidodds.ocr.loader;

import java.io.File;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;

@Singleton
public class LatestScreenshotProvider {

	public File getLatestScreenshotFile() {
		final File[] files = new File(Environment.getExternalStorageDirectory() + "/DH_Texas_Poker/screenshot/").listFiles();

		File imageToProcess = null;

		boolean found = false;
		for (int i = 0; i < files.length && !found; i++) {
			final Date creationDate = new Date(files[i].lastModified());
			if (creationDate.compareTo(DroidOddsApplication.APPLICATION_START_UP_DATE) > 0) {
				imageToProcess = files[i];
				found = true;
			}
		}

		// TODO implement this properly
		if (imageToProcess != null) {
			boolean canBeProcessed = false;
			while (!canBeProcessed) {
				try {
					Bitmap.createBitmap(BitmapFactory.decodeFile(imageToProcess.getPath()), 673, 504, 71, 64);
					canBeProcessed = true;
				} catch (final Exception e) {
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		return imageToProcess;
	}
}
