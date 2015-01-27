package com.dev.genericsqladapter;

import android.database.Cursor;

/**
 * 
 * Abstract SQL Database Object class for simplified database interaction.
 * @author John Riley
 * 
 */
public abstract class SQLObject implements ISQLObject {
	
	public SQLObject(){}
	
	/**
	 * SQLCursor constructor.
	 * @param cursor - SQL cursor object to be used for instantiation.
	 */
	public SQLObject(Cursor cursor){}
}
