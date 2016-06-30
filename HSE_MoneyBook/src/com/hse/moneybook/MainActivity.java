package com.hse.moneybook;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.hse.fragment.BaseFragment.NextFragmentDelegate;
import com.hse.fragment.MainMenuFragment;
import com.hse.fragment.SettingMenuFragment;

public class MainActivity extends Activity implements NextFragmentDelegate{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*------------アクションバーの設定-----------------*/
		ActionBar actionBar = getActionBar() ;
		//ホームアイコンを表示したければこれを指定する必要がある
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.custom_actionbar);
		TextView title = (TextView)actionBar.getCustomView().findViewById(R.id.actionTitle) ;
		title.setText("めざせ！おくまんちょうじゃ!");
		title.setTypeface(Typeface.createFromAsset(getAssets(), "aquafont.ttf"));
		/*----------アクションバーの設定ここまで--------------*/
		setContentView(R.layout.activity_main);
		
		Calendar cal = Calendar.getInstance() ;
		cal.setTimeInMillis(System.currentTimeMillis()) ;
		MoneyBookApplication app = (MoneyBookApplication)getApplication() ;
		app.setCurrentYear(cal.get(Calendar.YEAR)) ;
		app.setCurrentMonth(cal.get(Calendar.MONTH) + 1) ;
		app.setCurrentDay(cal.get(Calendar.DAY_OF_MONTH)) ;
		
		if (savedInstanceState == null) {
			MainMenuFragment fragment = new MainMenuFragment() ;
			fragment.setDelegate(this);
			getFragmentManager().beginTransaction()
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.add(R.id.container, fragment)
			.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Log.v("アクションボタン", "設定画面へ") ;
			SettingMenuFragment settingFragment = new SettingMenuFragment() ;
			toNextFragment(settingFragment, "setting") ;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void toNextFragment(Fragment nextFragment, String tag) {
		getFragmentManager()
		.beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.addToBackStack(null)
		.replace(R.id.container, nextFragment, tag)
		.commit() ;
	}
}
