package com.dev.opencalc.expressionparsing;

import java.util.HashSet;
import java.util.LinkedList;

import com.dev.opencalc.exception.CalculationException;

/**
 * Extracts unlinked variables from an expression.
 * @author John Riley
 *
 */
public class VariableTokenizer extends Tokenizer {
	
	protected HashSet<String> mParameterSet = null;

	public VariableTokenizer() {
		super();
		mParameterSet = new HashSet<String>();
		initializeMetaData();
	}
	
	@Override
	public void tokenize(String entry) throws CalculationException {
		super.tokenize(trimExpression(entry));
		if(mOutput != null && mOutput.size() > 0){
			mParameterSet = toVariableSet(mOutput);
		}
		else{
			throw new CalculationException("Tokenization error");
		}
	}

	@Override
	public Token toToken(String token, TokenType type){
		Token result;
		
		switch(type){
		case Variable:
			result = new ParameterToken(token, 0);
			break;
		case Left_Parenthesis:
			result = SpecialToken.LEFT_PAREN;
			break;
		default:
			result = ValueToken.ZERO;
			break;
		}
		return result;
	}
	
	public ParameterToken[] getParameters(){
		return getParameterArray(mParameterSet);
	}
	
	public ParameterToken[] getNewParameters(HashSet<String> oldSet){
		return getParameterArray(getNewVariableSet(oldSet));
	}
	
	public HashSet<String> getParameterSet(){ return mParameterSet; }
	
	public HashSet<String> getNewVariableSet(HashSet<String> oldSet){
		if(oldSet != null && oldSet.size() > 0){
			HashSet<String> newSet = setCompliment(oldSet, mParameterSet);
			return newSet;
		}
		else{
			return mParameterSet;
		}
	}
	
	public boolean containsToken(String token){ return mParameterSet.contains(token); }
	
	public void clear(){ mParameterSet.clear(); }
	
	private void initializeMetaData(){
		add("\\(", TokenType.Left_Parenthesis);
		add("[a-df-zA-Z]{1}", TokenType.Variable);
		add("([ -@\\[-`\\{-~])+", TokenType.NULL);
		add("[e]", TokenType.NULL);
	}
	
	private static HashSet<String> toVariableSet(LinkedList<Token> list){
		Token sample = null, lookAhead = null;
		HashSet<String> set = new HashSet<String>();
		while(!list.isEmpty()){
			sample = list.remove();
			if(sample instanceof ParameterToken){
				lookAhead = list.poll();
				if(lookAhead == null || !isLeftParenthesis(lookAhead)){
					set.add(sample.mStrToken);
				}
			}
		}
		
		return set;
	}
	
	private static ParameterToken[] getParameterArray(HashSet<String> set){
		if(set == null || set.size() < 1){
			return null;
		}
		else{
			ParameterToken[] arr = new ParameterToken[set.size()];
			int count = set.size();
			for(String s : set){
				arr[--count] = new ParameterToken(s, 0);
			}
			return arr;
		}
	}
	
	private static String trimExpression(String expr){
		String temp = expr.replaceAll("\\s+", "")
				  .replaceAll("\\(-", "(0-")
				  .replaceAll(",-", ",0-");
		return temp;
	}
	
	private static boolean isLeftParenthesis(Token token){
		return token.mType.equals(SpecialToken.LEFT_PAREN.mType);
	}
	
	private static HashSet<String> setCompliment(HashSet<String> a, HashSet<String> b){
		HashSet<String> newSet = a;
		for(String s : a){
			if(b.contains(s)){
				newSet.remove(s);
			}
		}
		return newSet;
	}
}
