package com.dev.opencalc.activity;

import com.dev.opencalc.R;
import com.dev.opencalc.fragment.ContextPadFragment;
import com.dev.opencalc.fragment.DisplayFragment;
import com.dev.opencalc.fragment.KeypadFragment;
import com.dev.opencalc.interfaces.CalculatorListener;
import com.dev.opencalc.model.Calculation;
import com.dev.opencalc.model.CalculatorDatabase;
import com.dev.opencalc.model.FunctionMeta;
import com.dev.opencalc.thread.CalculationHandler;
import com.dev.opencalc.thread.CalculationRunnable;
import com.dev.opencalc.utils.AppUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * Primary Calculator activity for entering and displaying calculations.
 * @author John Riley
 * 
 */
public class CalculatorActivity extends SupportFragmentActivity implements CalculatorListener {
	
	public static final String EXPR_KEY = "com.dev.memorycalc.currentexpression";
	
	private int mCurrKeypad = 0;
	private boolean bCalculated = false;
	private String mCurrentEntry = "";
	private Calculation mLastCalculation = null;
	
	private ContextPadFragment mContextPad;
	private KeypadFragment mKeypad;
	private DisplayFragment mCalcDisplay;
	
	private CalculationHandler mHandler;
	private CalculatorDatabase mDb;
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.action_graph_menu) {
			Toast.makeText(this, "Currently undergoing development. Coming soon\u2026", 
					Toast.LENGTH_SHORT).show();
			//startActivity(new Intent(this, GraphActivity.class));
		} else if (id == R.id.action_show_calculation_history) {
			startActivity(new Intent(this, HistoryActivity.class));
		} else if (id == R.id.action_credits){
			startActivity(new Intent(this, CreditsActivity.class));
		} else if (id == R.id.action_feedback){
			startActivity(new Intent(this, FeedbackActivity.class));
		} else if (id == R.id.action_rating){
			startActivity(AppUtils.getPlayStoreIntent(getPackageName()));
		} else{}
        return super.onOptionsItemSelected(item);
    }
    
	@Override
	protected int getLayoutId() { return R.layout.activity_calculator; }

	@Override
	protected int getMenuId() { return R.menu.calculator; }
	
	@Override
	protected void initializeActivity(Bundle args) {
		initializeDefaultActivity();
		if(args.containsKey(EXPR_KEY)){
			mCurrentEntry = args.getString(EXPR_KEY);
			mCalcDisplay.setDisplayText(mCurrentEntry);
		}
	}

	@Override
	protected void initializeDefaultActivity() {
        mDb = new CalculatorDatabase(this);
        
        mCalcDisplay = DisplayFragment.newInstance(mCurrentEntry);
        mKeypad = KeypadFragment.newInstance();
        mContextPad = ContextPadFragment.newInstance();
        mHandler = new CalculationHandler(mCalcDisplay);
        
        beginTransaction().add(R.id.calculatorDisplayContainer, mCalcDisplay)
            .add(R.id.calculatorKeypadContainer, mKeypad)
            .add(R.id.calculatorContextContainer, mContextPad)
            .commit();
	}

	/**
	 * Called on equals click: Attempts to calculate the entry if possible.
	 */
    private void handleCalculation(){
    	CalculationRunnable runnable = new CalculationRunnable(mHandler, 
    			mCurrentEntry, mLastCalculation);
    	Thread thread = new Thread(runnable);
    	thread.start();
    	
    	mDb.insertCalculation(mLastCalculation);
		bCalculated = true;
    }

    //------------------------Calculator Listener Methods--------------------------//


	@Override
	public void onContextPadClick(int id) {
		if(id == R.id.keypadCustomButton){
			Intent i = new Intent(this, FunctionListActivity.class);
			i.putExtra(EXPR_KEY, mCurrentEntry);
			startActivity(i);
		}
		else if(id == R.id.keypadGeometryButton){
			Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
		}
		else{
			mCurrKeypad = id;
			mKeypad = KeypadFragment.newInstance(id);
			replaceFragment(R.id.calculatorKeypadContainer, mKeypad);
		}
	}
    
	@Override
	public void onKeyPadClearClick() {
		bCalculated = false;
		mCurrentEntry = "";
		mCalcDisplay.setDisplayText("", "");
	}


	@Override
	public void onKeyPadBackspaceClick() {
		bCalculated = false;
		if(mCurrentEntry.length() > 0){
			mCurrentEntry = mCurrentEntry.substring(0, mCurrentEntry.length() - 1);
			mCalcDisplay.setDisplayText(mCurrentEntry);
		}
	}


	@Override
	public void onKeyPadEntry(String entry) {
		if(bCalculated){
			onKeyPadClearClick();
			bCalculated = false;
		}
		mCurrentEntry = mCurrentEntry + entry;
		mCalcDisplay.setDisplayText(mCurrentEntry);
		if(mCurrKeypad != R.id.keypadDefaultButton){
			mKeypad = KeypadFragment.newInstance(R.id.keypadDefaultButton);
			replaceFragment(R.id.calculatorKeypadContainer, mKeypad);
		}
	}

	@Override
	public void onKeyPadEnterClick() {
		if(!bCalculated && mCurrentEntry.length() > 0){
			handleCalculation();
		}
	}


	@Override
	public FunctionMeta getHotFunction(int pos) {
		if(mDb != null && pos > -1 && pos < 10){
			return mDb.getFunctionByHotkey(pos);
		}
		else{
			return null;
		}
	}
}
