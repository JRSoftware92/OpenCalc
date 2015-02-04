package com.dev.opencalc.expressionparsing;

import java.util.HashMap;
import java.util.LinkedList;

import android.util.Log;

import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.exception.InvalidCalculationException;
import com.dev.opencalc.exception.InvalidValueException;
import com.dev.opencalc.exception.MisplacedSeparatorException;
import com.dev.opencalc.exception.ParenMismatchException;
import com.dev.opencalc.utils.MathUtils;

/**
 * Tokenizer class for tokenizing a calculatable expression if possible.
 * @author John Riley
 *
 */
public class ExpressionTokenizer extends Tokenizer {
	
	private static final String VALUE_REGEX = "((\\.)?(\\d)+(\\.\\d+)?)";
	private static final String GEOMETRY_REGEX = "([ACV]{1})([{]{1})(SQR|TRI|O){1}([}]{1})";
	private static final String ALGEBRA_REGEX = "[!\u221A\u221B]{1}";
	
	protected LinkedList<Token> mOpStack = null;
	protected HashMap<String, ParameterToken> mParameters = null;
	protected double mLastResult = 0;

	/**
	 * Standard Constructor
	 */
	public ExpressionTokenizer() {
		super();
		initializeMetaData();
		mOpStack = new LinkedList<Token>();
		mParameters = new HashMap<String, ParameterToken>();
	}
	
	/**
	 * Expression Constructor
	 * @param entry - String expression to be computed.
	 * @throws CalculationException
	 */
	public ExpressionTokenizer(String entry) throws CalculationException {
		this();
		tokenize(entry);
	}
	
	/**
	 * Parameter Constructor
	 * @param expr - String expression to be computed.
	 * @param params - Parameters in expression to be matched at run time.
	 * @throws CalculationException
	 */
	public ExpressionTokenizer(String expr, ParameterToken[] params) throws CalculationException{
		this();
		for(ParameterToken token : params){
			Log.d("ExpressionTokenizer", "Adding parameter token: " + token + " with value: " + token.mValue);
			mParameters.put(token.mStrToken, token);
		}
		tokenize(expr);
	}
	
	@Override
	public void tokenize(String entry) throws CalculationException {
		super.tokenize(trimExpression(entry));
		if(mOutput != null && mOutput.size() > 0){
			Log.d("Tokenizer", "Standard Output " + fromList(mOutput));
			mOutput = toRPN(mOutput);
		}
		else{
			throw new InvalidCalculationException();
		}
	}
	
	/**
	 * Tokenizes an expression with respect to the previou calculation.
	 * @param entry - String expression to be computed.
	 * @param lastCalculation - Result of the previous calculation, if needed.
	 * @throws CalculationException
	 */
	public void tokenize(String entry, double lastCalculation) throws CalculationException {
		mLastResult = lastCalculation;
		this.tokenize(entry);
	}

	/**
	 * Initializes token meta data based on their regex values.
	 */
	private void initializeMetaData(){
		add(VALUE_REGEX, TokenType.Value);
		add("[e]{1}", TokenType.Constant_e);
		add("[" + Character.toString(MathUtils.PI_CHAR) + "]{1}", TokenType.Constant_pi);
		add("[\\*\\+\\-\\/\\^\\%]{1}", TokenType.Operator);
		add("[\\(]{1}", TokenType.Left_Parenthesis);
		add("[\\)]{1}", TokenType.Right_Parenthesis);
		add("[a-df-zA-Z]+", TokenType.Function);
		add("[,]{1}", TokenType.Comma);
		//TODO May cause problems.
		//add("[.]{1}", TokenType.Period);
		add(GEOMETRY_REGEX, TokenType.Function);
		add(ALGEBRA_REGEX, TokenType.Function);
	}

	@Override
	public Token toToken(String token, TokenType type){
		Token result = null;
		
		switch(type){
		case Value:
			result = new ValueToken(token, Double.parseDouble(token));
			break;
		case Operator:
			result = OperatorToken.getOperator(token);
			break;
		case Function:
			if(PresetFunction.isValidToken(token)){
				Log.d("Tokenizer", "Found preset: " + token);
				result = PresetFunction.getToken(token);
				Log.d("Tokenizer", "Preset successful.");
			}
			else if (CustomFunction.isValidToken(token)){
				result = CustomFunction.getToken(token);
			}
			else if (token.equals("ANS")){
				result = new ValueToken(token, mLastResult);
			}
			else if (mParameters != null && mParameters.containsKey(token)){
				Log.d("Expression Tokenizer", "Found parameter token.");
				result = mParameters.get(token);
			}
			break;
		case Left_Parenthesis:
			result = SpecialToken.LEFT_PAREN;
			break;
		case Right_Parenthesis:
			result = SpecialToken.RIGHT_PAREN;
			break;
		case Period:
			result = SpecialToken.DECIMAL;
			break;
		case Comma:
			result = SpecialToken.COMMA;
			break;
		case Constant_e:
			result = ValueToken.E;
			break;
		case Constant_pi:
			result = ValueToken.PI;
			break;
		default:
			result = null;
			break;
		}
		Log.d("Tokenizer", "Result: " + result.mStrToken + " is " + result.mType);
		return result;
	}
	
