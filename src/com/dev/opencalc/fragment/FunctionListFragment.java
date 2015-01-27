package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.activity.FunctionListActivity;
import com.dev.opencalc.adapter.FunctionAdapter;
import com.dev.opencalc.model.FunctionMeta;

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
 * A simple vertical list fragment designed to display and interact with functions.
 * @author John Riley
 * 
 */
public class FunctionListFragment extends SupportListFragment<FunctionMeta> {
	
	private static final String DATA_KEY = "com.qu.simplelistfragment.data";

	public static SupportFragment newInstance(FunctionMeta[] arr) {
		SupportFragment fragment = new FunctionListFragment();
		
		if(arr != null && arr.length > 0){
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
	    String title = "Function Options";
	    menu.setHeaderTitle(title);

	    menu.add(Menu.NONE, R.id.edit_function_list_item, Menu.NONE, getString(R.string.text_edit));
	    menu.add(Menu.NONE, R.id.delete_function_list_item, Menu.NONE, getString(R.string.text_delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		FunctionListActivity activity = (FunctionListActivity) getActivity();
	    switch (item.getItemId()) {
	    case R.id.edit_function_list_item:
	    	activity.onEditFunction(info.position);
	        return true;
	    case R.id.delete_function_list_item:
	        activity.onDeleteFunction(info.position);
	        return true;
	    default:
	        return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	protected int getLayoutId() { return R.layout.fragment_simple_list;}
	
	@Override
	public int getListViewId() { return R.id.simpleListView; }
	
	@Override
	protected void initializeFragment(View rootView, Bundle args){
		initializeDefaultFragment(rootView);
		if(args != null){
			if(args.containsKey(DATA_KEY)){
				mData = (FunctionMeta[]) args.getParcelableArray(DATA_KEY);
				FunctionAdapter adapter = new FunctionAdapter(rootView.getContext(), mData);
				this.setListAdapter(adapter);
			}
		}
	}

	@Override
	protected ArrayAdapter<FunctionMeta> getAdapter(FunctionMeta[] data) {
		return new FunctionAdapter(getActivity(), data);
	}
}
