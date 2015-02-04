package com.dev.opencalc.thread;

import com.dev.opencalc.fragment.DisplayFragment;
import com.dev.opencalc.model.Calculation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Handles asynchronous calculation results.
 * @author John Riley
 *
 */
public class CalculationHandler extends Handler {
	
	public static final String RESULT_KEY = "com.opencalc.result";
	
	private final DisplayFragment display;

	public CalculationHandler(DisplayFragment display){
		super();
		this.display = display;
	}
	
	@Override
	public void handleMessage(Message msg){
		Bundle args = msg.getData();
		Calculation result = args.getParcelable(RESULT_KEY);
		if(display != null){
			display.setDisplayText(result.getStringStatement(), result.getStringResult());
		}
	}
}
