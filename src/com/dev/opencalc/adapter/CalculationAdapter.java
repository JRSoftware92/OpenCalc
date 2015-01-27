package com.dev.opencalc.adapter;

import com.dev.opencalc.R;
import com.dev.opencalc.model.Calculation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * Array Adapter for Calculations.
 * @author John Riley
 * 
 */
public class CalculationAdapter extends ArrayAdapter<Calculation> {
	
	private final Context context;

	static class ViewHolder {
		public TextView mExpression;
		public TextView mResult;
	}

	/**
	 * Standard constructor.
	 * @param context - Context of the application.
	 * @param history - Array of calculation objects.
	 */
	public CalculationAdapter(Context context, Calculation[] history) {
		super(context, R.layout.calculation_history_item, history);
		this.context = context;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Calculation rowItem = getItem(position);

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.calculation_history_item, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.mExpression= (TextView) rowView.findViewById(R.id.historyItemExpressionView);
			viewHolder.mResult = (TextView) rowView.findViewById(R.id.historyItemResultView);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.mExpression.setText(rowItem.getStringStatement());
		holder.mResult.setText(rowItem.getStringResult());
    
		return rowView;
	}
} 
