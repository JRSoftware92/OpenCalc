package com.dev.opencalc.adapter;

import com.dev.opencalc.R;
import com.dev.opencalc.model.FunctionMeta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Array Adapter for functions.
 * @author John Riley
 * 
 */
public class FunctionAdapter extends ArrayAdapter<FunctionMeta> {
	
	private final Context context;

	/**
	 * View holder class for function list items.
	 * @author John Riley
	 *
	 */
	static class ViewHolder {
		public TextView mName;
		public TextView mExpression;
		public TextView mToken;
	}

	/**
	 * Standard constructor.
	 * @param context - Context of the application.
	 * @param functions - Array of function objects to be displayed.
	 */
	public FunctionAdapter(Context context, FunctionMeta[] functions) {
		super(context, R.layout.function_list_item, functions);
		this.context = context;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		FunctionMeta rowItem = getItem(position);

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.function_list_item, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.mName = (TextView) rowView.findViewById(R.id.functionNameView);
			viewHolder.mExpression= (TextView) rowView.findViewById(R.id.functionExpressionView);
			viewHolder.mToken = (TextView) rowView.findViewById(R.id.functionTokenView);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.mExpression.setText(rowItem.getExpression());
		holder.mName.setText(rowItem.getName());
		holder.mToken.setText(rowItem.getTokenString() + rowItem.getParametersWithCommas());
    
		return rowView;
	}
} 
