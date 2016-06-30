package com.hse.ui;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ButtonTouchEvent implements OnTouchListener{

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Rect rect = new Rect() ;
		switch (event.getAction()) {
		//押下時
		case MotionEvent.ACTION_DOWN:
			v.setScaleX(2.0f);
			v.setScaleY(2.0f);
			break;
			//タッチイベント受け取るViewの範囲外を押下時
		case MotionEvent.ACTION_MOVE:
			v.getGlobalVisibleRect(rect) ;
			//タッチしたビューの外側に指が動いたら
			if( !((rect.top < event.getRawY() && rect.left < event.getRawX()) &&
					(rect.bottom > event.getRawY() && rect.right > event.getRawX())) ){
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}
			break ;
			//押下終了時
		case MotionEvent.ACTION_UP:
			v.setScaleX(1.0f);
			v.setScaleY(1.0f);
			break;

		default:
			break;
		}
		//タッチイベントを完結しない
		return false;
	}
}