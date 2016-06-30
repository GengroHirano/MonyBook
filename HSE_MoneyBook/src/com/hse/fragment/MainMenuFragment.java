package com.hse.fragment;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieGraph.OnSliceClickedListener;
import com.echo.holographlibrary.PieSlice;
import com.hse.moneyDB.DBAdapter;
import com.hse.moneybook.MainActivity;
import com.hse.moneybook.MoneyBookApplication;
import com.hse.moneybook.R;
import com.hse.ui.CustomToast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMenuFragment extends BaseFragment {

	TextView statusText ;
	Button inButton ;
	Button outButton ;
	Button bookButton ;
	MoneyBookApplication app ;
	
	int income = 0 ;
	int spending = 0 ;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setTitle(getActivity(), "めざせ！おくまんちょうじゃ！") ;
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		statusText = (TextView)rootView.findViewById(R.id.statusText) ;
		inButton = (Button)rootView.findViewById(R.id.in_money);
		outButton = (Button)rootView.findViewById(R.id.out_money) ;
		bookButton = (Button)rootView.findViewById(R.id.book) ;
		app = (MoneyBookApplication)getActivity().getApplication() ;
				
		income = getTotalIncome() ;
		spending = getTotalSpending() ;
		PieGraph graph = (PieGraph)rootView.findViewById(R.id.graph);
		PieSlice slice = new PieSlice();
		slice.setColor(Color.parseColor("#99CC00"));
		slice.setValue((income - spending < 0) ? 0 : income - spending);
		graph.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#FF2222"));
		slice.setValue(spending);
		graph.addSlice(slice);
		graph.setOnSliceClickedListener(new OnSliceClickedListener() {
			
			@Override
			public void onClick(int index) {
				int price = (index == 0) ? income - spending : spending ;
				CustomToast.show(getActivity().getLayoutInflater(), 
						getActivity().getApplicationContext(), 
						""+price+"えん") ;
			}
		}) ;
		if (!app.getShowGraph()) {
			graph.setVisibility(View.INVISIBLE) ;
			statusText.setVisibility(View.INVISIBLE) ;
		}
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		statusText.setText(getStatus(income, spending));
		statusText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf"));

		TextView messageText = (TextView)getActivity().findViewById(R.id.messageText) ;
		messageText.setText("おかねを");
		messageText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf"));

		inButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf"));
		inButton.setText("いれる");
		inButton.setOnTouchListener(new ButtonTouchEvent());
		inButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if( delegate != null ){
					InputFragment inputFragment = new InputFragment() ;
					Bundle bundle = new Bundle() ;
					bundle.putInt("STATUS", InputFragment.STATUS_INPUT) ;
					inputFragment.setArguments(bundle);
					inputFragment.setDelegate((MainActivity)getActivity()) ;
					delegate.toNextFragment(inputFragment, "input");
				}
			}
		});

		outButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf"));
		outButton.setText("だす");
		outButton.setOnTouchListener(new ButtonTouchEvent());
		outButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if( delegate != null ){
					InputFragment inputFragment = new InputFragment() ;
					Bundle bundle = new Bundle() ;
					bundle.putInt("STATUS", InputFragment.STATUS_OUTPUT) ;
					bundle.putLong("time", System.currentTimeMillis());
					inputFragment.setArguments(bundle);
					inputFragment.setDelegate((MainActivity)getActivity()) ;
					delegate.toNextFragment(inputFragment, "input");
				}
			}

		}) ;

		bookButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf"));
		bookButton.setText("つうちょう");
		bookButton.setOnTouchListener(new ButtonTouchEvent());
		bookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( delegate != null ){
					PassBookFragment passBookFragment = new PassBookFragment() ;
					passBookFragment.setDelegate((MainActivity)getActivity()) ;
					delegate.toNextFragment(passBookFragment, "passBook");
				}
			}
		});
	}

	/**
	 * DBから収入の合計を持ってくる
	 * @return 収入の合計
	 */
	private int getTotalIncome(){
		DBAdapter dbAdapter = new DBAdapter(getActivity().getApplicationContext()) ;
		dbAdapter.open() ;
		Cursor c = null ;
		switch (app.getFilterMode()) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			c = dbAdapter.getTotalIncome(app.getCurrentYear()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			c = dbAdapter.getTotalIncome(app.getCurrentYear(), app.getCurrentMonth()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_DAY:
			c = dbAdapter.getTotalIncome(app.getCurrentYear(), app.getCurrentMonth(), app.getCurrentDay()) ;
			break;
		}
		c.moveToFirst() ;
		int total = c.getInt(c.getColumnIndex("total")) ;
		dbAdapter.close(c) ;
		return total ;
	}
	
	/**
	 * DBから出費の合計を持ってくる
	 * @return 出費の合計
	 */
	private int getTotalSpending(){
		DBAdapter dbAdapter = new DBAdapter(getActivity().getApplicationContext()) ;
		dbAdapter.open() ;
		Cursor c = null ;
		switch (app.getFilterMode()) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			c = dbAdapter.getTotalSpending(app.getCurrentYear()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			c = dbAdapter.getTotalSpending(app.getCurrentYear(), app.getCurrentMonth()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_DAY:
			c = dbAdapter.getTotalSpending(app.getCurrentYear(), app.getCurrentMonth(), app.getCurrentDay()) ;
			break;
		}
		c.moveToFirst() ;
		int total = c.getInt(c.getColumnIndex("total")) ;
		dbAdapter.close(c) ;
		return total ;
	}
	
	/**
	 * 収支のよってステータスを返す
	 * @param income 収入の合計
	 * @param spending 出費の合計
	 * @return ステータス
	 */
	private String getStatus(int income, int spending){
		int total = income + spending ;
		if(total == 0){
			return "なんもない" ;
		}
		else{
			if( ((double)income / (double)total) >= 0.5 ){
				return "おかねもち" ;
			}
			else{
				return "びんぼー" ;
			}
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private class ButtonTouchEvent implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Rect rect = new Rect() ;
			switch (event.getAction()) {
			//押下時
			case MotionEvent.ACTION_DOWN:
				v.setScaleX(2.0f);
				v.setScaleY(2.0f);
				break;
				//タッチイベント受け取るViewの範囲外を押下時
			case MotionEvent.ACTION_MOVE:
				v.getGlobalVisibleRect(rect) ;
				//タッチしたビューの外側に指が動いたら
				if( !((rect.top < event.getRawY() && rect.left < event.getRawX()) &&
						(rect.bottom > event.getRawY() && rect.right > event.getRawX())) ){
					v.setScaleX(1.0f);
					v.setScaleY(1.0f);
				}
				break ;
				//押下終了時
			case MotionEvent.ACTION_UP:
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
				break;

			default:
				break;
			}
			//タッチイベントを完結しない
			return false;
		}
	}
}