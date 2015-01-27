package com.dev.genericsqladapter;

import java.util.HashMap;
import java.util.Stack;

import android.content.ContentValues;

/**
 * 
 * Interface meant for implementation with standard data model objects to be stored using SQLite.
 * @author John Riley
 * 
 */
public interface ISQLObject {
	
	/**
	 * Returns the row id of the given sql object implementation.
	 * @return long db id corresponding to row placement
	 */
	public long getId();
	
	/**
	 * Returns a mapping of content values to be processed by the adapter on insert or update.
	 * @return Hashmap with table name as key, and a Queue of content values to be added to that table.
	 */
	public HashMap<String, Stack<ContentValues>> getContentValueMap();
}
