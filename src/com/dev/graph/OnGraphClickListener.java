package com.dev.graph;


public interface OnGraphClickListener {
	
	/**
	 * Called when a chart view is clicked.
	 * @param series - Series data contained by the graph.
	 */
	public void onGraphClick(GraphSeries series);
}
