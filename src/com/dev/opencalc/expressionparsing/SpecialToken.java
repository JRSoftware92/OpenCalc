package com.dev.opencalc.expressionparsing;

/**
 * 
 * A Special token with specific importance.
 * @author John Riley
 * 
 */
public class SpecialToken extends Token {
	
	public static final SpecialToken LEFT_PAREN = new SpecialToken("(", TokenType.Left_Parenthesis);
	public static final SpecialToken RIGHT_PAREN = new SpecialToken(")", TokenType.Right_Parenthesis);
	public static final SpecialToken DECIMAL = new SpecialToken(".", TokenType.Period);
	public static final SpecialToken COMMA = new SpecialToken(",", TokenType.Comma);

	public SpecialToken(String token, TokenType type){
		super(token, type);
	}
}
