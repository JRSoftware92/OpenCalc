package com.dev.opencalc.interfaces;

import com.dev.opencalc.model.FunctionMeta;

/**
 * 
 * Listener for major calculator button events.
 * @author John Riley
 * 
 */
public interface CalculatorListener {
	
	public FunctionMeta getHotFunction(int pos);
	
	public void onKeyPadEntry(String entry);
	public void onKeyPadEnterClick();
	public void onKeyPadClearClick();
	public void onKeyPadBackspaceClick();
	
	public void onContextPadClick(int id);
}