	/**
	 * Converts a list of tokens to RPN format.
	 * @param tokens
	 * @return
	 * @throws CalculationException
	 */
	public LinkedList<Token> toRPN(LinkedList<Token> tokens) throws CalculationException{
		LinkedList<Token> output = new LinkedList<Token>();
		Token token = null;
		Log.d("Tokenizer", "Converting list to RPN of size: " + tokens.size());
		
		while(!tokens.isEmpty()){
			token = tokens.remove();
			Log.d("Tokenizer", token.toString());
			//Number or decimal
			if(token instanceof ValueToken){
				Log.d("Expression Tokenizer", "Found value token: " + token.mStrToken);
				output.add(token);
			}
			//Operator 
			else if(token instanceof OperatorToken){
				Token top = mOpStack.peek();
				OperatorToken currOp = (OperatorToken) token;
				
				while(top != null && top instanceof OperatorToken 
						&& isSwitchable(currOp, (OperatorToken)top)){
					output.add(mOpStack.pop());
					top = mOpStack.peek();
				}
				
				mOpStack.push(currOp);
			}
			else if(token instanceof CustomFunction || token instanceof PresetFunction){
				mOpStack.push(token);
			}
			else if(token instanceof SpecialToken){
				if(token.mType == TokenType.Left_Parenthesis){
					mOpStack.push(token);
				}
				else if(token.mType == TokenType.Right_Parenthesis){
					Token top = mOpStack.peek();
					while(top != null && !top.equals(SpecialToken.LEFT_PAREN)){
						output.add(mOpStack.pop());
						top = mOpStack.peek();
					}
					
					if(top == null){
						throw new ParenMismatchException(false);
					}
					else{
						mOpStack.pop();
						if(mOpStack.peek() instanceof FunctionToken){
							output.add(mOpStack.pop());
						}
					}
				}
				else if(token.mType == TokenType.Comma){
					Token top = mOpStack.peek();

					while(top != null && !top.equals(SpecialToken.LEFT_PAREN)){
						output.add(mOpStack.pop());
						top = mOpStack.peek();
					}
				
					if(top == null){
						throw new MisplacedSeparatorException(SpecialToken.COMMA.mStrToken.charAt(0));
					}
				}
			}
			else{
				throw new InvalidValueException(token.mStrToken);
			}
			
			//TODO remove debug code
			Log.d("Tokenizer", "RPN Output " + fromList(output));
		}
		
		return finishRPN(output);
	}
	
	/**
	 * Finalizes RPN Tokenization.
	 */
	private LinkedList<Token> finishRPN(LinkedList<Token> tokens) throws CalculationException {
		Log.d("Tokenizer", "Finishing tokenization");
		Token token = null;
		LinkedList<Token> output = tokens;
		while(!mOpStack.isEmpty()){
			token = mOpStack.pop();
			if(token instanceof SpecialToken){
				throw new InvalidCalculationException(token.toString());
			}
			else{
				output.add(token);
			}
		}
		
		return output;
	}
	
	/**
	 * Trims an expression string and adds 0 values for '-' unary operator hack.
	 * @param expr - Expression to be trimmed.
	 * @return - Trimmed expression with proper negatives.
	 */
	public static String trimExpression(String expr){
		String temp = expr.replaceAll("\\s+", "")
				  .replaceAll("\\(-", "(0-")
				  .replaceAll(",-", ",0-");
		return temp;
	}
	
	/**
	 * Returns a token string for debugging purposes.
	 * @param tokens
	 * @return
	 */
	public static String fromList(LinkedList<Token> tokens){
		StringBuilder sb = new StringBuilder();
		for(Token token : tokens){
			sb.append(token.mStrToken);
		}
		return sb.toString();
	}
	
	/**
	 * Whether or not b should be popped off the operator stack for a
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isSwitchable(OperatorToken a, OperatorToken b){
		if(a.isRightAssociative()){
			return a.getPrecedence() < b.getPrecedence();
		}
		else{
			return a.getPrecedence() <= b.getPrecedence();
		}
	}
}
