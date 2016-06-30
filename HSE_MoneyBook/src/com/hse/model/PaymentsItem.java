package com.hse.model;

public class PaymentsItem {

	/*
	 * 登録ID
	 */
	private int id ;
	
	/*
	 * 買ったものの名前
	 */
	private String name ;
	
	/*
	 * ひとこと
	 */
	private String description ;

	/*
	 * 値段
	 */
	private int price ;
	
	/*
	 * ステータス　0:出費 1以上:収入
	 */
	private int status ;

	/*
	 * 買った年
	 */
	private int year ;
	
	/*
	 * 買った月 
	 */
	private int month ;
	
	/*
	 * 買った日
	 */
	private int day ;
	
	/*
	 * 買った時間
	 */
	private int hour ;
	
	/*
	 * 買った分
	 */
	private int minutes ;

	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id ;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
}
