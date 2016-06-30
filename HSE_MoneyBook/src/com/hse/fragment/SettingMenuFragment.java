package com.hse.fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hse.dialog.LicenseDialogFragment;
import com.hse.moneybook.MoneyBookApplication;
import com.hse.moneybook.R;
import com.hse.ui.ButtonTouchEvent;

public class SettingMenuFragment extends BaseFragment {

	private RadioGroup filterGroup ;
	private RadioGroup showGraphGroup ;
	private Button commitButton ;
	private Button licenseButton ;
	private MoneyBookApplication app ;
	
	int filterRadioID = 0 ;
	int showGraphRadioID = 0 ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MoneyBookApplication)getActivity().getApplication() ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setTitle(getActivity(), "せってい") ;
		View rootView = inflater.inflate(R.layout.fragment_setting, container, false) ;
		filterGroup = (RadioGroup)rootView.findViewById(R.id.filterGroup) ;
		showGraphGroup = (RadioGroup)rootView.findViewById(R.id.showGraphGroup) ;
		
		commitButton = (Button)rootView.findViewById(R.id.commitButton) ;
		commitButton.setOnTouchListener(new ButtonTouchEvent()) ;
		licenseButton = (Button)rootView.findViewById(R.id.licenseButton) ;
		licenseButton.setOnTouchListener(new ButtonTouchEvent()) ;
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "aquafont.ttf") ;
		commitButton.setTypeface(tf) ;
		licenseButton.setTypeface(tf) ;
		commitButton.setOnClickListener(new ButtonClickEvent()) ;
		licenseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LicenseDialogFragment dialogFragment = new LicenseDialogFragment() ;
				dialogFragment.show(getFragmentManager(), "LicenseDialog");
			}
		}) ;
		
		switch (app.getFilterMode()) {
		case MoneyBookApplication.FILTER_MODE_YEAR: filterRadioID = R.id.filterYear ; break;
		case MoneyBookApplication.FILTER_MODE_MONTH: filterRadioID = R.id.filterMonth ; break ;
		case MoneyBookApplication.FILTER_MODE_DAY: filterRadioID = R.id.filterDay ; break ;
		}
		if (app.getShowGraph()) {
			showGraphRadioID = R.id.showGraph ;
		}
		else{
			showGraphRadioID = R.id.hiddenGraph ;
		}
		filterGroup.check(filterRadioID) ;
		showGraphGroup.check(showGraphRadioID) ;
		filterGroup.setOnCheckedChangeListener(new RadioChangeEvent()) ;
		showGraphGroup.setOnCheckedChangeListener(new RadioChangeEvent()) ;
		
		return rootView ;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.empty_menu, menu) ;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	private class RadioChangeEvent implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getId()) {
			case R.id.filterGroup:
				filterRadioID = checkedId ;
				break;
			case R.id.showGraphGroup:
				showGraphRadioID = checkedId ;
				break;
			default:
				break;
			}
			setTitle(getActivity(), "せってい(へんこうがあるよ)") ;
		}
		
	}
	
	private class ButtonClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (filterRadioID) {
			case R.id.filterYear:
				app.setFilterMode(MoneyBookApplication.FILTER_MODE_YEAR) ;
				break;
			case R.id.filterMonth:
				app.setFilterMode(MoneyBookApplication.FILTER_MODE_MONTH) ;
				break;
			case R.id.filterDay:
				app.setFilterMode(MoneyBookApplication.FILTER_MODE_DAY) ;
				break;
			}
			
			switch (showGraphRadioID) {
			case R.id.showGraph:
				app.setShowGraph(true) ;
				break;
			case R.id.hiddenGraph:
				app.setShowGraph(false) ;
				break ;
			default:
				break;
			}
			getFragmentManager().popBackStack() ;
		}
		
	}
}