package com.dev.opencalc.fragment;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.dev.opencalc.model.FunctionSeries;

import android.content.Context;
import android.os.Bundle;

/**
 * 
 * Encapsulating class for an achartengine line chart.
 * @author John Riley
 *
 */
public class LineGraphFragment extends GraphFragment {
	
	public static LineGraphFragment newInstance(FunctionSeries series){
		LineGraphFragment fragment = new LineGraphFragment();
		if(series != null && series.size() > 0){
			Bundle args = new Bundle();
			args.putParcelable(SERIES_KEY, series);
			fragment.setArguments(args);
		}
		return fragment;
	}

	@Override
	protected void initializeGraph(Context context, Bundle args) {
		if(args != null){
			FunctionSeries series = args.getParcelable(SERIES_KEY);
			addSeries(context, series);
		}
	}
	
	@Override
	protected void initializeRenderer(Context context, Bundle args){
		initializeXYRenderer(context, args);
	}

	@Override
	public void addSeries(Context context, FunctionSeries series) {
		clear();
		if(series != null){
			mSeries = series;
			mChartView = ChartFactory.getLineChartView(context, 
					series.toXYDataSet(), 
					(XYMultipleSeriesRenderer)mRenderer);
			mChartFrame.addView(mChartView);
		}
	}
}
