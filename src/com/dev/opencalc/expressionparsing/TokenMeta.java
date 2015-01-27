package com.dev.opencalc.expressionparsing;

import java.util.regex.Pattern;

/**
 * Meta data class for a token classification.
 * @author John Riley
 *
 */
public class TokenMeta {
	
	public final TokenType tokenType;
	public final Pattern regex;

	public TokenMeta(String regex, TokenType type) {
		tokenType = type;
		if(regex != null){
			this.regex = Pattern.compile(regex);
		}
		else{
			this.regex = null;
		}
	}
}
