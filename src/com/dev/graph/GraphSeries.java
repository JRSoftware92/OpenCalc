package com.dev.graph;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import com.dev.opencalc.model.FunctionSeries;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Simple String - Float map class with parcelable functionality for easy graph instantiation.
 * @author John Riley
 *
 */
public class GraphSeries extends HashMap<Double, Double> implements Parcelable {

	private static final long serialVersionUID = 1L;
	
	public final GraphType type;
	public final int[] colors;
	public final String xAxisLabel, yAxisLabel, title;
	
	public GraphSeries(Parcel in){
		Bundle rows = in.readBundle();
		if(rows != null){
			for(String key : rows.keySet()){
				put(Double.parseDouble(key), rows.getDouble(key));
			}
		}
		type = GraphType.valueAt(in.readInt());
		colors = new int[0];
		in.readIntArray(colors);
		xAxisLabel = in.readString();
		yAxisLabel = in.readString();
		title = in.readString();
	}
	
	protected GraphSeries(HashMap<Double,Double> map, GraphType type, int[] colors, 
			String xLabel, String yLabel, String title){
		for(Double key : map.keySet()){
			this.put(key, map.get(key));
		}
		this.type = type;
		this.colors = colors;
		this.xAxisLabel = xLabel;
		this.yAxisLabel = yLabel;
		this.title = title;
	}
	
	public double getMinXValue(){
		double min = -1;
		for(Double key : keySet()){
			if(min < 0 || key < min){
				min = key;
			}
		}
		return min;
	}
	
	public double getMaxXValue(){
		double max = 0;
		for(Double key : keySet()){
			if(key > max){
				max = key;
			}
		}
		return max;
	}
	
	public double getMinYValue(){
		double min = -1;
		for(Double key : keySet()){
			if(min < 0 || get(key) < min){
				min = get(key);
			}
		}
		return min;
	}
	
	public double getMaxYValue(){
		double max = 0;
		for(Double key : keySet()){
			if(get(key) > max){
				max = get(key);
			}
		}
		return max;
	}
	
	public CategorySeries toCategorySeries(){
		return toCategorySeries("");
	}
	
	public CategorySeries toCategorySeries(String title){
		CategorySeries series = new CategorySeries(title);
		for(Double key : keySet()){
			series.add(Double.toString(key), get(key));
			//series.add(get(key));
		}
		return series;
	}
	
	public XYMultipleSeriesDataset toXYDataSet(){
		return toXYDataSet("");
	}
	
	public XYMultipleSeriesDataset toXYDataSet(String title){
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		dataSet.addSeries(toCategorySeries(title).toXYSeries());
		return dataSet;
	}
	
	//-------------------------------Builder Class --------------------------------------//
	
	/**
	 * 
	 * @author John Riley
	 *
	 */
	public static class Builder {
		private GraphType type = GraphType.NULL;
		private ArrayList<Integer> colors;
		private HashMap<Double, Double> valueMap;
		private String xLabel = "x", yLabel = "y", title = "";
		
		public Builder(){
			this.colors = new ArrayList<Integer>();
			this.valueMap = new HashMap<Double, Double>();
		}
		
		public Builder(GraphSeries series){
			this();
			for(Double key : series.keySet()){
				this.valueMap.put(key, series.get(key));
			}
			for(int i = 0; i < series.colors.length; i++){
				this.colors.add(series.colors[i]);
			}
			this.xLabel = series.xAxisLabel;
			this.yLabel = series.yAxisLabel;
			this.title = series.title;
			this.type = series.type;
		}
		
		public Builder(FunctionSeries series){
			this();
			this.type = GraphType.Line;
			Double num = 0d;
			for(Double entry : series.entrySet()){
				try{
					num = Double.parseDouble(series.get(entry));
				}
				catch(NumberFormatException nfe){
					num = null;
				}
				//TODO Find way to split two series.
				valueMap.put(entry, num);
			}
		}
		
		public Builder setXLabel(String label){ this.xLabel = label; return this;}
		
		public Builder setYLabel(String label){ this.yLabel = label; return this;}
		
		public Builder setTitle(String title){ this.title = title; return this;}
		
		public Builder setType(GraphType type){
			this.type = type;
			return this;
		}
		
		public Builder setColors(int... colors){
			if(colors != null){
				this.colors.clear();
				for(int color : colors){
					this.colors.add(color);
				}
			}
			return this;
		}
		
		public Builder addColor(int color){
			this.colors.add(color);
			return this;
		}
		
		public Builder setValue(Double key, Double value){
			if(this.valueMap.containsKey(key)){
				this.valueMap.remove(key);
			}
			this.valueMap.put(key, value);
			return this;
		}
		
		public GraphSeries toSeries(){
			GraphSeries series;
			int[] colorArr = new int[colors.size()];
			for(int i = 0; i < colorArr.length; i++){
				colorArr[i] = colors.get(i);
			}
			series = new GraphSeries(valueMap, type, colorArr, xLabel, yLabel, title);
			
			return series;
		}
	}

	//--------------------------------Parcelable Methods----------------------------//
	
	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		for(Double key : keySet()){
			bundle.putDouble(Double.toString(key), get(key));
		}
		dest.writeBundle(bundle);
		dest.writeInt(type.ordinal());
		dest.writeIntArray(colors);
		dest.writeString(xAxisLabel);
		dest.writeString(yAxisLabel);
		dest.writeString(title);
	}
	
	public static final Parcelable.Creator<GraphSeries> CREATOR = new Parcelable.Creator<GraphSeries>() {
        public GraphSeries createFromParcel(Parcel in) {
            return new GraphSeries(in);
        }

        public GraphSeries[] newArray(int size) {
         //throw new UnsupportedOperationException();
            return new GraphSeries[size];
        }
    };
}
