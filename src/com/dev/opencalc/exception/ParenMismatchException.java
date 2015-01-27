package com.dev.opencalc.exception;

/**
 * 
 * Calculation exception for mismatched parenthesis in an expression.
 * @author John Riley
 * 
 */
public class ParenMismatchException extends CalculationException {

	private static final long serialVersionUID = 1L;

	public ParenMismatchException(){
		super("Mismatched Parenthesis");
	}
	
	public ParenMismatchException(boolean bRight){
		super("Missing " + (bRight ? "right" : "left") +" parenthesis");
	}
}
