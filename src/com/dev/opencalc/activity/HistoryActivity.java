package com.dev.opencalc.activity;

import java.util.ArrayList;

import com.dev.opencalc.R;
import com.dev.opencalc.fragment.HistoryFragment;
import com.dev.opencalc.fragment.SupportFragment;
import com.dev.opencalc.model.Calculation;
import com.dev.opencalc.model.CalculatorDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * Activity for viewing and interacting with HistoryFragment
 * @author John Riley
 * 
 */
public class HistoryActivity extends SupportFragmentActivity  implements HistoryFragment.OnListItemClickListener {
	
	private String mCurrentEntry = "";
	
	private CalculatorDatabase mDb = null;
	
	private SupportFragment mFragment;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id == R.id.action_clear_history){
			long count = mDb.clearHistory();
			Toast.makeText(this, count + " entries deleted.", Toast.LENGTH_SHORT).show();
			updateHistory(mDb.getCalculationHistory());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected int getLayoutId() { return R.layout.activity_fragment; }

	@Override
	protected int getMenuId() { return R.menu.history; }

	@Override
	protected void initializeDefaultActivity() {	
		Calculation[] history = null;
		mDb = new CalculatorDatabase(this);
		
		if(mDb != null){
			history = mDb.getCalculationHistory();
		}
		
		//TODO Set a default fragment if history isn't working.
		mFragment = HistoryFragment.newInstance(history);
		addFragment(R.id.fragmentContainer, mFragment);
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
		Calculation calculation = ((HistoryFragment)mFragment).getItem(pos);
		mCurrentEntry = calculation.getStringStatement();
		toCalculatorActivity();
	}
	
	/**
	 * 
	 * @return String expression passed from CalculatorActivity
	 */
	public String getCurrentEntry(){ return mCurrentEntry; }
	
	/**
	 * Called on selection of Delete context menu option.
	 * Deletes the calculation from the given position in the list.
	 * @param pos - Index of the calculation to be deleted.
	 */
	public void onDeleteCalculation(int pos){
		Calculation[] arr = mDb.getCalculationHistory();
		if(pos < arr.length){
			ArrayList<Calculation> list = new ArrayList<Calculation>(arr.length);
			for(int i = 0; i < arr.length; i++){
				if(i != pos){
					list.add(arr[i]);
				}
			}
			arr = new Calculation[list.size()];
			updateHistory(list.toArray(arr));
		}
	}
	
	/**
	 * Updates the fragment with the provided array of calculation history.
	 * @param calculationHistory - Array of calculation objects to be displayed.
	 */
	public void updateHistory(Calculation[] calculationHistory){
		if(mFragment == null || !(mFragment instanceof HistoryFragment)){
			mFragment = HistoryFragment.newInstance(calculationHistory);
			addFragment(R.id.graphFragmentContainer, mFragment);
		}
		else{
			((HistoryFragment)mFragment).setList(calculationHistory);
		}
	}
	
	/**
	 * Intent to return to the primary calculator activity.
	 */
	private void toCalculatorActivity(){
		Intent i = new Intent(this, CalculatorActivity.class);
		i.putExtra(CalculatorActivity.EXPR_KEY, mCurrentEntry);
		startActivity(i);
	}
}
