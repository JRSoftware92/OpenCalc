package com.dev.opencalc.activity;

import java.util.HashMap;

import com.dev.opencalc.R;
import com.dev.opencalc.adapter.ExpandableFunctionAdapter.OnValueChangedListener;
import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.expressionparsing.ParameterToken;
import com.dev.opencalc.expressionparsing.VariableTokenizer;
import com.dev.opencalc.fragment.LineGraphFragment;
import com.dev.opencalc.fragment.ParameterListFragment;
import com.dev.opencalc.fragment.SimpleTextFragment;
import com.dev.opencalc.fragment.SupportFragment;
import com.dev.opencalc.fragment.SupportListFragment.OnListItemClickListener;
import com.dev.opencalc.model.FunctionMeta;
import com.dev.opencalc.model.FunctionSeries;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * <--------------------------------CURRENTLY NOT WORKING------------------------------->
 * Activity for charting basic two dimensional functions.
 * @author John Riley
 * 
 */
public class GraphActivity extends SupportFragmentActivity implements OnValueChangedListener, 
																	  OnListItemClickListener{
	private VariableTokenizer mVarTokenizer = null;
	
	private String mCurrentEntry = "";
	private FunctionMeta mFunction = null;
	private ParameterToken[] mParams = null;
	private int mActiveAxis = 0;
	
	private SupportFragment mGraphFragment = null;
	private SupportFragment mListFragment = null;
	private EditText mExprText = null;
	private Button mImport = null;
	
	//TODO Offer an option to display a table of results based on:
	//Delta, Number of results, and Initial entry value.

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id == R.id.action_back_to_calculator){
			Intent i = new Intent(this, CalculatorActivity.class);
			i.putExtra(CalculatorActivity.EXPR_KEY, mCurrentEntry);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getLayoutId() { return R.layout.activity_graph; }

	@Override
	protected int getMenuId() { return R.menu.graph; }

	@Override
	protected void initializeDefaultActivity() {
		mVarTokenizer = new VariableTokenizer();
		//mDb = new CalculatorDatabase(this);
		mExprText = (EditText) findViewById(R.id.graphFunctionExprText);
		mImport = (Button) findViewById(R.id.graphFunctionImportButton);
		mGraphFragment = SimpleTextFragment.newInstance("No data available to display.");
		mListFragment = ParameterListFragment.newInstance(mParams, mActiveAxis);
		
		addFragment(R.id.graphFragmentContainer, mGraphFragment);
		addFragment(R.id.functionListContainer, mListFragment);
		mImport.setOnClickListener(DEFAULT_IMPORT_LISTENER);
		mExprText.addTextChangedListener(DEFAULT_EXPRESSION_LISTENER);
	}

	@Override
	protected void initializeActivity(Bundle args) {
		initializeDefaultActivity();
		if(args.containsKey(CalculatorActivity.EXPR_KEY)){
			mCurrentEntry = args.getString(CalculatorActivity.EXPR_KEY);
		}
	}
	
	@Override
	public void onListItemClick(int pos, long id) {
		// TODO Dialog for requested parameter
	}
	
	@Override
	public void onParameterValueUpdated(int funcPos, int paramPos, double value) {
		mParams[paramPos] = new ParameterToken(mParams[paramPos], value);
		refresh();
	}
	
	@Override
	public void onPrimaryAxisSet(int funcPos, int paramPos) {
		mActiveAxis = paramPos;
		refresh();
	}
	
	/**
	 * 
	 * @return String entry passed from calculator activity.
	 */
	public String getCurrentEntry(){
		return mCurrentEntry;
	}
	
	/**
	 * Refreshes the activity with the current data.
	 */
	public void refresh(){
		Log.d("GraphActivity", "Refreshing");
		updateFragment(mFunction, mParams, mActiveAxis);
	}
	
	/**
	 * Updates the fragment with the provided data.
	 * @param func - Function to be charted.
	 * @param params - Parameters of the function.
	 * @param axis - The index of the primary axis parameter (x-axis) of the function.
	 */
	private void updateFragment(FunctionMeta func, ParameterToken[] params, int axis){
		FunctionSeries series = getSeries(func, params, axis);
		
		Log.d("GraphActivity", "Initializing list fragment");
		mListFragment = ParameterListFragment.newInstance(params, axis);
		Log.d("GraphActivity", "Initializing graph fragment");
		mGraphFragment = LineGraphFragment.newInstance(series);
		replaceFragment(R.id.functionListContainer, mListFragment);
		replaceFragment(R.id.graphFragmentContainer, mGraphFragment);
	}
	
	public void onImportFunctionClick(View v){
		//TODO Figure out how to look up function
	}
	
	/**
	 * Called when the expression edit text value has been changed:
	 * Re tokenizes the expression for new parameters and charts the newly entered expression.
	 * @param text
	 */
	public void onExpressionUpdated(String text){
		if(text == null || text.equals("")){
			Log.d("Graph Activity", "No text to display.");
		}
		else{
			try {
				Log.d("GraphActivity", "Tokenizing variables from expression");
				mVarTokenizer.tokenize(text);
				Log.d("GraphActivity", "Tokenized");
				mParams = mVarTokenizer.getParameters();
				char[] c = new char[mParams.length];
				for(int i = 0; i < mParams.length; i++){
					c[i] = mParams[i].getStringParameter().charAt(0);
				}
				mFunction = new FunctionMeta("", "", text, c);
				refresh();
			} 
			catch (CalculationException e) {
				Log.e("GraphActivity", e.getMessage());
			}
		}
	}
	
	/**
	 * Returns a function series based on the provided values.
	 * @param func
	 * @param params
	 * @param primaryAxisIndex
	 * @return
	 */
	public static FunctionSeries getSeries(FunctionMeta func, ParameterToken[] params, int primaryAxisIndex){
		HashMap<String, Double> valueMap = new HashMap<String, Double>();
		for(int i = 0; i < params.length; i++){
			valueMap.put(params[i].getStringParameter(), params[i].getValue());
		}
		Log.d("GraphActivity", "Before Builder");
		FunctionSeries series = new FunctionSeries.Builder(func, valueMap)
			.setDelta(1).setNumResults(5).setStartingValue(0)
			.setPrimaryAxisByIndex(primaryAxisIndex)
			.toSeries();
		Log.d("GraphActivity", "After Builder");
		
		return series;
	}
	
	/**
	 * Standard onValueChangedListener for the expression edit text.
	 */
	private final TextWatcher DEFAULT_EXPRESSION_LISTENER = new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			//TODO
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			//TODO
		}

		@Override
		public void afterTextChanged(Editable s) {
			onExpressionUpdated(s.toString());
		}
		
	};
	
	private final OnClickListener DEFAULT_IMPORT_LISTENER = new OnClickListener(){
		@Override
		public void onClick(View v) {
			onImportFunctionClick(v);
		}
	};
}
