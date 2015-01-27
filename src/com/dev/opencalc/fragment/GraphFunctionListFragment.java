package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.adapter.ExpandableFunctionAdapter;
import com.dev.opencalc.adapter.ExpandableFunctionAdapter.OnValueChangedListener;
import com.dev.opencalc.model.FunctionMeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * 
 * A simple vertical list fragment designed to display and interact with functions.
 * @author John Riley
 * 
 */
@Deprecated
public class GraphFunctionListFragment extends SupportFragment implements 
											ExpandableFunctionAdapter.OnValueChangedListener {
	
	private static final String DATA_KEY = "com.qu.simplelistfragment.data";
	private static final String PARAM_KEY = "com.qu.simplelistfragment.params";
	
	private ExpandableListView mListView = null;
	
	private OnValueChangedListener mListener = null;
	
	public static GraphFunctionListFragment newInstance(FunctionMeta arr, double[] params) {
		return newInstance(new FunctionMeta[]{arr}, new double[][]{params});
	}

	public static GraphFunctionListFragment newInstance(FunctionMeta[] arr, double[][] params) {
		GraphFunctionListFragment fragment = new GraphFunctionListFragment();
		
		Bundle args = new Bundle();
		args.putParcelableArray(DATA_KEY, arr);
		for(int i = 0; i < arr.length; i++){
			args.putDoubleArray(PARAM_KEY + "_" + arr[i].getTokenString(), params[i]);
		}
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnValueChangedListener) activity;
		} 
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnValueChangedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	protected int getLayoutId() { return R.layout.fragment_simple_list; }

	/** TODO */
	@Override
	protected void initializeDefaultFragment(View rootView) {
		//mListView = (ExpandableListView) rootView.findViewById(R.id.simpleListView);
	}
	
	@Override
	protected void initializeFragment(View rootView, Bundle args){
		initializeDefaultFragment(rootView);
		FunctionMeta[] functions = null;
		if(args != null){
			functions = (FunctionMeta[]) args.getParcelableArray(DATA_KEY);
			double[][] params = new double[functions.length][];
			for(int i = 0; i < functions.length; i++){
				params[i] = args.getDoubleArray(PARAM_KEY + "_" + functions[i].getTokenString());
			}
		}
		
		ExpandableFunctionAdapter adapter = new ExpandableFunctionAdapter(rootView.getContext(), functions);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onParameterValueUpdated(int funcPos, int paramPos, double value) {
		if(mListener != null){
			mListener.onParameterValueUpdated(funcPos, paramPos, value);
		}
	}
	
	@Override
	public void onPrimaryAxisSet(int funcPos, int paramPos) {
		if(mListener != null){
			mListener.onPrimaryAxisSet(funcPos, paramPos);
		}
	}
	
	public void setOnValueChangedListener(OnValueChangedListener listener){
		mListener = listener;
	}
	
	public void setListData(FunctionMeta[] arr){
		setListAdapter(new ExpandableFunctionAdapter(getActivity(), arr));
	}
	
	public void setListAdapter(ExpandableFunctionAdapter adapter){
		mListView.setAdapter(adapter);
	}
	
	public FunctionMeta getItemAtPos(int pos){
		if(pos > -1 && pos < mListView.getAdapter().getCount()){
			return (FunctionMeta) mListView.getAdapter().getItem(pos);
		}
		else{
			return null;
		}
	}
}
