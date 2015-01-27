package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.adapter.ParameterListAdapter;
import com.dev.opencalc.expressionparsing.ParameterToken;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

/**
 * 
 * For displaying an array of parameters with their set values.
 * @author John Riley
 *
 */
public class ParameterListFragment extends SupportListFragment<ParameterToken> {
	
	private static final String PARAM_KEY = "com.calculator.param",
								VALUE_KEY = "com.calculator.value",
								AXIS_KEY = "com.calculator.axis",
								TOKEN_KEY = "com.calculator.paramTokens";
	
	//private String[] mParams = null;
	//private double[] mValues = null;
	private int xAxis = 0;
	
	public static SupportFragment newInstance(char[] params, double[] values, int xAxis){
		SupportFragment fragment = new ParameterListFragment();
		if(params != null && values != null && params.length > 0 && values.length == params.length){
			Bundle args = new Bundle();
			String[] arr = new String[params.length];
			for(int i = 0; i < params.length; i++){
				arr[i] = Character.toString(params[i]);
			}
			
			args.putStringArray(PARAM_KEY, arr);
			args.putDoubleArray(VALUE_KEY, values);
			args.putInt(AXIS_KEY, xAxis);
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	public static SupportFragment newInstance(String[] params, double[] values, int xAxis){
		SupportFragment fragment = new ParameterListFragment();
		if(params != null && values != null && params.length > 0 && params.length == values.length){
			Bundle args = new Bundle();
			
			args.putStringArray(PARAM_KEY, params);
			args.putDoubleArray(VALUE_KEY, values);
			args.putInt(AXIS_KEY, xAxis);
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	public static SupportFragment newInstance(ParameterToken[] tokens, int xAxis){
		SupportFragment fragment = new ParameterListFragment();
		if(tokens != null && tokens.length > 0){
			Bundle args = new Bundle();
			
			args.putParcelableArray(TOKEN_KEY, tokens);
			args.putInt(AXIS_KEY, xAxis);
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	public ParameterListFragment(){}

	@Override
	protected int getListViewId() { return R.id.simpleListView; }

	@Override
	protected int getLayoutId() { return R.layout.fragment_simple_list; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		ParameterListAdapter adapter = null;
		xAxis = args.getInt(AXIS_KEY);
		if(args.containsKey(PARAM_KEY) && args.containsKey(VALUE_KEY)){
			String[] mParams = args.getStringArray(PARAM_KEY);
			double[] mValues = args.getDoubleArray(VALUE_KEY);
			adapter = new ParameterListAdapter(rootView.getContext(), mParams, mValues, xAxis);
		}
		if(args.containsKey(TOKEN_KEY)){
			ParameterToken[] arr = (ParameterToken[]) args.getParcelableArray(TOKEN_KEY);
			adapter = new ParameterListAdapter(rootView.getContext(), arr, xAxis);
		}
		Log.d("ParameterListFragment", "Initializing adapter");
		setListAdapter(adapter);
	}

	@Override
	protected ArrayAdapter<ParameterToken> getAdapter(ParameterToken[] data) {
		return new ParameterListAdapter(getActivity(), data, 0);
	}
}
