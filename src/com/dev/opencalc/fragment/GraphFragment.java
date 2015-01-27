package com.dev.opencalc.fragment;

import org.achartengine.GraphicalView;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.dev.opencalc.R;
import com.dev.opencalc.interfaces.OnGraphClickListener;
import com.dev.opencalc.model.FunctionSeries;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * <------------------------------CURRENTLY NOT WORKING--------------------------------->
 * Generic achartengine graph class encapsulating basic universal graph functionality.
 * @author John Riley
 *
 */
public abstract class GraphFragment extends SupportFragment{
	
	protected static final String SERIES_KEY = "com.suitepitch.seriesKey",
						 		  TYPE_KEY = "com.suitepitch.typeKey",
						 		  TITLE_KEY = "com.suitepitch.titleKey",
						 		  COLOR_KEY = "com.suitepitch.colorKey";
	
	protected FunctionSeries mSeries = null;
	
	protected GraphicalView mChartView;
	protected FrameLayout mChartFrame;
	protected DefaultRenderer mRenderer;
	
	protected OnGraphClickListener mListener;
	
	/**
	 * Called after the renderer has been initialized
	 * @param context - Context of the application.
	 * @param args - Arguments provided for initialization.
	 */
	protected abstract void initializeGraph(Context context, Bundle args);
	
	/**
	 * Called when a new function series is to be added to the graph fragment.
	 * @param context - Context of the application.
	 * @param series - Series to be displayed.
	 */
	public abstract void addSeries(Context context, FunctionSeries series);
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnGraphClickListener) activity;
		} 
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnGraphClickListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	
	/**
	 * Called after the fragment has been initialized to its default settings.
	 * @param args
	 */
	protected void initializeRenderer(Context context, Bundle args){
		if(args != null){
			String title = args.getString(TITLE_KEY);
			int[] colors = args.getIntArray(COLOR_KEY);
			
			setTitle(title);
			setRendererColors(colors);
		}
	}
	
	/**
	 * Initializes an XYMultipleSeriesRenderer in place of the default renderer.
	 * @param context - Context of the application.
	 * @param args - Arguments provided for initialization.
	 */
	protected void initializeXYRenderer(Context context, Bundle args){
		mRenderer = new XYMultipleSeriesRenderer();
		if(args != null){
			String title = args.getString(TITLE_KEY);
			int[] colors = args.getIntArray(COLOR_KEY);
			
			setTitle(title);
			setRendererColors(colors);
		}
	}

	@Override
	protected int getLayoutId() { return R.layout.layout_frame; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		initializeDefaultFragment(rootView);
		initializeRenderer(rootView.getContext(), args);
		initializeGraph(rootView.getContext(), args);
	}

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mChartFrame = (FrameLayout) rootView.findViewById(R.id.layoutFrame);
		mRenderer = new DefaultRenderer();
	}
	
	/**
	 * Clears the frame layout of currently displaying views.
	 */
	public void clear(){
		mChartFrame.removeAllViews();
	}
	
	/**
	 * Sets the chart title of the graph renderer.
	 * @param title - String title to be set.
	 */
	public void setTitle(String title){
		if(title != null){
			mRenderer.setChartTitle(title);
		}
	}
	
	/**
	 * Initializes a simple series renderer for each color provided (preserves order).
	 * @param colors - Int array of color values to be added to the renderer.
	 */
	public void setRendererColors(int[] colors){
		if(colors != null){
			mRenderer.removeAllRenderers();
			SimpleSeriesRenderer renderer;
			for(int i = 0; i < colors.length; i++){
				renderer = new SimpleSeriesRenderer();
				renderer.setColor(colors[i]);
				mRenderer.addSeriesRenderer(i, renderer);
			}
		}
	}
	
	/**
	 * Displays a message in the event there is no data to be displayed.
	 * @param rootView - Root view of the layout.
	 */
	protected void showNeedDataMessage(View rootView){
		clear();
		
		TextView text = new TextView(rootView.getContext());
		text.setText("No data available to display");
		mChartFrame.addView(text);
	}
	
	/**
	 * Returns a standard initialization bundle for this fragment.
	 * @param title
	 * @param series
	 * @return
	 */
	protected static Bundle getBundle(String title, FunctionSeries series){
		if(series != null && series.size() > 0){
			Bundle args = new Bundle();
			args.putString(TITLE_KEY, title);
			args.putParcelable(SERIES_KEY, series);
			return args;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Returns a standard initialization bundle for this fragment.
	 * @param title
	 * @param series
	 * @param colors
	 * @return
	 */
	protected static Bundle getBundle(String title, FunctionSeries series, int[] colors){
		if(series != null && series.size() > 0 && colors != null && title != null){
			Bundle args = new Bundle();
			args.putString(TITLE_KEY, title);
			args.putParcelable(SERIES_KEY, series);
			args.putIntArray(COLOR_KEY, colors);
			return args;
		}
		else{
			return null;
		}
	}
}
