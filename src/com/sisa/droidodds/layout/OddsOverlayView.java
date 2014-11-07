package com.sisa.droidodds.layout;

import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.sisa.droidodds.R;
import com.sisa.droidodds.calculator.OddsCalculatorFacade;
import com.sisa.droidodds.service.OverlayService;

public class OddsOverlayView extends OverlayView {

	private final OddsCalculatorFacade odssCalculatorFacade;
	private TextView info;
	private Handler handler;

	public OddsOverlayView(final OverlayService service) {
		super(service, R.layout.odds_activity, 1);
		odssCalculatorFacade = new OddsCalculatorFacade();
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
			handler.postDelayed(this, 100);
		}
	}

}
