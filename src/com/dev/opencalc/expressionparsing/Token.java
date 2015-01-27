package com.dev.opencalc.expressionparsing;

/**
 * 
 * Basic class for interpretation of infix notation expressions.
 * @author John Riley
 * 
 */
public class Token {

	protected final TokenType mType;
	protected final String mStrToken;
	
	public Token(String token, TokenType type){ 
		mStrToken = token; 
		mType = type;
	}
	
	@Override
	public String toString(){
		return mStrToken;
	}
}
