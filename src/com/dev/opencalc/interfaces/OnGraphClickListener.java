package com.dev.opencalc.interfaces;

import com.dev.opencalc.model.FunctionSeries;

/**
 * Listener interface for handling a graph click.
 * @author John Riley
 *
 */
public interface OnGraphClickListener {
	
	/**
	 * Called when a chart view is clicked.
	 * @param series - Series data contained by the graph.
	 */
	public void onGraphClick(FunctionSeries series);
}
