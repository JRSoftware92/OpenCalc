package com.dev.opencalc.exception;

/**
 * 
 * Parent class of all calculation exceptions which occur during tokenization and interpretation.
 * @author John Riley
 * 
 */
public class CalculationException extends Exception {

	private static final long serialVersionUID = 1L;

	public CalculationException(){
		super("Calculation Exception");
	}
	
	public CalculationException(String msg){
		super(msg);
	}
}
