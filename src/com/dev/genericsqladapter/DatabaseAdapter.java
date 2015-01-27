package com.dev.genericsqladapter;

import java.util.HashMap;
import java.util.Stack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * Generic database adapter class meant for implementation with the ISQLObject class.
 * @author John Riley
 * 
 */
public abstract class DatabaseAdapter {
	
	protected SQLiteDatabase mDb;
	protected DatabaseHelper mDbHelper;
	
	/**
	 * Context constructor.
	 * @param context - Context of the application.
	 */
	public DatabaseAdapter(Context context){}
	
	/**
	 * Opens the database adapter.
	 * @throws SQLException
	 */
	public void open() throws SQLException { mDb = mDbHelper.getWritableDatabase(); }
	  
	/**
	 * Closes the database adapter.
	 */
	public void close() { mDbHelper.close(); }
	
	//--------------------------IDBObject Interface Methods-----------------------//
	
	/**
	 * Adds a SQLObject to the database into the provided table
	 * @param idTable - Table where the data's primary key can be found.
	 * @param data - IDBObject with interface for adding ContentValues to tables.
	 * @return Row id returned from the insert on the primary key table, -1 otherwise
	 */
	public final long addData(String idTable, ISQLObject data){
		long id = -1;
		
		if(data != null){
			HashMap<String, Stack<ContentValues>> map = data.getContentValueMap();
			if(map != null){
				Stack<ContentValues> values = null;
				for(String table : map.keySet()){
					values = map.get(table);
					if(values != null){
						while(!values.isEmpty()){
							if(table.equals(idTable)){
								id = mDb.insert(table, null, values.pop());
							}
							else{
								mDb.insert(table, null, values.pop());
							}
						}
					}
				}
			}
		}
		
		return id;
	}
	
	/**
	 * Updates the SQLObject data based on the provided where clause and arguments.
	 * @param data - Data to be added to the database
	 * @param whereClause - Determines how the data will be inserted
	 * @param whereArgs - Arguments (if any) for the whereClause provided.
	 * @return Number of rows effected.
	 */
	public final long updateData(ISQLObject data, String whereClause, String[] whereArgs){
		long count = 0;
		
		if(data != null){
			HashMap<String, Stack<ContentValues>> map = data.getContentValueMap();
			if(map != null){
				Stack<ContentValues> values = null;
				for(String table : map.keySet()){
					values = map.get(table);
					if(values != null){
						while(!values.isEmpty()){
							count += mDb.update(table, values.pop(), 
									whereClause, whereArgs);
						}
					}
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Deletes the data based on the provided id across the given tables using provided where clauses.
	 * @param tables - Tables to delete from.
	 * @param idColumn - Column the id corresponds to
	 * @param id - Id of the data
	 * @return Number of rows effected.
	 */
	public final long deleteDataById(long idTable, String[] tables, String[] whereClauses){
		long count = 0;
		if(tables != null && tables.length > 0 && tables.length == whereClauses.length){
			for(int i = 0; i < tables.length; i++){
				count += mDb.delete(tables[i], whereClauses[i], null);
			}
		}
		
		return count;
	}
	
	/**
	 * Returns a properly initialized cursor based on a where clause.
	 * @param distinct
	 * @param table
	 * @param columns
	 * @param whereClause
	 * @return
	 */
	protected Cursor getCursor(boolean distinct, String table, String[] columns, String whereClause){
		Cursor myCurs = mDb.query(distinct, table, columns,
				whereClause, null, null, null, null, null);
		if (myCurs != null) {
			myCurs.moveToFirst();
		}
		return myCurs;
	}
	
	/**
	 * Returns a properly initialized cursor based on where and group by clauses.
	 * @param distinct
	 * @param table
	 * @param columns
	 * @param whereClause
	 * @param groupBy
	 * @return
	 */
	protected Cursor getCursor(boolean distinct, String table, String[] columns, String whereClause,
			String groupBy){
		Cursor myCurs = mDb.query(distinct, table, columns,
				whereClause, null, groupBy, null, null, null);
		if (myCurs != null) {
			myCurs.moveToFirst();
		}
		return myCurs;
	}
	
	/**
	 * Returns a properly initialized cursor based on where, group by, and having clauses.
	 * @param distinct
	 * @param table
	 * @param columns
	 * @param whereClause
	 * @param groupBy
	 * @param having
	 * @return
	 */
	protected Cursor getCursor(boolean distinct, String table, String[] columns, String whereClause,
			String groupBy, String having){
		
		Cursor myCurs = mDb.query(distinct, table, columns,
				whereClause, null, groupBy, having, null, null);
		if (myCurs != null) {
			myCurs.moveToFirst();
		}
		return myCurs;
	}
	
	/**
	 * Returns a properly initialized cursor based on where, group by, having, and orderBy clauses.
	 * @param distinct
	 * @param table
	 * @param columns
	 * @param whereClause
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	protected Cursor getCursor(boolean distinct, String table, String[] columns, String whereClause,
			String groupBy, String having, String orderBy){
		
		Cursor myCurs = mDb.query(distinct, table, columns,
				whereClause, null, groupBy, having, orderBy, null);
		if (myCurs != null) {
			myCurs.moveToFirst();
		}
		return myCurs;
	}
}
