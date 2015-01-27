package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.interfaces.CalculatorListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * Fragment for setting the keypad context.
 * @author John Riley
 * 
 */
public class ContextPadFragment extends SupportFragment {
	
	private Button mDefault = null, mAlgebra = null, mGeometry = null, mTrig = null, mCustom = null;

	private CalculatorListener mListener = null;
	
	public static ContextPadFragment newInstance() { return new ContextPadFragment(); }
	
	public ContextPadFragment(){}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (CalculatorListener) activity;
		} catch (ClassCastException e) {
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
	protected int getLayoutId() { return R.layout.fragment_context_pad; }

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mDefault = (Button) rootView.findViewById(R.id.keypadDefaultButton);
		mAlgebra = (Button) rootView.findViewById(R.id.keypadAlgebraButton);
		mGeometry = (Button) rootView.findViewById(R.id.keypadGeometryButton);
		mTrig = (Button) rootView.findViewById(R.id.keypadTrigButton);
		mCustom = (Button) rootView.findViewById(R.id.keypadCustomButton);
		
		Button.OnClickListener listener = new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				onButtonPressed(v);
			}
		};
		mAlgebra.setOnClickListener(listener);
		mGeometry.setOnClickListener(listener);
		mTrig.setOnClickListener(listener);
		mCustom.setOnClickListener(listener);
		mDefault.setOnClickListener(listener);
	}
	
	@Override
	protected void initializeFragment(View rootView, Bundle args){
		args = null;
		initializeDefaultFragment(rootView);
	}

	public void onButtonPressed(View view) {
		if (mListener != null) {
			mListener.onContextPadClick(view.getId());
		}
	}
}
