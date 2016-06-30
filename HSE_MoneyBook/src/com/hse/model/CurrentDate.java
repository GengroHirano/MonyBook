package com.hse.model;

import com.hse.moneybook.MoneyBookApplication;

public class CurrentDate {
	private int year ;
	private int month ;
	private int day ;
	
	public CurrentDate(int year, int month, int day) {
		this.year = year ;
		this.month = month ;
		this.day = day ;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	public void next(int mode){
		switch (mode) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			year++ ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			if(month < 12 ){
				month++ ;
			}
			else{
				year++ ;
				month = 1 ;
			}
			break;
		case MoneyBookApplication.FILTER_MODE_DAY:
			if(day < 31){
				day++ ;
			}
			else{
				month++ ;
				day = 1 ;
			}
			break;
		}
	}
	
	public void prev(int mode){
		switch (mode) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			year-- ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			if (month > 1) {
				month-- ;
			}
			else{
				month = 12 ;
				year-- ;
			}
			break;
		case MoneyBookApplication.FILTER_MODE_DAY:
			if (day > 1) {
				day-- ;
			}
			else{
				day = 31 ;
				month-- ;
			}
			break;
		}
	}
}
