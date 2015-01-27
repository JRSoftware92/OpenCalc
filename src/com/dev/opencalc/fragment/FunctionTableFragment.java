package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.adapter.FunctionSeriesAdapter;
import com.dev.opencalc.model.FunctionSeries;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

/**
 * NOT CURRENTLY IMPLEMENTED
 * Fragment for displaying a table for a given function series.
 * @author John Riley
 *
 */
public class FunctionTableFragment extends SupportFragment {
	
	private static final String DATA_KEY = "com.calculator.series";
	
	private FunctionSeries mSeries;
	private TableLayout mTable;
	private FunctionSeriesAdapter mAdapter;

	public static SupportFragment newInstance(){
		return SimpleTextFragment.newInstance("No data available to display\u2026");
	}
	
	public static SupportFragment newInstance(FunctionSeries series){
		SupportFragment fragment = null;
		if(series != null && series.size() > 0){
			fragment = new FunctionTableFragment();
			Bundle args = new Bundle();
			
			args.putParcelable(DATA_KEY, series);
			fragment.setArguments(args);
		}
		else{
			fragment = SimpleTextFragment.newInstance("No data available to display\u2026");
		}
		return fragment;
	}

	@Override
	protected int getLayoutId() { return R.layout.fragment_function_table; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		initializeDefaultFragment(rootView);
		if(args.containsKey(DATA_KEY)){
			mSeries = args.getParcelable(DATA_KEY);
		}
		
		setData(mSeries);
	}

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mTable = (TableLayout) rootView.findViewById(R.id.functionSeriesTableLayout);
	}

	public void setData(FunctionSeries series){
		if(mSeries != null){
			FunctionSeriesAdapter adapter = new FunctionSeriesAdapter(getActivity(), series);
			setAdapter(adapter);
		}
		else{
			//TODO Default
		}
	}
	
	public void setAdapter(FunctionSeriesAdapter adapter){
		mAdapter = adapter;
		refresh();
	}
	
	public void refresh(){
		mTable.removeAllViews();
		int size = mAdapter.getCount();
		for(int i = 0; i < size; i++){
			mTable.addView(mAdapter.getView(i, null, mTable));
		}
	}
}
