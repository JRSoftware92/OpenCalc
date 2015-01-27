package com.dev.opencalc.adapter;

import com.dev.opencalc.R;
import com.dev.opencalc.expressionparsing.ParameterToken;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Array Adapter for function parameters.
 * @author John Riley
 *
 */
public class ParameterListAdapter extends ArrayAdapter<ParameterToken>{
	
	private final Context context;
	private int mAxis = 0;
	
	static class ViewHolder{
		TextView text;
		EditText value;
		RadioButton radio;
	}
	
	/**
	 * Standard constructor
	 * @param context - Context of the application.
	 * @param tokens - Array of parameter tokens objects representing the parameters to be
	 * displayed.
	 * @param axis - Index of the primary axis parameter (x-axis) in the token array.
	 */
	public ParameterListAdapter(Context context, ParameterToken[] tokens, int axis){
		super(context, R.layout.item_function_parameter, tokens);
		this.context = context;
		mAxis = axis;
	}

	/**
	 * Simple Construct constructor
	 * @param context - Context of the application.
	 * @param tokens - Array of string tokens objects representing the parameters to be
	 * displayed.
	 * @param values - Array of double values representing the values of each string token 
	 * parameter.
	 * @param axis - Index of the primary axis parameter (x-axis) in the token array.
	 */
	public ParameterListAdapter(Context context, String[] tokens, double[] values, int axis) {
		super(context, R.layout.item_function_parameter, getTokens(tokens, values));
		this.context = context;
		mAxis = axis;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ParameterToken rowItem = getItem(position);

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.item_function_parameter, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.parameterTextView);
			viewHolder.value = (EditText) rowView.findViewById(R.id.parameterValueText);
			viewHolder.radio = (RadioButton) rowView.findViewById(R.id.parameterAxisCheckButton);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(rowItem.getStringParameter());
		holder.value.setText(Double.toString(rowItem.getValue()));
		
		if(position == mAxis){
			holder.radio.setChecked(true);
		}
    
		return rowView;
	}
	
	/**
	 * Returns an array of parameter tokens initialized from the provided arguments.
	 * @param tokens - String array of parameter names.
	 * @param values - Double array of parameter values.
	 * @return Initialized array of ParameterTokens.
	 */
	public static ParameterToken[] getTokens(String[] tokens, double[] values){
		ParameterToken[] res = new ParameterToken[tokens.length];
		for(int i = 0; i < tokens.length; i++){
			res[i] = new ParameterToken(tokens[i], values[i]);
		}
		return res;
	}
}
