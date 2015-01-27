package com.dev.opencalc.model;

import java.util.HashMap;
import java.util.Stack;

import com.dev.genericsqladapter.SQLObject;
import com.dev.opencalc.expressionparsing.ParameterToken;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 * Meta data for a stored function.
 * @author John Riley
 * 
 */
public class FunctionMeta extends SQLObject implements Parcelable {
	
	private long mId = -1;
	private String mFunctionName = "";
	private String mFunctionToken = "";
	private String mExpression = "";
	private char[] mParameters = new char[0];
	
	public FunctionMeta(String name, String token, String expression, char[] parameters){
		mFunctionName = name;
		mFunctionToken = token;
		mExpression = expression;
		mParameters = parameters;
	}
	
	public FunctionMeta(String name, String token, String expression, ParameterToken[] parameters){
		mFunctionName = name;
		mFunctionToken = token;
		mExpression = expression;
		mParameters = new char[parameters.length];
		for(int i = 0; i < mParameters.length; i++){
			mParameters[i] = parameters[i].getStringParameter().charAt(0);
		}
	}
	
	public FunctionMeta(Parcel in){
		mId = in.readLong();
		mFunctionName = in.readString();
		mFunctionToken = in.readString();
		mExpression = in.readString();
		in.readCharArray(mParameters);
	}
	
	public FunctionMeta(Cursor cursor){
		mId = cursor.getLong(cursor.getColumnIndex(CalcDBHelper.ID));
		mFunctionName = cursor.getString(cursor.getColumnIndex(CalcDBHelper.NAME));
		mFunctionToken = cursor.getString(cursor.getColumnIndex(CalcDBHelper.TOKEN));
		mExpression = cursor.getString(cursor.getColumnIndex(CalcDBHelper.EXPRESSION));
		mParameters = cursor.getString(cursor.getColumnIndex(CalcDBHelper.PARAMETERS)).toCharArray();
		for(char c : mParameters){
			Log.d("Function Initialization", Character.toString(c));
		}
	}
	
	public String getName(){ return mFunctionName; }
	
	public String getTokenString(){ return mFunctionToken; }
	
	public String getExpression(){ return mExpression; }
	
	public String getParameterString(){ 
		if(mParameters != null){
			return new String(mParameters); 
		}
		else{
			return null;
		}
	}
	
	public int getNumberOfParameters(){
		if(mParameters == null){
			return 0;
		}
		else{
			return mParameters.length;
		}
	}
	
	public char[] getParameterArray(){ return mParameters; }
	
	public String getParametersWithCommas(){
		StringBuilder sb = new StringBuilder("(");
		if(mParameters != null && mParameters.length > 0){
			sb.append(mParameters[0]);
			if(mParameters.length > 1){
				for(int i = 1; i < mParameters.length; i++){
					sb.append("," + mParameters[i]);
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public long getId() { return mId; }

	@Override
	public HashMap<String, Stack<ContentValues>> getContentValueMap() {
		HashMap<String, Stack<ContentValues>> map = new HashMap<String, Stack<ContentValues>>();
		Stack<ContentValues> stack = new Stack<ContentValues>();
		ContentValues values = new ContentValues();
		
		if(mId > -1){
			values.put(CalcDBHelper.ID, mId);
		}
		values.put(CalcDBHelper.NAME, mFunctionName);
		values.put(CalcDBHelper.TOKEN, mFunctionToken);
		values.put(CalcDBHelper.EXPRESSION, mExpression);
		values.put(CalcDBHelper.PARAMETERS, getParameterString());
		stack.push(values);
		map.put(CalcDBHelper.FUNCTION_TABLE, stack);
		return map;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeString(mFunctionName);
		dest.writeString(mFunctionToken);
		dest.writeString(mExpression);
		dest.writeCharArray(mParameters);
	}
	
	public static final Parcelable.Creator<FunctionMeta> CREATOR = new Parcelable.Creator<FunctionMeta>() {
        public FunctionMeta createFromParcel(Parcel in) {
            return new FunctionMeta(in);
        }

        public FunctionMeta[] newArray(int size) {
         //throw new UnsupportedOperationException();
            return new FunctionMeta[size];
        }
    };
    
    public static String compute(FunctionMeta function, String[] entries){
    	//TODO Return computed functional expression with parameters substituted.
    	return "";
    }
}
