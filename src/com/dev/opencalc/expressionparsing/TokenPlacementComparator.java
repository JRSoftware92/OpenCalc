package com.dev.opencalc.expressionparsing;

import java.util.Comparator;

/**
 * Comparator for Token Placement.
 * @author John Riley
 *
 */
public class TokenPlacementComparator implements Comparator<TokenPlacement>{
	@Override
	public int compare(TokenPlacement x, TokenPlacement y){
		return x.compareTo(y);
	}
}
