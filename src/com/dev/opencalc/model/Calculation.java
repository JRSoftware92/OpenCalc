package com.dev.opencalc.model;

import java.util.HashMap;
import java.util.Stack;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.genericsqladapter.SQLObject;
import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.expressionparsing.Interpreter;

/**
 * 
 * Data object holding calculation history data.
 * @author John Riley
 * 
 */
public class Calculation extends SQLObject implements Parcelable {
	
	private long mId = -1;
	private boolean bIsError = false;
	private String mStatement = "";
	private String mResult = "";
	
	public Calculation(String statement){
		super();
		mStatement = statement;
		try {
			mResult = Double.toString(Interpreter.calculate(mStatement));
		} 
		catch (CalculationException ce){
			bIsError = true;
			mResult = ce.getMessage();
		}
		catch (Exception e) {
			bIsError = true;
			mResult = "Unknown Error";
		}
	}
	
	public Calculation(String statement, String result){
		super();
		mStatement = statement;
		mResult = result;
	}
	
	public Calculation(Parcel in){
		mId = in.readLong();
		mStatement = in.readString();
		mResult = in.readString();
	}

	public Calculation(Cursor cursor) {
		super(cursor);
		mId = cursor.getLong(cursor.getColumnIndex(CalcDBHelper.ID));
		mStatement = cursor.getString(cursor.getColumnIndex(CalcDBHelper.EXPRESSION));
		mResult = cursor.getString(cursor.getColumnIndex(CalcDBHelper.RESULT));
	}
	
	//----------------------------Data Model Accessors/Mutators----------------------------//
	
	public String getStringStatement(){ return mStatement; }
	
	public String getStringResult(){ return mResult; }
	
	public double getDoubleResult() throws NumberFormatException {
		return Double.parseDouble(mResult);
	}
	
	public int getIntResult() throws NumberFormatException{
		return Integer.parseInt(mResult);
	}
	
	public boolean isError(){ return bIsError; }
	
	//---------------------------Database Interface Methods------------------//
	
	@Override
	public long getId() {
		return mId;
	}

	@Override
	public HashMap<String, Stack<ContentValues>> getContentValueMap() {
		HashMap<String, Stack<ContentValues>> map = new HashMap<String, Stack<ContentValues>>();
		Stack<ContentValues> queue = new Stack<ContentValues>();
		ContentValues values = new ContentValues();
		
		if(mId > -1){
			values.put(CalcDBHelper.ID, mId);
		}
		values.put(CalcDBHelper.EXPRESSION, mStatement);
		values.put(CalcDBHelper.RESULT, mResult);
		queue.add(values);
		map.put(CalcDBHelper.CALCULATION_TABLE, queue);
		
		return map;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeString(mStatement);
		dest.writeString(mResult);
	}
	
	public static final Parcelable.Creator<Calculation> CREATOR = new Parcelable.Creator<Calculation>() {
        public Calculation createFromParcel(Parcel in) {
            return new Calculation(in);
        }

        public Calculation[] newArray(int size) {
         //throw new UnsupportedOperationException();
            return new Calculation[size];
        }
    };
}
