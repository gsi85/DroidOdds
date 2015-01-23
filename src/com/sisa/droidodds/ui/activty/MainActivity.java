package com.sisa.droidodds.ui.activty;

import roboguice.RoboGuice;
import android.content.Intent;
import android.os.Bundle;

import com.sisa.droidodds.R;
import com.sisa.droidodds.service.OddsOverlayService;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		RoboGuice.setUseAnnotationDatabases(false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, OddsOverlayService.class));
		getResources();
	}
}
