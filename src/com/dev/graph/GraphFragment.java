package com.dev.graph;

import org.achartengine.GraphicalView;

import com.dev.opencalc.R;
import com.dev.opencalc.fragment.SupportFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Generic achartengine graph class encapsulating basic universal graph functionality.
 * @author John Riley
 *
 */
public abstract class GraphFragment extends SupportFragment{
	
	protected IRenderer mRenderer;
	protected GraphSeries mSeries = null;
	
	protected GraphicalView mChartView;
	protected FrameLayout mChartFrame;
	
	protected OnGraphClickListener mListener;
	
	/**
	 * Called after the renderer has been initialized
	 * @param context
	 * @param args
	 */
	protected abstract void initializeGraph(Bundle args);
	
	/**
	 * Called after the fragment has been initialized to its default settings.
	 * Returns a renderer object suited to the graph type.
	 * @param context
	 * @param args
	 */
	protected abstract IRenderer getRenderer(Context context, Bundle args);
	
	/**
	 * Called during initialization of the graph (retrieves unique chartview)
	 * @param context
	 * @param series
	 * @return
	 */
	public abstract GraphicalView getChartView(Context context, GraphSeries series);
	
	public void setSeries(GraphSeries series) {
		clear();
		if(series != null && mRenderer != null){
			mSeries = series;
			Log.d("GraphFragment", "Graph Series Size: " + mSeries.size());
			try{
				mChartView = getChartView(getActivity(), series);
				mChartFrame.addView(mChartView);
				mChartView.setOnClickListener(GRAPH_CLICK_LISTENER);
			}
			catch(Exception e){
				String msg = e.getMessage();
				Log.e("Graph Fragment", msg == null ? "Unknown Error" : msg);
				showErrorMessage(e);
			}
			
		}
		else{
			showNeedDataMessage();
		}
	}
	
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

	@Override
	protected int getLayoutId() { return R.layout.layout_frame; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		initializeDefaultFragment(rootView);
		initializeRenderer(rootView, args);
		initializeGraph(args);
	}

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mChartFrame = (FrameLayout) rootView.findViewById(R.id.layoutFrame);
	}
	
	protected void initializeRenderer(View rootView, Bundle args){
		mRenderer = getRenderer(rootView.getContext(), args);
	}
	
	public void clear(){
		mChartFrame.removeAllViews();
	}
	
	public void setTitle(String title){
		if(title != null){
			mRenderer.setChartTitle(title);
		}
	}
	
	public void setRendererColors(int[] colors){
		if(colors != null){
			mRenderer.setColors(colors);
		}
	}
	
	protected void showNeedDataMessage(){
		clear();
		
		TextView text = new TextView(getActivity());
		text.setText("No data available to display.");
		mChartFrame.addView(text);
	}
	
	protected void showErrorMessage(Exception e){
		clear();
		
		StringBuilder sb = new StringBuilder("Unexpected error has occurred: ");
		if(e.getMessage() != null){
			sb.append(e.getMessage());
			sb.append('\n');
		}
		if(mRenderer == null){
			sb.append("Renderer is null.\n");
		}
		else if(mRenderer.getSeriesRendererCount() == 0){
			sb.append("Renderer has no series renderers.\n");
		}
		else{
			sb.append("Renderer has: ");
			sb.append(mRenderer.getSeriesRendererCount());
			sb.append(" series renderers.\n");
		}
		
		if(mSeries == null){
			sb.append("Series is null.\n");
		}
		else if(mSeries.size() == 0){
			sb.append("Series is empty.\n");
		}
		else{
			sb.append("Series has: ");
			sb.append(mSeries.size());
			sb.append(" entries.\n");
		}
		TextView text = new TextView(getActivity());
		text.setText(sb.toString());
		mChartFrame.addView(text);
	}
	
	protected static Bundle getBundle(String title, GraphSeries series){
		if(series != null && series.size() > 0){
			Bundle args = new Bundle();
			args.putString(GraphUtils.TITLE_KEY, title);
			args.putParcelable(GraphUtils.SERIES_KEY, series);
			return args;
		}
		else{
			return null;
		}
	}
	
	protected final Button.OnClickListener GRAPH_CLICK_LISTENER = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			if(mListener != null){
				mListener.onGraphClick(mSeries);
			}
		}
	};
}
