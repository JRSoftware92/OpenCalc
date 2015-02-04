package com.dev.graph;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.os.Bundle;

public interface IRenderer {
	
	/**
	 * Sets the title of the series being displayed.
	 * @param title
	 */
	public void setChartTitle(String title);
	
	/**
	 * Sets the series renderers of this renderer using provided colors.
	 * @param colors
	 */
	public void setColors(int... colors);
	
	/**
	 * Default initialization for this renderer.
	 */
	public void initializeRenderer();
	
	/**
	 * Initialize the renderer using provided context and arguments.
	 * @param context
	 * @param args
	 */
	public void initializeRenderer(Context context, Bundle args);
	
	/**
	 * Returns the object as a DefaultRenderer if possible, null otherwise.
	 * @return
	 */
	public DefaultRenderer toDefaultRenderer();
	
	/**
	 * Returns the object as an XYMultipleSeriesRenderer if possible, null otherwise.
	 * @return
	 */
	public XYMultipleSeriesRenderer toXYRenderer();
	
	/**
	 * Returns number of series renderers in the renderer object.
	 * @return
	 */
	public int getSeriesRendererCount();
}
