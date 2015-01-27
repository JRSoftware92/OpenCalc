package com.dev.opencalc.exception;

/**
 * 
 * Calculation Exception for misplaced commas and periods.
 * @author John Riley
 * 
 */
public class MisplacedSeparatorException extends CalculationException{

	private static final long serialVersionUID = 1L;
	
	public MisplacedSeparatorException(){
		super("Misplaced Separator");
	}
	
	public MisplacedSeparatorException(char c){
		super("Misplace Separator: '" + Character.toString(c) + "'");
	}
}
