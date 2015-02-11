package com.dev.opencalc.expressionparsing;

import java.util.HashMap;

import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.exception.FunctionArgException;
import com.dev.opencalc.utils.MathUtils;

/**
 * 
 * A Basic token representing an operator.
 * @author John Riley
 * 
 */
public class OperatorToken extends FunctionToken {
	
	private enum Operator{
		PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/'), MODULO('%'), POW('^');
		
		private final char mToken;
		
		private Operator(){ mToken = ' '; }
		
		private Operator(char token){ mToken = token; }
	}
	
	private static final HashMap<String, OperatorToken> mMap;
	
	private final byte mPrecedence;
	private final boolean bIsRightAssoc;
	private final Operator mOperator;
	
	static{
		mMap = new HashMap<String, OperatorToken>();
		
		mMap.put("+", new OperatorToken(Operator.PLUS, (byte) 2, false));
		mMap.put("-", new OperatorToken(Operator.MINUS, (byte) 2, false));
		mMap.put("*", new OperatorToken(Operator.MULTIPLY, (byte) 3, false));
		mMap.put("/", new OperatorToken(Operator.DIVIDE, (byte) 3, false));
		mMap.put("%", new OperatorToken(Operator.MODULO, (byte) 3, false));
		mMap.put("^", new OperatorToken(Operator.POW, (byte) 4, true));
	}
	
	public OperatorToken(Operator op, byte prec, boolean bRightAssoc) {
		super(Character.toString(op.mToken), 2);
		mOperator = op;
		mPrecedence = prec;
		bIsRightAssoc = bRightAssoc;
	}
	
	public byte getPrecedence(){
		return mPrecedence;
	}
	
	public boolean isRightAssociative(){
		return bIsRightAssoc;
	}
	
	public static boolean isValidToken(String token){
		return mMap.containsKey(token);
	}
	
	public static OperatorToken getOperator(String token){
		return mMap.get(token);
	}
	
	public static HashMap<String, OperatorToken> getTokenMap(){
		return mMap;
	}

	@Override
	public double calculate(double[] params) throws CalculationException {
		if(params.length == 2){
			switch(mOperator){
			case PLUS: return params[0] + params[1];
			case MINUS: return params[0] - params[1];
			case MULTIPLY: return params[0] * params[1];
			case MODULO: return params[0] % params[1];
			case POW: return MathUtils.pow(params[1], params[2]);
			case DIVIDE:
				if(params[1] != 0){
					return params[0] / params[1];
				}
				else{
					throw new CalculationException("Division by zero");
				}
			default: 
				throw new CalculationException("Invalid operator: " + mStrToken); 
			}
		}
		else if(params.length == 1){
			if(mStrToken.equals("-")){
				return params[0] * -1d;
			}
			else{
				throw new FunctionArgException(this);
			}
		}
		else{
			throw new FunctionArgException(this);
		}
	}
}
