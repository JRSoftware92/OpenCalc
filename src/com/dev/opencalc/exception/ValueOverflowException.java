package com.dev.opencalc.exception;

/**
 * 
 * Calculation Exception for values that exceed the memory format.
 * @author John Riley
 * 
 */
public class ValueOverflowException extends CalculationException {

	private static final long serialVersionUID = 1L;

	public ValueOverflowException(){
		super("Value Overflow");
	}
}
