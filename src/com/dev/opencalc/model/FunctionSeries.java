package com.dev.opencalc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dev.opencalc.expressionparsing.ExpressionTokenizer;
import com.dev.opencalc.expressionparsing.Interpreter;
import com.dev.opencalc.expressionparsing.ParameterToken;

/**
 * Mapping class for f(x) = y style functions.
 * @author John Riley
 *
 */
public class FunctionSeries implements Parcelable {

	private HashMap<Double, String> mValueMap = null;
	private String mXLabel = "", mYLabel = "";
	
	public FunctionSeries(){
		mValueMap = new HashMap<Double, String>();
	}
	
	public FunctionSeries(FunctionSeries series){
		mValueMap = series.mValueMap;
	}
	
	public FunctionSeries(Parcel in){
		mXLabel = in.readString();
		mYLabel = in.readString();
		Bundle bundle = in.readBundle();
		for(String key : bundle.keySet()){
			mValueMap.put(Double.parseDouble(key), bundle.getString(key));
		}
	}
	
	public int size(){ return mValueMap == null ? 0 : mValueMap.size(); }
	
	public String get(double key){ return mValueMap.get(key); } 
	
	public String getXAxisLabel(){ return mXLabel; }
	
	public String getYAxisLabel(){ return mYLabel; }
	
	public double getDouble(double key){ return Double.parseDouble(mValueMap.get(key)); }
	
	public boolean hasResult(double key){
		boolean flag = false;
		flag = mValueMap.containsKey(key);
		if(flag){
			try{
				Double.parseDouble(mValueMap.get(key));
				flag = true;
			}
			catch(NumberFormatException nfe){
				flag = false;
			}
		}
		
		return flag;
	}
	
	public Set<Double> entrySet(){ return mValueMap.keySet(); }
	
	public Collection<String> resultSet(){ return mValueMap.values(); }
	
	public Bundle toBundle(){
		Bundle args = new Bundle();
		for(Double val : entrySet()){
			args.putString(Double.toString(val), get(val));
		}
		return args;
	}
	
	public CategorySeries toCategorySeries(){
		return toCategorySeries("");
	}
	
	public CategorySeries toCategorySeries(String title){
		CategorySeries series = new CategorySeries(title);
		for(Double key : mValueMap.keySet()){
			series.add(Double.toString(key), getDouble(key));
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
	
	public static class Builder {
		
		private int mNumResults = 0;
		private int mAxisPos = 0;
		private double mDelta = 0;
		private double mStartVal = 0;
		private String mXLabel = "";
		private String mYLabel = "";
		
		private FunctionMeta mFunc = null;
		private HashMap<String, Double> mArgMap = null;
		
		public Builder(FunctionMeta func){
			mFunc = func;
		}
		
		public Builder(FunctionMeta func, HashMap<String, Double> args){
			this(func);
			mArgMap = args;
		}
		
		public Builder setXAxisLabel(String label){ 
			this.mXLabel = label; 
			return this;
		}
		
		public Builder setYAxisLabel(String label){
			this.mYLabel = label;
			return this;
		}
		
		/**
		 * Sets the first entry value to be computed in the series.
		 * @param value
		 * @return
		 */
		public Builder setStartingValue(double value){ mStartVal = value; return this; }
		
		/**
		 * Sets the rate of change between x values
		 * @param delta
		 */
		public Builder setDelta(double delta){ mDelta = delta; return this; }
		
		/**
		 * Sets the number of outputted results.
		 * @param num
		 */
		public Builder setNumResults(int num){ mNumResults = num; return this; }
		
		/**
		 * Sets which parameter will be used as the x-axis during computation.
		 * @param index
		 */
		public Builder setPrimaryAxisByIndex(int index){ mAxisPos = index; return this; }
		
		private FunctionSeries calculate(){
			FunctionSeries series = new FunctionSeries();
			if(mNumResults > 0 && mFunc != null){
				Log.v("FunctionSeries", "Beginning calculation");
				ParameterToken[] values = new ParameterToken[mFunc.getNumberOfParameters()];
				double result = 0;
				double entry = mStartVal;
				ExpressionTokenizer tokenizer;
				Interpreter interpreter;
				char[] params = mFunc.getParameterArray();
				Log.v("FunctionSeries", "Method Variable Initialized");
				
				for(int i = 0; i < values.length; i++){		//set parameter values
					Log.v("FunctionSeries", "Initializing parameter " + i);
					if(i == mAxisPos){
						values[i] = new ParameterToken("", 0); //TO BE SET REPEATEDLY.
						Log.v("FunctionSeries", "Setting main axis");
					}
					else{
						Log.v("FunctionSeries", "Setting constant parameter");
						values[i] = new ParameterToken(Character.toString(params[i]),
								mArgMap.get(Character.toString(params[i])));
					}
				}
				for(int i = 0; i < mNumResults; i++){	//set entry value, then tokenize and interpret
					if(i > 0){
						entry += mDelta;
						Log.d("FunctionSeries", "Computing entry " + Double.toString(entry));
					}
					values[mAxisPos] = new ParameterToken(Character.toString(params[mAxisPos]),
							entry);
		
					try{
						Log.v("FunctionSeries", "Tokenizing" );
						tokenizer = new ExpressionTokenizer(mFunc.getExpression(), values);
						Log.v("FunctionSeries", "Interpreting" );
						interpreter = new Interpreter(tokenizer.getOutput());
						Log.v("FunctionSeries", "Getting Result" );
						result = interpreter.getResult();
						Log.d("FunctionSeries", "Placing entry with result " + Double.toString(result));
						series.mValueMap.put(entry, Double.toString(result));
					}
					catch(Exception e){
						series.mValueMap.put(entry, "N/A");
					}
				}
			}
			
			return series;
		}
		
		public FunctionSeries toSeries(){
			FunctionSeries series = calculate();
			series.mXLabel = this.mXLabel;
			series.mYLabel = this.mYLabel;
			return series;
		}
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mXLabel);
		dest.writeString(mYLabel);
		dest.writeBundle(toBundle());
	}
	
	public static final Parcelable.Creator<FunctionSeries> CREATOR = new Parcelable.Creator<FunctionSeries>() {
        public FunctionSeries createFromParcel(Parcel in) {
            return new FunctionSeries(in);
        }

        public FunctionSeries[] newArray(int size) {
         //throw new UnsupportedOperationException();
            return new FunctionSeries[size];
        }
    };
}
