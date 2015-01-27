package com.dev.opencalc.exception;

/**
 * 
 * Calculation Exception for an invalid value entry.
 * @author John Riley
 * 
 */
public class InvalidValueException extends CalculationException{

	private static final long serialVersionUID = 1L;

	public InvalidValueException(){
		super("Invalid value entered.");
	}
	
	public InvalidValueException(String val){
		super("Invalid value entered: " + val);
	}
}
