package com.dev.opencalc.exception;

/**
 * Calculation exception for improper calculations.
 * @author John Riley
 *
 */
public class InvalidCalculationException extends CalculationException {

	private static final long serialVersionUID = 1L;
	
	public InvalidCalculationException(){
		super("Invalid Calculation");
	}
	
	public InvalidCalculationException(String str){
		super("Invalid Calculation: " + str);
	}

}
