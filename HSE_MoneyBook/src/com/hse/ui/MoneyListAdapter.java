package com.hse.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hse.model.PaymentsItem;
import com.hse.moneybook.R;

public class MoneyListAdapter extends ArrayAdapter<PaymentsItem> {
	
	public interface DataChangeDelegate {
		/**
		 * アダプターにバインドされているデータに変更が有った時に呼び出される
		 */
		public void onDataSourceChange() ;
	}
	
	private ArrayList<PaymentsItem> PaymentList ;
	private LayoutInflater mInflater ;
	private DataChangeDelegate delegate ;
	
	public MoneyListAdapter(Context context, ArrayList<PaymentsItem> item) {
		super(context, 0, item);
		PaymentList = item ;
		mInflater = (LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
	}
	
	/**
	 * ListViewのメモリ効率を上げるためのViewHolder
	 * @see http://labs.techfirm.co.jp/android/cho/2161
	 */
	static class ViewHolder{
		CustomTextView date ;
		CustomTextView buyName ;
		CustomTextView buy ;
	}
	
	//デリゲートセット
	public void setDalegate(DataChangeDelegate delegate){
		this.delegate = delegate ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if( convertView == null ){
			convertView = mInflater.inflate(R.layout.item_list, parent, false) ;
			holder = new ViewHolder() ;
			holder.date = (CustomTextView)convertView.findViewById(R.id.DateOfPurchase) ;
			holder.buyName = (CustomTextView)convertView.findViewById(R.id.PurchaseName) ;
			holder.buy = (CustomTextView)convertView.findViewById(R.id.Money) ;
			convertView.setTag(holder) ;
		}
		else{
			holder = (ViewHolder)convertView.getTag() ;
		}
		if( !PaymentList.isEmpty() ){
			PaymentsItem item = PaymentList.get(position) ;
			holder.date.setText(item.getYear() + "ねん" + item.getMonth() + "がつ" + item.getDay() + "にち");
			holder.buyName.setText(item.getName());
			if( item.getStatus() != 1 ){
				holder.buy.setTextColor(Color.RED) ;
			}
			else{
				holder.buy.setTextColor(Color.BLACK) ;
			}
			holder.buy.setText(""+item.getPrice());
		}
		return convertView;
	}
	
	public void addPaymentList(PaymentsItem item) {
		PaymentList.add(item) ;
		this.notifyDataSetChanged();
		if (delegate != null) {
			delegate.onDataSourceChange() ;
		}
	}
	
	public void removePaymentList(int position){
		PaymentList.remove(position) ;
		this.notifyDataSetChanged();
		if (delegate != null) {
			delegate.onDataSourceChange() ;
		}
	}

}
