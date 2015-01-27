package com.dev.genericsqladapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * Generic abstract Database Helper class designed for use with DatabaseAdapter.
 * @author John Riley
 * 
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {
	
	/**
	 * Initializes the Database Helper class.
	 * @param context - Context of the application.
	 * @param dbName - Name of the database.
	 * @param dbVersion - Current version of the database.
	 */
	public DatabaseHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		Log.d("DatabaseHelper", "Db name: " + dbName + " and version " + dbVersion);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    
		onCreate(db);
	}
}
