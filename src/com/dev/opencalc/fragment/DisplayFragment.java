package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.interfaces.CalculatorListener;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * 
 * A Display Fragment for the calculator activity.
 * @author John Riley
 * 
 */
public class DisplayFragment extends SupportFragment {
	private static final String ENTRY_KEY = "com.dev.memorycalc.entry";
	private static final String RESULT_KEY = "com.dev.memorycalc.result";
	
	private CalculatorListener mListener;

	private String mEntry = "";
	private String mResult = "";
	
	private TextView mExpressionDisplay;
	private TextView mResultDisplay;
	private Button mEqualsButton, mClearButton, mBackSpaceButton;

	public static DisplayFragment newInstance(String entry) {
		DisplayFragment fragment = new DisplayFragment();
		Bundle args = new Bundle();
		args.putString(ENTRY_KEY, entry);
		fragment.setArguments(args);
		return fragment;
	}
	
	public static DisplayFragment newInstance(String entry, String res) {
		DisplayFragment fragment = new DisplayFragment();
		Bundle args = new Bundle();
		args.putString(ENTRY_KEY, entry);
		args.putString(RESULT_KEY, res);
		fragment.setArguments(args);
		return fragment;
	}
	
	public DisplayFragment(){}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (CalculatorListener) activity;
		} 
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement CalculatorListener");
		}
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	protected int getLayoutId() { return R.layout.fragment_display; }

	@Override
	protected void initializeDefaultFragment(View rootView) {
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "pocket_calcuatlor_tt.ttf");
		mResultDisplay = (TextView) rootView.findViewById(R.id.calculatorResultDisplay);
		mExpressionDisplay = (TextView) rootView.findViewById(R.id.calculatorExpressionDisplay);
		mEqualsButton = (Button) rootView.findViewById(R.id.keypadEqualsButton);
		mClearButton = (Button) rootView.findViewById(R.id.keypadClearButton);
		mBackSpaceButton = (Button) rootView.findViewById(R.id.keypadBackspaceButton);
		
		Log.d("Display Fragment", mExpressionDisplay.getTypeface().toString());
		mExpressionDisplay.setTypeface(Typeface.SANS_SERIF);
		mResultDisplay.setTypeface(font, Typeface.BOLD);

		mClearButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mListener.onKeyPadClearClick();
			}
		});
		
		mBackSpaceButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mListener.onKeyPadBackspaceClick();
			}
		});
		
		mEqualsButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mListener.onKeyPadEnterClick();
			}
		});
	}
	
	@Override
	protected void initializeFragment(View rootView, Bundle args){
		initializeDefaultFragment(rootView);
		
		if(args.containsKey(ENTRY_KEY)){
			mEntry = args.getString(ENTRY_KEY);
			mExpressionDisplay.setText(mEntry);
		}
		if(args.containsKey(RESULT_KEY)){
			mResult = args.getString(RESULT_KEY);
			mResultDisplay.setText(mResult);
		}
	}
	
	public String getDisplayText(){ return mEntry; }
	
	public String getResultText(){ return mResult; }
	
	public void setDisplayText(String expr){
		mEntry = expr;
		if(mExpressionDisplay != null){
			mExpressionDisplay.setText(expr);
		}
	}
	
	public void setDisplayText(String expr, String res){
		mEntry = expr;
		if(mExpressionDisplay != null){
			mExpressionDisplay.setText(expr);
		}
		if(mResultDisplay != null){
			mResultDisplay.setText(res);
		}
	}
}
