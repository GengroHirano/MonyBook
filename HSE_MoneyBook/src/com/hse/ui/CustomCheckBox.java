package com.hse.ui;

import com.hse.moneybook.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.CheckBox;

public class CustomCheckBox extends CheckBox {
	
	private final static String TAG = "com.hse.ui.CustomCheckBox" ;
	
	private static LruCache<String, Typeface> fontCache = FontCache.getInstance() ;
	
	public CustomCheckBox(Context context) {
		super(context);
	}
	
	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}
	
	public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomCheckBox);
		String customFont = a.getString(R.styleable.CustomCheckBox_customFont);
		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			tf = fontCache.get(asset);
			if( tf == null ){
				tf = Typeface.createFromAsset(ctx.getAssets(), asset);
				fontCache.put(asset, tf);
			}
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: "+e.getMessage());
			return false;
		}

		setTypeface(tf);  
		return true;
	}

}
