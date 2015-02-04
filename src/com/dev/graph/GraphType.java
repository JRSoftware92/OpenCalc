package com.dev.graph;

public enum GraphType {
	NULL, Bar, Pie, Line;
	
	public static GraphType valueAt(int index){
		GraphType[] values = values();
		return (index < 0 || index >= values.length) ? NULL : values[index]; 
	}
}
