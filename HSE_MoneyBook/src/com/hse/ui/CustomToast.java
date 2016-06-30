package com.hse.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.moneybook.R;

public class CustomToast {
	
	public static void show(LayoutInflater inflater, Context context, String text){
		View toastLayout = inflater.inflate(R.layout.toast_layout, null) ;
		((TextView)toastLayout.findViewById(R.id.toastText)).setText(text);
		Toast toast = new Toast(context) ;
		toast.setView(toastLayout);
		toast.show();
	}
}
