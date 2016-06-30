package com.hse.moneyDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoneySQLiteOpenHelper extends SQLiteOpenHelper {
	
	static final String DB ="hse_money_book.db" ;
	static final int DB_VERSION = 1 ;
	static final String CREATE_MAIN_TABLE = 
			"CREATE TABLE price(_id integer primary key autoincrement default 0,"
			+ " name text,"
			+ " description text,"
			+ " price integer(6),"
			+ " status integer(1)) ;" ;
	static final String CREATE_DATE_TABLE = 
			"CREATE TABLE buy_date(_id integer default 0,"
			+ " year integer(4),"
			+ " month integer(2),"
			+ " day integer(2),"
			+ " hour integer(2),"
			+ " minutes integer(2)) ;" ;
	static final String DROP_TABLE = "DROP TABLE price; DROP TABLE DATE_TABLE" ;
	
	public MoneySQLiteOpenHelper(Context context) {
		super(context, DB, null, DB_VERSION) ;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MAIN_TABLE);
		db.execSQL(CREATE_DATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if( oldVersion < newVersion ){
			db.execSQL(DROP_TABLE);
		}
	}
	
}
