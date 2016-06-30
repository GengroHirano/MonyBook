package com.hse.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.hse.moneybook.R;

public class BaseFragment extends Fragment {

	public interface NextFragmentDelegate {
		/**
		 * fragmentの画面遷移を受け持つインターフェース
		 * @param nextFragment 遷移先フラグメント
		 * @param tag タグ
		 */
		public void toNextFragment(Fragment nextFragment, String tag);
	}

	protected NextFragmentDelegate delegate;

	/**
	 * デリゲートのセット
	 * @param delegate {@link NextFragmentDelegate}を実装したクラス
	 */
	public void setDelegate(NextFragmentDelegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true) ;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	/**
	 * アクションバーのタイトルを設定する
	 * @param activity アクティビティ
	 * @param title タイトル
	 */
	protected void setTitle(Activity activity, String title) {
		ActionBar actionBar = activity.getActionBar() ;
		((TextView)actionBar.getCustomView().findViewById(R.id.actionTitle)).setText(title) ;
	}
	
}
