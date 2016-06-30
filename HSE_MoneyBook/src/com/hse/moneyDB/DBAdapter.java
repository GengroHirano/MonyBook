package com.hse.moneyDB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

	private MoneySQLiteOpenHelper dbHelper ;
	private SQLiteDatabase db ;

	public DBAdapter(Context context) {	
		dbHelper = new MoneySQLiteOpenHelper(context) ;
	}

	public DBAdapter open(){
		db = dbHelper.getWritableDatabase() ;
		return this ;
	}

	public void close(Cursor c){
		c.close() ;
	}

	/**
	 * データベースから登録したデータすべてを持ってくる
	 * @return カーソル
	 */
	public Cursor getAllItem(){
		String sql = "SELECT * FROM price "
				+ "INNER JOIN buy_date ON price._id = buy_date._id " 
				+ "ORDER BY buy_date.year, buy_date.month, buy_date.day ;" ;
		return db.rawQuery(sql, null) ;
	}

	/**
	 * データベースから引数に指定した条件にマッチしたデータを持ってくる
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return カーソル
	 */
	public Cursor getItem(int year, int month, int day){
		String sql = "SELECT * FROM price " +
				"INNER JOIN buy_date ON price._id = buy_date._id " 
				+"WHERE buy_date.year = ? AND buy_date.month = ? AND buy_date.day = ?" 
				+"ORDER BY buy_date.year, buy_date.month, buy_date.day, buy_date.hour, buy_date.minutes ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month, ""+day}) ;
	}

	/**
	 * データベースから引数に指定した条件にマッチしたデータを持ってくる
	 * @param year 年
	 * @param month 月
	 * @return カーソル
	 */
	public Cursor getItem(int year, int month){
		String sql = "SELECT * FROM price " +
				"INNER JOIN buy_date ON price._id = buy_date._id " 
				+"WHERE buy_date.year = ? AND buy_date.month = ? " 
				+"ORDER BY buy_date.year, buy_date.month, buy_date.day, buy_date.hour, buy_date.minutes ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month}) ;
	}

	/**
	 * データベースから引数の条件にマッチしたデータを持ってくる
	 * @param year 年
	 * @return カーソル
	 */
	public Cursor getItem(int year){
		String sql = "SELECT * FROM price " +
				"INNER JOIN buy_date ON price._id = buy_date._id " 
				+"WHERE buy_date.year = ? " 
				+"ORDER BY buy_date.year, buy_date.month, buy_date.day, buy_date.hour, buy_date.minutes ;" ;
		return db.rawQuery(sql, new String[]{""+year}) ;
	}

	/**
	 * データベースに登録したデータの合計金額を持ってくる
	 * @return カーソル
	 */
	public Cursor getAllTotal(){
		String sql = "SELECT SUM( CASE WHEN price.status = 1 THEN price.price ELSE price.price * -1 END ) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id ;" ;
		return db.rawQuery(sql, null) ;
	}

	/**
	 * データベースに登録した引数の条件にマッチしたデータの合計金額を持って来る
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return カーソル
	 */
	public Cursor getTotal(int year, int month, int day){
		String sql = "SELECT SUM( CASE WHEN price.status = 1 THEN price.price ELSE price.price * -1 END ) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? AND buy_date.day = ? ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month, ""+day}) ;
	}

	/**
	 * データベースに登録した引数の条件にマッチしたデータの合計金額を持って来る
	 * @param year 年
	 * @param month 月
	 * @return カーソル
	 */
	public Cursor getTotal(int year, int month){
		String sql = "SELECT SUM( CASE WHEN price.status = 1 THEN price.price ELSE price.price * -1 END ) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month}) ;
	}

	/**
	 * データベースに登録した引数の条件にマッチしたデータの合計金額を持って来る
	 * @param year 年
	 * @return カーソル
	 */
	public Cursor getTotal(int year){
		String sql = "SELECT SUM( CASE WHEN price.status = 1 THEN price.price ELSE price.price * -1 END ) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? ;" ;
		return db.rawQuery(sql, new String[]{""+year}) ;
	}

	/**
	 * データベースに登録した収入の合計を引数の条件で持ってくる
	 * @param year　年
	 * @param month 月
	 * @param day 日
	 * @return カーソル
	 */
	public Cursor getTotalIncome(int year, int month, int day){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? AND buy_date.day = ? AND price.status = 1 ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month, ""+day}) ;
	}

	/**
	 * データベースに登録した収入の合計を引数の条件で持ってくる
	 * @param year　年
	 * @param month 月
	 * @return カーソル
	 */
	public Cursor getTotalIncome(int year, int month){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? AND price.status = 1 ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month}) ;
	}

	/**
	 * データベースに登録した収入の合計を引数の条件で持ってくる
	 * @param year　年
	 * @return カーソル
	 */
	public Cursor getTotalIncome(int year){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND price.status = 1 ;" ;
		return db.rawQuery(sql, new String[]{""+year}) ;
	}

	/**
	 * データベースに登録した出費の合計を引数の条件で持ってくる
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return カーソル
	 */
	public Cursor getTotalSpending(int year, int month, int day){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? AND buy_date.day = ? AND price.status = 0 ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month, ""+day}) ;
	}

	/**
	 * データベースに登録した出費の合計を引数の条件で持ってくる
	 * @param year 年
	 * @param month 月
	 * @return カーソル
	 */
	public Cursor getTotalSpending(int year, int month){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND buy_date.month = ? AND price.status = 0 ;" ;
		return db.rawQuery(sql, new String[]{""+year, ""+month}) ;
	}

	/**
	 * データベースに登録した出費の合計を引数の条件で持ってくる
	 * @param year 年
	 * @return カーソル
	 */
	public Cursor getTotalSpending(int year){
		String sql = "SELECT SUM(price.price) AS total " +
				"FROM price INNER JOIN buy_date ON price._id = buy_date._id " +
				"WHERE buy_date.year = ? AND price.status = 0 ;" ;
		return db.rawQuery(sql, new String[]{""+year}) ;
	}

	//データが追加された最新のIDを返す
	public int getLatestID(){
		String sql = "SELECT _id FROM price WHERE _id = LAST_INSERT_ROWID() ;" ;
		Cursor c = db.rawQuery(sql, null) ;
		c.moveToFirst() ;
		int id = c.getInt(0) ;
		c.close() ;
		return id ;
	}

	/**
	 * データを登録する
	 * @param data 商品名、備考、価格、ステータス、年、月、日、時、分
	 * @return
	 */
	public boolean insertData(String...data){
		String price_sql = "INSERT INTO price(name, description, price, status) VALUES (?, ?, ?, ?) ;" ;
		String buy_date_sql = "INSERT INTO buy_date VALUES (?, ?, ?, ?, ?, ?) ;" ;
		try {
			db.execSQL(price_sql, new String[]{
					data[0], //商品名
					data[1], //備考
					data[2], //価格
					data[3]}) ; //ステータス
			int id = getLatestID() ;
			Log.v("tag", ""+id) ;
			db.execSQL(buy_date_sql, new String[]{
					""+id,
					data[4], //年
					data[5], //月
					data[6], //日
					data[7], //時
					data[8]}); //分
			return true ;
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
	}

	/**
	 * データをアップデートする
	 * @param data _id, 商品名、備考、価格、年、月、日、時、分
	 * @return 処理成功の可否
	 */
	public boolean updateData(String...data){
		String price_sql = "UPDATE price SET name = ?, description = ?, price = ? WHERE _id = ? ;" ;
		String buy_date_sql = "UPDATE buy_date SET year = ?, month = ?, day = ?, hour = ?, minutes = ? WHERE _id = ? ;" ;
		try {
			db.execSQL(price_sql, new String[]{
					data[1], //商品名
					data[2], //備考
					data[3], //価格
					data[0]}) ; //_Id
			db.execSQL(buy_date_sql, new String[]{
					data[4], //年
					data[5], //月
					data[6], //日
					data[7], //時
					data[8], //分
					data[0]}); //_Id
			return true ;
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
	}

	/**
	 * データを削除する
	 * @param _id _id
	 * @return 処理成功の可否
	 */
	public boolean deleteData(Integer _id){
		String price_sql = "DELETE FROM price WHERE _id = ?" ;
		String buy_date_sql = "DELETE FROM buy_date WHERE _id = ?" ;
		try {
			db.execSQL(price_sql, new String[]{_id.toString()}) ;
			db.execSQL(buy_date_sql, new String[]{_id.toString()}) ;
			return true ;
		} catch (SQLException e) {
			e.printStackTrace() ;
			return false ;
		}
	}
}
