package com.hse.moneybook;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MoneyBookApplication extends Application {

	public static final int FILTER_MODE_YEAR = 0 ;
	public static final int FILTER_MODE_MONTH = 1 ;
	public static final int FILTER_MODE_DAY = 2 ;
	
	private int currentYear ;
	private int currentMonth ;
	private int currentDay ;
	private int filterMode ;
	private boolean showGraph ;
	
	@Override
	public void onCreate() {
		SharedPreferences pref = getSharedPreferences(getString(R.string.SHARED_PREFERENCE_NAME), MODE_PRIVATE) ;
		int mode = pref.getInt(getString(R.string.FILTER_KEY), FILTER_MODE_MONTH) ;
		boolean show = pref.getBoolean(getString(R.string.GRAPH_KEY), false) ;
		setFilterMode(mode) ;
		setShowGraph(show) ;
		super.onCreate();
	}
	
	public int getCurrentYear() {
		return currentYear;
	}
	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}
	
	public int getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(int currentMonth) {
		this.currentMonth = currentMonth;
	}
	
	public int getCurrentDay() {
		return currentDay;
	}
	public void setCurrentDay(int currentDay) {
		this.currentDay = currentDay;
	}
	
	public int getFilterMode() {
		return filterMode;
	}
	public void setFilterMode(int filterMode) {
		switch (filterMode) {
		case FILTER_MODE_YEAR:
			this.filterMode = filterMode ;
			break;
		case FILTER_MODE_MONTH:
			this.filterMode = filterMode ;
			break ;
		case FILTER_MODE_DAY:
			this.filterMode = filterMode ;
			break ;
			
		default:
			this.filterMode = FILTER_MODE_MONTH ;
			break;
		}
		this.filterMode = filterMode;
		SharedPreferences pref = getSharedPreferences(getString(R.string.SHARED_PREFERENCE_NAME), MODE_PRIVATE) ;
		Editor editor = pref.edit() ;
		editor.putInt(getString(R.string.FILTER_KEY), filterMode) ;
		editor.commit() ;
	}

	public boolean getShowGraph() {
		return showGraph;
	}
	public void setShowGraph(boolean showGraph) {
		this.showGraph = showGraph;
		SharedPreferences pref = getSharedPreferences(getString(R.string.SHARED_PREFERENCE_NAME), MODE_PRIVATE) ;
		Editor editor = pref.edit() ;
		editor.putBoolean(getString(R.string.GRAPH_KEY), showGraph) ;
		editor.commit() ;
	}
}
