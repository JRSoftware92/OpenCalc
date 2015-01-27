package com.dev.opencalc.expressionparsing;

import java.util.LinkedList;
import java.util.Stack;

import android.util.Log;

import com.dev.opencalc.exception.*;

/**
 * 
 * Class for interpreting a list of tokens in post fix order.
 * @author John Riley
 * 
 */
public class Interpreter {
	
	private String mResult = "";
	
	/**
	 * Standard constructor.
	 */
	public Interpreter(){}
	
	/**
	 * Token Constructor
	 * @param tokens - RPN Tokens to be interpreted.
	 */
	public Interpreter(LinkedList<Token> tokens){
		this();
		try{
			interpret(tokens);
		}
		catch(CalculationException ce){
			Log.e("Interpreter", ce.getMessage());
			mResult = ce.getMessage();
		}
		catch(NumberFormatException nfe){
			Log.e("Interpreter", nfe.getMessage());
			mResult = "Invalid entry provided.";
		}
		catch(Exception e){
			Log.e("Interpreter", "Unknown Interpretation Error");
			mResult = "Unknown Error";
		}
	}
	
	/**
	 * Returns the result of the calculation in string format.
	 * @return - String result of the interpretation.
	 */
	public String getStringResult(){
		return mResult;
	}
	
	/**
	 * 
	 * @return Double result of the interpretation if possible.
	 * @throws NumberFormatException
	 */
	public double getResult() throws NumberFormatException {
		return Double.parseDouble(mResult);
	}

	/**
	 * Interprets the provided linked list of tokens if possible.
	 * @param tokens - RPN tokens to be interpreted.
	 * @throws CalculationException
	 */
	public void interpret(LinkedList<Token> tokens) throws CalculationException {
		Stack<Token> calcStack = new Stack<Token>();
		for (Token token : tokens) {
			Log.v("Interpreter", "Token being interpreted: " + token.mStrToken);
			if (token instanceof ValueToken) {
				calcStack.push(token);
			} 
			else if (token instanceof FunctionToken) {
				FunctionToken function = (FunctionToken) token;

				if (calcStack.size() < function.numArgs) {
					Log.v("Interpreter", tokens.size() + " tokens to look at.");
					mResult = new FunctionArgException(function).getMessage();
				}
				
				double[] params = new double[function.numArgs];
				for (int i = function.numArgs - 1; i > -1; i--) {
					Token paramToken = calcStack.pop();
					if (!(paramToken instanceof ValueToken)) {
						mResult = new InvalidValueException(paramToken.toString()).getMessage();
					}
					params[i] = ((ValueToken)paramToken).getValue();
				}
				
				double result = function.calculate(params);
				calcStack.push(new ValueToken(Double.toString(result), result));
			}
		}
		if (calcStack.size() != 1) {
			mResult = "Calculation Error";
		}
		Token finalToken = calcStack.pop();
		if (!(finalToken instanceof ValueToken)) {
			mResult = new InvalidCalculationException(finalToken.toString()).getMessage();
		}
		try{
			mResult = Double.toString(((ValueToken)finalToken).getValue());
		}
		catch(NumberFormatException nfe){
			mResult = "Result not a number.";
		}
	}
	
	/**
	 * Tokenizes and calculates the provided string expression.
	 * @param entry - String expression to be interpreted.
	 * @return Double value of the computed expression, if possible.
	 * @throws Exception
	 */
	public static double calculate(String entry) throws Exception {
		Log.v("Interpreter", "Initializing tokenizer for entry: " + entry);
		ExpressionTokenizer tokenizer = new ExpressionTokenizer(entry);
		Log.v("Interpreter", "Retrieving output queue");
		LinkedList<Token> tokens = tokenizer.getOutput();
		Log.v("Interpreter", "Interpreting output queue");
		
		Interpreter interpreter = new Interpreter();
		interpreter.interpret(tokens);
		return interpreter.getResult();
	}
}
