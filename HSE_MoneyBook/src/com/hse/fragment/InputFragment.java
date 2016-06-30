package com.hse.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hse.dialog.DatePickerDialogFragment;
import com.hse.dialog.DatePickerDialogFragment.PushDialogDateIF;
import com.hse.dialog.TimePickerDialogFragment;
import com.hse.dialog.TimePickerDialogFragment.PushDialogTimeIF;
import com.hse.moneyDB.DBAdapter;
import com.hse.moneybook.R;
import com.hse.ui.ButtonTouchEvent;
import com.hse.ui.CustomToast;

public class InputFragment extends BaseFragment implements PushDialogDateIF, PushDialogTimeIF {

	public static final int STATUS_INPUT = 1 ;
	public static final int STATUS_OUTPUT = 0 ;
	public static final int STATUS_UPDATE = 2 ;
	//現在のステータス
	int status ;

	//金額表示View
	TextView dispNum ;

	//日付の入力フォーム
	TextView dateText ;
	TextView timeText ;

	//購入品名入力フォーム
	EditText editBuyName ;
	//備考入力フォーム
	EditText editDescription ;

	//キーボードコントロールクラス
	InputMethodManager mInputMethodManager ;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity) ;
		setTitle(activity, "とうろくがめん") ;
		status = getArguments().getInt("STATUS", 1) ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_in_out_money, container, false);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf") ;

		//入力欄のオブジェクト取得
		dispNum = (TextView)rootView.findViewById(R.id.dispNum) ;
		editBuyName = (EditText)rootView.findViewById(R.id.buyName) ;
		editDescription = (EditText)rootView.findViewById(R.id.description) ;
		dateText = (TextView)rootView.findViewById(R.id.editDate) ;
		timeText = (TextView)rootView.findViewById(R.id.editDay) ;

		//初期設定
		dispNum = (TextView)rootView.findViewById(R.id.dispNum) ;
		final Calendar cal = Calendar.getInstance() ;
		dateText.setText(DateFormat.format("yyyy/MM/dd", cal.getTime()));
		timeText.setText(DateFormat.format("kk:mm", cal.getTime()));
		editBuyName.setTypeface(tf);
		editDescription.setTypeface(tf);

		switch (status) {
		case STATUS_INPUT:
			dispNum.setTextColor(0xff000000) ;
			((TextView)rootView.findViewById(R.id.buy)).setText("おこづかい");
			break;
		case STATUS_OUTPUT:
			dispNum.setTextColor(0xffff0000) ;
			((TextView)rootView.findViewById(R.id.buy)).setText("かったもの");
			break;
		case STATUS_UPDATE:
			setTitle(getActivity(), "しょうさい") ;
			if (getArguments().getInt("updateStatus") == STATUS_INPUT) {
				dispNum.setTextColor(0xff000000) ;
				((TextView)rootView.findViewById(R.id.buy)).setText("おこづかい");
			}
			else{
				dispNum.setTextColor(0xffff0000) ;
				((TextView)rootView.findViewById(R.id.buy)).setText("かったもの");
			}
			dispNum.setText(getArguments().getString("dispNum")) ;
			editBuyName.setText(getArguments().getString("buyName")) ;
			editDescription.setText(getArguments().getString("description")) ;
			dateText.setText(getArguments().getString("date")) ;
			timeText.setText(getArguments().getString("time")) ;
			break ;
		}

		//ボタンの初期設定
		initButtons(rootView, tf);

		//エディットテキストのリスナ設定
		mInputMethodManager = (InputMethodManager)getActivity()
				.getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		EditTextKeyEvent editEvent = new EditTextKeyEvent() ;
		editBuyName.setOnKeyListener(editEvent);
		editDescription.setOnKeyListener(editEvent);

		//日付表示部分のリスナ設定
		timeText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle() ;
				String time = DateFormat.format("kk:mm", cal.getTime()).toString() ;
				bundle.putString("time", getArguments().getString("time", time)) ;
				TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment() ;
				dialogFragment.setArguments(bundle) ;
				dialogFragment.setDelegate(InputFragment.this);
				dialogFragment.show(getFragmentManager(), "TimePickerDialog");
			}
			
		});
		dateText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment() ;
				dialogFragment.setDelegate(InputFragment.this);
				dialogFragment.show(getFragmentManager(), "DatePickerDialog");
			}
			
		});

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	//ボタンオブジェクトの初期設定
	private void initButtons(View rootView, Typeface tf){
		ButtonClickEvent clickEvent = new ButtonClickEvent() ;
		ButtonTouchEvent event = new ButtonTouchEvent() ;
		for(int i = 0; i < 10; i++){
			int id = getActivity().getResources().getIdentifier("button" + i,
					"id",
					getActivity().getPackageName()) ;
			Button button = (Button)rootView.findViewById(id) ;
			button.setTag(i);
			button.setTypeface(tf);
			button.setOnClickListener(clickEvent);
			button.setOnTouchListener(event);
		}

		Button button = (Button)rootView.findViewById(R.id.resist_button) ;
		button.setTag(10);
		button.setTypeface(tf);
		button.setOnClickListener(clickEvent);
		button.setOnTouchListener(event);

		button = (Button)rootView.findViewById(R.id.clear_button) ;
		button.setTag(11);
		button.setTypeface(tf);
		button.setOnClickListener(clickEvent);
		button.setOnTouchListener(event);
		button.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				dispNum.setText("0");
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
				return true;
			}
		});
	}

	private boolean inValidateCheck(){
		if( editBuyName.getText().length() == 0 ){
			return false ;
		}
		return true ;
	}

	//トースト表示用メソッド
	private void showToast(){
		String text = "";
		switch (status) {
		case STATUS_INPUT:
			text = "おこづかいのなまえをいれてね" ;
			break;
		case STATUS_OUTPUT:
			text = "かったもののなまえをいれてね" ;
			break ;
		case STATUS_UPDATE:
			text = "なまえをいれてね" ;
			break ;
		}
		CustomToast.show(getActivity().getLayoutInflater(), getActivity().getApplicationContext(), text) ;
	}

	/*
	 * (非 Javadoc)デリゲートメソッド
	 * @see com.hse.dialog.DatePickerDialogFragment.PushDialogDateIF#dialogPushPositiveDate(android.widget.DatePicker)
	 */
	@Override
	public void dialogPushPositiveDate(DatePicker datepicker) {
		Calendar cal = Calendar.getInstance() ;
		cal.set(Calendar.YEAR, datepicker.getYear());
		cal.set(Calendar.MONTH, datepicker.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, datepicker.getDayOfMonth());
		dateText.setText(DateFormat.format("yyyy/MM/dd", cal.getTime()));
	}

	/*
	 * (非 Javadoc)デリゲートメソッド
	 * @see com.hse.dialog.TimePickerDialogFragment.PushDialogTimeIF#dialogPushPositiveTime(android.widget.TimePicker)
	 */
	@Override
	public void dialogPushPositiveTime(TimePicker timepicker) {
		Calendar cal = Calendar.getInstance() ;
		cal.set(Calendar.HOUR_OF_DAY, timepicker.getCurrentHour());
		cal.set(Calendar.MINUTE, timepicker.getCurrentMinute());
		timeText.setText(DateFormat.format("kk:mm", cal.getTime()));
	}

	//ボタンクリックイベントクラス
	private class ButtonClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			int tagNumber = (Integer)v.getTag() ;
			//登録
			if( tagNumber == 10){
				if(inValidateCheck()){
					DBAdapter dbAdapter = new DBAdapter(getActivity().getApplicationContext()) ;
					dbAdapter.open() ;
					if(status != STATUS_UPDATE){
						dbAdapter.insertData(editBuyName.getText().toString(),
								editDescription.getText().toString(), 
								dispNum.getText().toString(), 
								""+status, 
								dateText.getText().subSequence(0, 4).toString(), 
								dateText.getText().subSequence(5, 7).toString(), 
								dateText.getText().subSequence(8, 10).toString(), 
								timeText.getText().subSequence(0, 2).toString(), 
								timeText.getText().subSequence(3, 5).toString()) ;
						getFragmentManager().popBackStack();
					}
					else{
						dbAdapter.updateData(getArguments().getString("_id"), 
								editBuyName.getText().toString(),
								editDescription.getText().toString(), 
								dispNum.getText().toString(), 
								dateText.getText().subSequence(0, 4).toString(), 
								dateText.getText().subSequence(5, 7).toString(), 
								dateText.getText().subSequence(8, 10).toString(), 
								timeText.getText().subSequence(0, 2).toString(), 
								timeText.getText().subSequence(3, 5).toString()) ;
						getFragmentManager().popBackStack();
					}
				}
				else{
					showToast();
				}
			}
			//クリア
			else if( tagNumber == 11 ){
				CharSequence cq = dispNum.getText() ;
				if( cq.length() != 0){
					if( dispNum.getText().length() == 1 ){
						dispNum.setText("0") ;
					}
					else{
						dispNum.setText(cq.subSequence(0, cq.length() - 1));
					}
				}
			}
			//数字ボタン
			else{
				if( dispNum.getText().length() == 1 && dispNum.getText().charAt(0) == '0' ){
					dispNum.setText(v.getTag().toString());
				}
				else if( dispNum.length() <= 6 ){
					dispNum.append(v.getTag().toString());
				}
			}
		}
	}

	//キーボードのイベントクラス
	private class EditTextKeyEvent implements OnKeyListener{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if( (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) ){
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
				return true ;
			}
			return false;
		}
	}
}
