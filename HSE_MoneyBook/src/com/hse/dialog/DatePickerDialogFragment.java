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
import android.widget.DatePicker;

import com.hse.moneybook.R;

public class DatePickerDialogFragment extends DialogFragment {

	public interface PushDialogDateIF{
		/**
		 * ピッカーダイアログの決定ボタンが押された時に呼び出される
		 * @param datepicker {@link DatePicker}
		 */
		public void dialogPushPositiveDate(DatePicker datepicker) ;
	}
	
	private PushDialogDateIF delegate ;
	
	public void setDelegate(PushDialogDateIF delegate){
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
		dialog.setContentView(R.layout.fragment_dialog_datepicker);
		
		final DatePicker datepicker = (DatePicker)dialog.findViewById(R.id.datePicker) ;
		
		Button pButton = (Button)dialog.findViewById(R.id.positiveButton) ;
		pButton.setTypeface(tf);
		pButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( delegate != null ){
					delegate.dialogPushPositiveDate(datepicker);
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
