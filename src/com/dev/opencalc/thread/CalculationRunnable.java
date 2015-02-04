package com.dev.opencalc.thread;

import android.os.Bundle;
import android.os.Message;

import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.expressionparsing.ExpressionTokenizer;
import com.dev.opencalc.expressionparsing.Interpreter;
import com.dev.opencalc.model.Calculation;

/**
 * Asynchronously calculates a given expression and outputs the result to a message handler.
 * @author John Riley
 *
 */
public class CalculationRunnable implements Runnable {
	
	private final String entry;
	private final Calculation lastCalculation;
	private final CalculationHandler handler;

	public CalculationRunnable(CalculationHandler handler, String expr, Calculation prev) {
		entry = expr;
		lastCalculation = prev;
		this.handler = handler;
	}

	@Override
	public void run() {
		ExpressionTokenizer tokenizer = new ExpressionTokenizer();
		Interpreter interpreter = new Interpreter();
		Calculation result = null;
		try {
			if(lastCalculation != null && !lastCalculation.isError()){
				tokenizer.tokenize(entry, lastCalculation.getDoubleResult());
			}
			else{
				tokenizer.tokenize(entry);
			}
			interpreter.interpret(tokenizer.getOutput());
			
			result = new Calculation(entry, interpreter.getStringResult());
		} 
		catch(CalculationException ce){
			String str = ce.getMessage();
			result = new Calculation(entry, str == null ? "Unknown Error" : str);
		}
		catch (Exception e) {
			result = new Calculation(entry, "Unknown Error");
		}
		
		if(handler != null){
			Message msg = handler.obtainMessage();
			Bundle args = new Bundle();
			args.putParcelable(CalculationHandler.RESULT_KEY, result);
			msg.setData(args);
			handler.sendMessage(msg);
		}
	}
}
