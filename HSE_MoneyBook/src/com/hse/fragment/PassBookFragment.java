package com.hse.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hse.model.CurrentDate;
import com.hse.model.PaymentsItem;
import com.hse.moneyDB.DBAdapter;
import com.hse.moneybook.MainActivity;
import com.hse.moneybook.MoneyBookApplication;
import com.hse.moneybook.R;
import com.hse.ui.CustomToast;
import com.hse.ui.MoneyListAdapter;
import com.hse.ui.MoneyListAdapter.DataChangeDelegate;
import com.hse.utility.CSVFileOut;
import com.hse.utility.MyComparator;


public class PassBookFragment extends BaseFragment implements DataChangeDelegate{

	private MoneyBookApplication app ;
	private ListView listView ;
	private MoneyListAdapter adapter ;
	private RelativeLayout footerView ;
	private DBAdapter dbAdapter ;
	private CurrentDate currentDate ;

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity) ;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MoneyBookApplication)getActivity().getApplication() ;
		currentDate = new CurrentDate(app.getCurrentYear(),
				app.getCurrentMonth(),
				app.getCurrentDay()) ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHeadTitle() ;
		View rootView = inflater.inflate(R.layout.fragment_list, container, false) ;
		footerView = (RelativeLayout)rootView.findViewById(R.id.sumArea) ;
		listView = (ListView)rootView.findViewById(R.id.moneyList) ;
		listView.setEmptyView(rootView.findViewById(R.id.emptyView));
		ArrayList<PaymentsItem> list = new ArrayList<PaymentsItem>() ;
		adapter = new MoneyListAdapter(getActivity().getApplicationContext(), list) ;
		adapter.setDalegate(this) ;
		footerView.setVisibility(View.INVISIBLE) ;
		listView.setMultiChoiceModeListener(new MultiClickAction()) ;
		listView.setOnItemClickListener(new ListClickEvent()) ;
		listView.setAdapter(adapter);

		return rootView ;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getItems(app.getFilterMode()) ;
		if (adapter.isEmpty()) {
			footerView.setVisibility(View.INVISIBLE) ;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.passbook_menu, menu) ;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_next:
			currentDate.next(app.getFilterMode()) ;
			setHeadTitle() ;
			getItems(app.getFilterMode()) ;
			break;
		case R.id.action_prev:
			currentDate.prev(app.getFilterMode()) ;
			setHeadTitle() ;
			getItems(app.getFilterMode()) ;
			break ;
		case R.id.action_csv_output:
			CSVFileOut out = new CSVFileOut(getActivity().getApplicationContext(), app) ;
			try {
				out.fileOpen(currentDate) ;
				out.headerWrite() ;
				for (int i = 0; i < adapter.getCount(); i++) {
					out.contentWrite(adapter.getItem(i)) ;
				}
				out.footerWrite(((TextView)footerView.findViewById(R.id.Money)).getText().toString()) ;
				out.fileClose() ;
				CustomToast.show(getActivity().getLayoutInflater(), 
						getActivity().getApplicationContext(), 
						"せいこう！") ;
			} catch (IOException e) {
				CustomToast.show(getActivity().getLayoutInflater(), 
						getActivity().getApplicationContext(), 
						"ごめん、しっぱいした") ;
				e.printStackTrace();
			}
			break ;
		}
		return true;
	}
	//タイトルを作成する
	private void setHeadTitle(){
		String title = "つうちょう" ;
		switch (app.getFilterMode()) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			int year = currentDate.getYear() ;
			title = "つうちょう\n("+ year +"ねん)" ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			int month = currentDate.getMonth() ;
			title = "つうちょう("+ month +"がつ)" ;
			break ;
		case MoneyBookApplication.FILTER_MODE_DAY:
			int day = currentDate.getDay() ;
			title = "つうちょう("+ day +"にち)" ;
			break ;
		}
		setTitle(getActivity(), title) ;
	}
	//データベースからデータを取ってくる
	private void getItems(int mode){
		dbAdapter = new DBAdapter(getActivity().getApplicationContext()) ;
		dbAdapter.open() ;
		Cursor c = null;
		switch (mode) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			c = dbAdapter.getItem(currentDate.getYear()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			c = dbAdapter.getItem(currentDate.getYear(), currentDate.getMonth()) ;
			break ;
		case MoneyBookApplication.FILTER_MODE_DAY:
			c = dbAdapter.getItem(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()) ;
			break ;
		default:
			c = dbAdapter.getAllItem() ;
			break;
		}
		setQueryItems(adapter, c) ;
		dbAdapter.close(c);
	}

	private void getTotal(int mode){
		dbAdapter.open() ;
		Cursor c = null ;
		switch (mode) {
		case MoneyBookApplication.FILTER_MODE_YEAR:
			c = dbAdapter.getTotal(currentDate.getYear()) ;
			break;
		case MoneyBookApplication.FILTER_MODE_MONTH:
			c = dbAdapter.getTotal(currentDate.getYear(), currentDate.getMonth()) ;
			break ;
		case MoneyBookApplication.FILTER_MODE_DAY:
			c = dbAdapter.getTotal(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()) ;
			break ;
		default:
			c = dbAdapter.getAllTotal() ;
			break;
		}
		c.moveToFirst() ;
		int total = c.getInt(c.getColumnIndex("total")) ;
		TextView totalView = ((TextView)footerView.findViewById(R.id.Money)) ;
		totalView.setText(""+total) ;
		if (total < 0) {
			totalView.setTextColor(Color.RED) ;
		}
		else{
			totalView.setTextColor(Color.BLACK) ;
		}
		dbAdapter.close(c) ;
	}

	//データベースから持ってきた値をセットする
	private void setQueryItems(MoneyListAdapter adapter, Cursor c){
		adapter.clear() ;
		if (c == null || c.getCount() == 0) {
			footerView.setVisibility(View.INVISIBLE) ;
			return ;
		}
		PaymentsItem item  ;
		while( c.moveToNext() ){
			item = new PaymentsItem() ;
			item.setId(c.getInt(c.getColumnIndex("_id"))) ;
			item.setName(c.getString(c.getColumnIndex("name"))) ;
			item.setDescription(c.getString(c.getColumnIndex("description"))) ;
			item.setPrice(c.getInt(c.getColumnIndex("price"))) ;
			item.setStatus(c.getInt(c.getColumnIndex("status"))) ;
			item.setYear(c.getInt(c.getColumnIndex("year"))) ;
			item.setMonth(c.getInt(c.getColumnIndex("month"))) ;
			item.setDay(c.getInt(c.getColumnIndex("day"))) ;
			item.setHour(c.getInt(c.getColumnIndex("hour"))) ;
			item.setMinutes(c.getInt(c.getColumnIndex("minutes"))) ;
			adapter.addPaymentList(item) ;
		}
	}

	//ListViewにセットするデータに変更がかかった時に呼び出される
	@Override
	public void onDataSourceChange() {
		if (adapter.getCount() >= 1) {
			if( footerView.getVisibility() == View.INVISIBLE ){
				footerView.setVisibility(View.VISIBLE) ;
			}
			getTotal(app.getFilterMode()) ;
		}
		else{
			footerView.setVisibility(View.INVISIBLE) ;
		}
	}

	//詳細画面へ遷移
	private class ListClickEvent implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			PaymentsItem item = (PaymentsItem)parent.getItemAtPosition(position) ;
			Bundle bundle = new Bundle() ;
			bundle.putInt("STATUS", InputFragment.STATUS_UPDATE) ;
			bundle.putString("_id", ""+item.getId()) ;
			bundle.putString("dispNum", ""+item.getPrice()) ;
			bundle.putString("buyName", item.getName()) ;
			bundle.putString("description", item.getDescription()) ;
			bundle.putInt("updateStatus", item.getStatus()) ;
			Calendar cal = Calendar.getInstance() ;
			cal.set(Calendar.YEAR, item.getYear()) ;
			cal.set(Calendar.MONTH, item.getMonth() -1) ;
			cal.set(Calendar.DAY_OF_MONTH, item.getDay()) ;
			cal.set(Calendar.HOUR_OF_DAY, item.getHour()) ;
			cal.set(Calendar.MINUTE, item.getMinutes()) ;
			bundle.putString("date", DateFormat.format("yyyy/MM/dd", cal.getTime()).toString()) ;
			bundle.putString("time", DateFormat.format("kk:mm", cal.getTime()).toString()) ;
			InputFragment inputFragment = new InputFragment() ;
			inputFragment.setArguments(bundle) ;
			inputFragment.setDelegate((MainActivity)getActivity()) ;
			delegate.toNextFragment(inputFragment, "update") ;
		}

	}

	//リストのアイテムを複数選択した時に呼び出される
	@SuppressLint("UseSparseArrays")
	private class MultiClickAction implements MultiChoiceModeListener{

		HashMap<Integer, Boolean> checkMap ;
		HashSet<Integer> keySet ;

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			listView.clearChoices() ;
			Iterator<Integer> iterator = keySet.iterator() ;
			ArrayList<Integer> positions = new ArrayList<Integer>();
			while (iterator.hasNext()) {
				Integer key = iterator.next();
				if ( checkMap.get(key) == true ) {
					positions.add(key) ;
				}
			}
			Collections.sort(positions, new MyComparator(MyComparator.DESC)) ;
			iterator = positions.iterator();
			while(iterator.hasNext()){
				Integer position = iterator.next() ;
				PaymentsItem listItem = adapter.getItem(position) ;
				dbAdapter.deleteData(listItem.getId()) ;
				adapter.removePaymentList(position) ;
			}
			mode.finish() ;
			return true;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater() ;
			inflater.inflate(R.menu.action_mode_menu, menu) ;
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			checkMap = new HashMap<Integer, Boolean>() ;
			keySet = new HashSet<Integer>() ;
			return true;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
			checkMap.put(position, checked) ;
			keySet.add(position) ;
		}
	}
}
