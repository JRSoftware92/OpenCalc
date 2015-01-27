package com.dev.opencalc.expressionparsing;

import com.dev.opencalc.exception.CalculationException;

/**
 * 
 * A token representing a calculatable function.
 * @author John Riley
 * 
 */
public abstract class FunctionToken extends Token {

	public final int numArgs;
	
	public FunctionToken(String token, int args) {
		super(token, TokenType.Function);
		numArgs = args;
	}
	
	public abstract double calculate(double[] args) throws CalculationException;
}
