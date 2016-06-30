package com.hse.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import com.hse.moneybook.R;

public class TimePickerDialogFragment extends DialogFragment {

	public interface PushDialogTimeIF{
		/**
		 * ピッカーダイアログの決定ボタンが押された時に呼び出される
		 * @param Timepicker {@link TimePicker}
		 */
		public void dialogPushPositiveTime(TimePicker Timepicker) ;
	}
	
	private PushDialogTimeIF delegate ;
	
	public void setDelegate(PushDialogTimeIF delegate){
		this.delegate = delegate ;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf");
		
		final Dialog dialog = new Dialog(getActivity());
		// タイトル非表示
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// フルスクリーン
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		dialog.setContentView(R.layout.fragment_dialog_timepicker);
		
		final TimePicker timepicker = (TimePicker)dialog.findViewById(R.id.timePicker) ;
		timepicker.setIs24HourView(true) ;
		
		String time = getArguments().getString("time") ;
		Integer currentHour = Integer.parseInt(time.split(":")[0]) ;
		Integer currentMin = Integer.parseInt(time.split(":")[1]) ;
		
		timepicker.setCurrentHour(currentHour) ;
		timepicker.setCurrentMinute(currentMin) ;
		
		Button pButton = (Button)dialog.findViewById(R.id.positiveButton) ;
		pButton.setTypeface(tf);
		pButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( delegate != null ){
					delegate.dialogPushPositiveTime(timepicker);
					dialog.cancel();
				}
			}
			
		});
		
		Button nButton = (Button)dialog.findViewById(R.id.negativeButton) ;
		nButton.setTextColor(Color.RED);
		nButton.setTypeface(tf);
		nButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		return dialog ;
	}
	
}
