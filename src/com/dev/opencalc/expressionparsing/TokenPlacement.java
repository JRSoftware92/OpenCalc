package com.dev.opencalc.expressionparsing;

/**
 * Tuple class for properly placing a tokenized string portion.
 * @author John Riley
 *
 */
public class TokenPlacement implements Comparable<TokenPlacement> {

	public final Token token;
	public final int index;
	
	public TokenPlacement(Token token, int i){
		this.token = token;
		this.index = i;
	}

	@Override
	public int compareTo(TokenPlacement other) {
		if (this.index < other.index){
			return -1;
	    }
	    if (this.index > other.index){
	    	return 1;
	    }
	    return 0;
	}
}
