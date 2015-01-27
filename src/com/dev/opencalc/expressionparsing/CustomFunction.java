package com.dev.opencalc.expressionparsing;

import java.util.HashMap;

import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.exception.FunctionArgException;

/**
 * 
 * A token representing a custom function entered by the user.
 * @author John Riley
 * 
 */
public class CustomFunction extends FunctionToken {
	
	private final char[] mParams;
	
	private static HashMap<String, CustomFunction> mMap;
	
	static{
		mMap = new HashMap<String, CustomFunction>();
	}
	
	private final String mExpression;

	public CustomFunction(String token, char[] args, String expr) {
		super(token, args.length);
		mExpression = expr;
		mParams = args;
	}
	
	public String getExpression(){
		return mExpression;
	}
	
	public char[] getParameterArray(){
		return mParams;
	}

	public static void registerFunction(CustomFunction func){
		if(!mMap.containsKey(func.mStrToken)){
			mMap.put(func.mStrToken, func);
		}
	}
	
	public static void unregisterFunction(String token){
		if(mMap.containsKey(token)){
			mMap.remove(token);
		}
	}
	
	public static void updateFunction(String token, CustomFunction func){
		if(mMap.containsKey(token)){
			mMap.remove(token);
		}
		mMap.put(token, func);
	}
	
	public static boolean isValidToken(String token){
		return mMap.containsKey(token);
	}
	
	public static CustomFunction getToken(String token){
		return mMap.get(token);
	}
	
	public static HashMap<String, CustomFunction> getTokenMap(){
		return mMap;
	}

	@Override
	public double calculate(double[] args) throws CalculationException {
		if(args == null || args.length != this.numArgs){
			throw new FunctionArgException(this);
		}
		else{
			ParameterToken[] params = new ParameterToken[numArgs];
			for(int i = 0; i < mParams.length; i++){
				params[i] = new ParameterToken(Character.toString(mParams[i]), args[i]);
			}
			ExpressionTokenizer tokenizer = new ExpressionTokenizer(mExpression, params);
			Interpreter interpreter = new Interpreter(tokenizer.getOutput());
			return interpreter.getResult();
		}
	}
}
