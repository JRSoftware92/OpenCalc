package com.dev.opencalc.model;

import android.content.Context;
import android.database.Cursor;

import com.dev.genericsqladapter.DatabaseAdapter;

/**
 * 
 * Database adapter for accessing and storing calculation history and user functions.
 * @author John Riley
 * 
 */
public final class CalculatorDatabase extends DatabaseAdapter {

	public CalculatorDatabase(Context context) {
		super(context);
		mDbHelper = new CalcDBHelper(context);
		open();
	}
	
	//-------------------------Data Methods---------------------------//
	
	/**
	 * Get an array of Calculation history.
	 * @return Array of Calculations representing overall calculation history.
	 */
	public Calculation[] getCalculationHistory(){
		Cursor cursor = mDb.rawQuery("SELECT * FROM " + CalcDBHelper.CALCULATION_TABLE, null);
		Calculation[] history = new Calculation[0];
		
		if(cursor != null){
			cursor.moveToFirst();
			history = new Calculation[cursor.getCount()];
			int count = 0;
			while(!cursor.isAfterLast()){
				history[count++] = new Calculation(cursor);
				cursor.moveToNext();
			}
		}
		
		return history;
	}
	
	/**
	 * Get an array of calculation history up to the number provided.
	 * @param numCalculations - Number of calculations to return
	 * @return Array of calculations from calculation history.
	 */
	public Calculation[] getCalculationHistory(int numCalculations){
		Cursor cursor = mDb.rawQuery("Select * FROM " + CalcDBHelper.CALCULATION_TABLE, null);
		Calculation[] history = new Calculation[0];
		if(cursor != null){
			cursor.moveToFirst();
			history = new Calculation[cursor.getCount() < numCalculations ? cursor.getCount() : numCalculations];
			int count = 0;
			while(!cursor.isAfterLast() && count < numCalculations){
				history[count++] = new Calculation(cursor);
				cursor.moveToNext();
			}
		}
		
		return history;
	}
	
	public long clearHistory(){
		return mDb.delete(CalcDBHelper.CALCULATION_TABLE, null, null);
	}
	
	public FunctionMeta getFunctionByHotkey(int funcPos){
		FunctionMeta func = null;
		Cursor cursor = getCursor(true, CalcDBHelper.FUNCTION_TABLE,
				CalcDBHelper.FUNCTION_COLUMNS, CalcDBHelper.HOTKEY + " = " + funcPos);
		
		if(cursor != null){
			func = new FunctionMeta(cursor);
		}
		
		return func;
	}
	
	/**
	 * Get an array of user created functions.
	 * @return An array of functions stored in the database.
	 */
	public FunctionMeta[] getFunctions(){
		Cursor cursor = mDb.rawQuery("Select * FROM " + CalcDBHelper.FUNCTION_TABLE, null);
		FunctionMeta[] history = new FunctionMeta[0];
		
		if(cursor != null){
			cursor.moveToFirst();
			history = new FunctionMeta[cursor.getCount()];
			int count = 0;
			while(!cursor.isAfterLast()){
				
				history[count++] = new FunctionMeta(cursor);
				cursor.moveToNext();
			}
		}
		
		return history;
	}
	
	/**
	 * Insert calculation into database.
	 * @param calc - Calculation history data to store.
	 * @return Row id of calculation history.
	 */
	public long insertCalculation(Calculation calc){
		return addData(CalcDBHelper.CALCULATION_TABLE, calc);
	}
	
	/**
	 * Insert function into database.
	 * @param func - Function data to store
	 * @return - Row id of function data.
	 */
	public long insertFunction(FunctionMeta func){
		return addData(CalcDBHelper.FUNCTION_TABLE, func);
	}
	
	/**
	 * Updates the calculation at the given rowId
	 * @param rowId
	 * @param calc
	 * @return
	 */
	public long updateCalculationRow(long rowId, Calculation calc){
		String whereClause = CalcDBHelper.ID + " = ?";
		return updateData(calc, whereClause, new String[]{Long.toString(rowId)});
	}
	
	/**
	 * Updates the function at the given rowId
	 * @param rowId
	 * @param func
	 * @return
	 */
	public long updateFunctionRow(long rowId, FunctionMeta func){
		String whereClause = CalcDBHelper.ID + " = ?";
		return updateData(func, whereClause, new String[]{Long.toString(rowId)});
	}
	
	/**
	 * Deletes the calculation at the given row id.
	 * @param rowId
	 * @return
	 */
	public long deleteCalculationRow(long rowId){
		String whereClause = CalcDBHelper.ID + " = ?";
		return deleteDataById(rowId, new String[]{CalcDBHelper.CALCULATION_TABLE},
				new String[]{whereClause});
	}
	
	/**
	 * Deletes the function at the given row id.
	 * @param rowId
	 * @return
	 */
	public long deleteFunctionRow(long rowId){
		String whereClause = CalcDBHelper.ID + " = ?";
		return deleteDataById(rowId, new String[]{CalcDBHelper.CALCULATION_TABLE},
				new String[]{whereClause});
	}
}
