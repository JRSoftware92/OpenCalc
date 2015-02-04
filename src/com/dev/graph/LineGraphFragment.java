package com.dev.graph;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import android.content.Context;
import android.os.Bundle;


/**
 * Encapsulating class for an achartengine line chart.
 * @author John Riley
 *
 */
public class LineGraphFragment extends GraphFragment {
	
	public static LineGraphFragment newInstance(GraphSeries series){
		LineGraphFragment fragment = new LineGraphFragment();
		if(series != null){
			Bundle args = new Bundle();
			args.putParcelable(GraphUtils.SERIES_KEY, series);
			fragment.setArguments(args);
		}
		return fragment;
	}

	@Override
	protected void initializeGraph(Bundle args) {
		if(args != null){
			GraphSeries series = args.getParcelable(GraphUtils.SERIES_KEY);
			setSeries(series);
		}
	}

	@Override
	public GraphicalView getChartView(Context context, GraphSeries series) {
		return mChartView = ChartFactory.getLineChartView(context, series.toXYDataSet(), 
				mRenderer.toXYRenderer());
	}

	@Override
	protected IRenderer getRenderer(Context context, Bundle args) {
		return new LineGraphRenderer(context, args);
	}
}
