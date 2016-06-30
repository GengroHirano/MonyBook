package com.hse.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import au.com.bytecode.opencsv.CSVWriter;

import com.hse.model.CurrentDate;
import com.hse.model.PaymentsItem;
import com.hse.moneybook.MoneyBookApplication;

public class CSVFileOut {

	private final String DIRNAME = "/MONEYBOOK" ;
	private Context context ;
	private MoneyBookApplication app ;
	private CSVWriter csvWriter ;
	private File csvFile ;

	public CSVFileOut(Context context, MoneyBookApplication app) {
		this.context = context ;
		this.app = app ;
	}

	public void fileOpen(CurrentDate date) throws IOException{
		File file ;
		if( Build.VERSION.SDK_INT >= 19){
			file = new File(context.getExternalFilesDir(null).getAbsolutePath() + DIRNAME) ;
			if(!file.exists()){
				file.mkdir() ;
			}
		}
		else{
			file = new File(Environment.getExternalStorageDirectory().getPath() + DIRNAME) ;
			if(!file.exists()){
				file.mkdir() ;
			}
		}
		String[] fileName = new String[]{"", "", ""};
		switch (app.getFilterMode()) {
		case MoneyBookApplication.FILTER_MODE_DAY:
			fileName[2] = ""+date.getDay()+"日" ;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			fileName[1] = ""+date.getMonth()+"月" ;
		case MoneyBookApplication.FILTER_MODE_YEAR:
			fileName[0] = ""+date.getYear()+"年" ;
			break;
		}
		csvFile = new File(file.getAbsolutePath() + "/" +
				fileName[0] +
				fileName[1] +
				fileName[2] +
				".csv") ;
		FileOutputStream fos = new FileOutputStream(csvFile) ;
		csvWriter = new CSVWriter(new OutputStreamWriter(fos, "Shift_JIS")) ;
	}

	public void headerWrite(String...strings){

	}

	public void headerWrite(){
		csvWriter.writeNext(new String[]{"ひにち", 
				"じかん", 
				"かったもの", 
				"ひとこと", 
				"ねだん"}) ;
	}

	public void contentWrite(PaymentsItem item){
		String date = ""+item.getYear() + "/"+item.getMonth() + "/"+item.getDay() ;
		String time = ""+item.getHour() + ":" + item.getMinutes() ;
		String price = item.getStatus() != 0 ? ""+item.getPrice() : ""+(item.getPrice() * -1);
		csvWriter.writeNext(new String[]{date,
				time, 
				item.getName(), 
				item.getDescription(), 
				price}) ;
	}

	public void footerWrite(String total){
		csvWriter.writeNext(new String[]{"ごうけい", "", "", "", total}) ;
	}

	public void fileClose() throws IOException{
		csvWriter.close() ;
	}

}
