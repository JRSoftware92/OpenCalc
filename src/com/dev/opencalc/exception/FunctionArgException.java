package com.dev.opencalc.exception;

import com.dev.opencalc.expressionparsing.FunctionToken;

/**
 * 
 * Calculation Exception for incorrect function argument entries.
 * @author John Riley
 */
public class FunctionArgException extends CalculationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FunctionArgException(){
		super("Improper Arguments provided.");
	}
	
	public FunctionArgException(FunctionToken func){
		super("Improper number of arguments (" + func.numArgs + ") provided to " + func.toString());
	}
}
