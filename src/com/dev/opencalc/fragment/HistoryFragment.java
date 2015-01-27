package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.activity.HistoryActivity;
import com.dev.opencalc.adapter.CalculationAdapter;
import com.dev.opencalc.model.Calculation;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

/**
 * 
 * A simple vertical list fragment designed to display an interact with calculation history.
 * @author John Riley
 * 
 */
public class HistoryFragment extends SupportListFragment<Calculation> {
	
	private static final String DATA_KEY = "com.qu.simplelistfragment.data";

	public static SupportFragment newInstance(Calculation[] arr) {
		SupportFragment fragment = null;
		if(arr != null && arr.length > 0){
			fragment = new HistoryFragment();
			Bundle args = new Bundle();
			
			args.putParcelableArray(DATA_KEY, arr);
			fragment.setArguments(args);
		}
		else{
			fragment = SimpleTextFragment.newInstance("No data available to display\u2026");
		}
		return fragment;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    String title = "Calculation";
	    menu.setHeaderTitle(title);

	    menu.add(Menu.NONE, R.id.delete_function_list_item, Menu.NONE, getString(R.string.text_delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		HistoryActivity activity = (HistoryActivity) getActivity();
	    switch (item.getItemId()) {
	    case R.id.delete_function_list_item:
	        activity.onDeleteCalculation(info.position);
	        return true;
	    default:
	        return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	protected int getLayoutId() { return R.layout.fragment_simple_list; }
	
	@Override
	protected int getListViewId(){ return R.id.simpleListView; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		initializeDefaultFragment(rootView);
		if(args != null){
			if(args.containsKey(DATA_KEY)){
				mData = (Calculation[]) args.getParcelableArray(DATA_KEY);
				CalculationAdapter adapter = new CalculationAdapter(rootView.getContext(), mData);
				mListView.setAdapter(adapter);
			}
		}
	}

	@Override
	protected ArrayAdapter<Calculation> getAdapter(Calculation[] data) {
		return new CalculationAdapter(getActivity(), data);
	}
}
