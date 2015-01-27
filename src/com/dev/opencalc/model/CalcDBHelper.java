package com.dev.opencalc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.genericsqladapter.DatabaseHelper;

/**
 * 
 * Helper class for CalculatorDatabase class.
 * @author John Riley
 * 
 */
public final class CalcDBHelper extends DatabaseHelper {
	
	public static final int mVersion = 9;
	
	public static final String CALCULATION_TABLE = "TABLE_CALCULATIONS";
	public static final String FUNCTION_TABLE = "TABLE_FUNCTIONS";
	
	public static final String ID = "_id";
	public static final String HOTKEY = "_hotkey";
	public static final String NAME = "_name";
	public static final String TOKEN = "_token";
	public static final String EXPRESSION = "_expr";
	public static final String RESULT = "_res";
	public static final String PARAMETERS = "_parstring";
	
	private static final String CREATE_TABLE_CALCULATION = "create table "
			+ CALCULATION_TABLE + "(" + ID + " integer primary key autoincrement, " 
			+ EXPRESSION + " text, " 
			+ RESULT + " text);";
	private static final String CREATE_TABLE_FUNCTION = "create table "
			+ FUNCTION_TABLE + "(" + ID + " integer primary key autoincrement, " 
			+ NAME + " text not null, " 
			+ TOKEN + " text not null, " 
			+ EXPRESSION + " text not null, " 
			+ PARAMETERS + " text, " 
			+ HOTKEY + " integer);";
	
	public static final String[] HISTORY_COLUMNS = new String[]{
		ID, EXPRESSION, RESULT
	};
	
	public static final String[] FUNCTION_COLUMNS = new String[]{
		ID, NAME, TOKEN, EXPRESSION, PARAMETERS, HOTKEY
	};
	
	private static final String DATABASE_NAME = "memorycalc.db";

	public CalcDBHelper(Context context) {
		super(context, DATABASE_NAME, mVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_CALCULATION);
		database.execSQL(CREATE_TABLE_FUNCTION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CalcDBHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
    
		db.execSQL("DROP TABLE IF EXISTS " + CALCULATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + FUNCTION_TABLE);
		
		onCreate(db);
	}
}
