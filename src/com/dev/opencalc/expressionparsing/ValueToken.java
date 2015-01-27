package com.dev.opencalc.expressionparsing;

import com.dev.opencalc.utils.MathUtils;

/**
 * 
 * Basic token containing a raw computable value.
 * @author John Riley
 * 
 */
public class ValueToken extends Token {
	
	public static final ValueToken ZERO = new ValueToken("0", 0);
	public static final ValueToken E = new ValueToken("e", MathUtils.E);
	public static final ValueToken PI = 
			new ValueToken(Character.toString(MathUtils.PI_CHAR), MathUtils.PI);
	
	protected double mValue = 0;

	public ValueToken(String token, double value) {
		super(token, TokenType.Value);
		mValue = value;
	}
	
	public double getValue(){
		return mValue;
	}
}
