package com.dev.graph;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

/**
 * Standard renderer for a bar chart.
 * @author John Riley
 *
 */
public class LineGraphRenderer extends XYMultipleSeriesRenderer implements IRenderer {

	private static final long serialVersionUID = 1L;
	
	public LineGraphRenderer(Context context, Bundle args){
		super();
		initializeRenderer(context, args);
	}
	
	@Override
	public void setColors(int... colors){
		if(colors != null){
			removeAllRenderers();
			XYSeriesRenderer renderer;
			for(int i = 0; i < colors.length; i++){
				renderer = new XYSeriesRenderer();
				renderer.setColor(colors[i]);
				renderer.setFillPoints(true);
				renderer.setPointStyle(PointStyle.CIRCLE);
				renderer.setStroke(BasicStroke.SOLID);
				renderer.setLineWidth(1.5f);
				addSeriesRenderer(i, renderer);
			}
		}
	}
	
	@Override
	public void initializeRenderer(){
        setChartTitleTextSize(20);
        setLabelsTextSize(16);
        setLegendTextSize(10);
        setInScroll(false);
        setAntialiasing(true);
        setShowGridX(true);
        setShowGridY(true);
        setPanEnabled(true, true);
        setApplyBackgroundColor(true);
        setDisplayValues(true);
        
        setBackgroundColor(Color.BLACK);
        setLabelsColor(Color.WHITE);
        setAxesColor(Color.WHITE);
	}
	
	@Override
	public void initializeRenderer(Context context, Bundle args){
		initializeRenderer();
		/*
		Resources res = context.getResources();
		
		setBackgroundColor(res.getColor(R.color.color_graph_background));
		setLabelsColor(res.getColor(R.color.color_graph_text));
		setAxesColor(res.getColor(R.color.color_graph_grid));
		*/
		if(args != null){
			GraphSeries series = args.getParcelable(GraphUtils.SERIES_KEY);
			setColors(series.colors);
			
			setChartTitle(series.title);
			setXTitle(series.xAxisLabel);
			setYTitle(series.yAxisLabel);
			setXAxisMax(series.getMaxXValue());
			setXAxisMin(series.getMinXValue());
			setYAxisMax(series.getMaxYValue());
			setYAxisMin(series.getMinYValue());
		}
	}

	@Override
	public DefaultRenderer toDefaultRenderer() { return null; }

	@Override
	public XYMultipleSeriesRenderer toXYRenderer() { return this; }
}
