package com.dev.opencalc.expressionparsing;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.regex.Matcher;

import com.dev.opencalc.exception.CalculationException;

/**
 * Generic tokenizer class for parsing a provided input string for tokens of a specific format.
 * @author John Riley
 *
 */
public abstract class Tokenizer {
	
	protected LinkedList<TokenMeta> mInfo;
	protected LinkedList<Token> mOutput;

	public Tokenizer() {
		mInfo = new LinkedList<TokenMeta>();
		mOutput = new LinkedList<Token>();
	}
	
	public LinkedList<Token> getOutput(){ return mOutput; }
	
	public void clear(){ mOutput.clear(); }
	
	public void clearMetaData() { mInfo.clear(); }

	public void add(String regex, TokenType type){
		mInfo.add(new TokenMeta(regex, type));
	}
	
	public void tokenize(String entry) throws CalculationException {
		String str = new String(entry);
		String temp = "";
		Matcher matcher = null;
		PriorityQueue<TokenPlacement> orderedTokens = new PriorityQueue<TokenPlacement>();
		clear();
		
		int startIndex = 0, endIndex = 0;
		for(TokenMeta meta : mInfo){
			matcher = meta.regex.matcher(str);
			while(matcher.find()){
				startIndex = matcher.start();
				endIndex = matcher.end();
				temp = str.substring(startIndex, endIndex);
				orderedTokens.add(new TokenPlacement(toToken(temp, meta.tokenType), startIndex));
			}
		}
		while(!orderedTokens.isEmpty()){
			mOutput.add(orderedTokens.remove().token);
		}
	}
	
	public abstract Token toToken(String token, TokenType type);
}