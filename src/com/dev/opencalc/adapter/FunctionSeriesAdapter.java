package com.dev.opencalc.adapter;

import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.dev.opencalc.R;
import com.dev.opencalc.model.FunctionSeries;

/**
 * WORK IN PROGRESS
 * Table Adapter for a function series.
 * @author John Riley
 *
 */
public class FunctionSeriesAdapter extends BaseAdapter {

	private final Context context;
	private final FunctionSeries series;
	private Double[] entries;
	
	static class ViewHolder{
		TableRow tableRow;
	}

	public FunctionSeriesAdapter(Context context, FunctionSeries series) {
		super();
		this.context = context;
		this.series = series;
		
		Set<Double> keys = series.entrySet();
		entries = new Double[keys.size()];
		entries = keys.toArray(entries);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		TextView mXColumn = new TextView(context);
		TextView mYColumn = new TextView(context);

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.table_item_function_series, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tableRow = (TableRow) rowView.findViewById(R.id.functionTableRow);
			
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		if(position > 0){
			mXColumn.setText(Double.toString(entries[position - 1]));
			mYColumn.setText(series.get(entries[position - 1]));
		}
		else{
			mXColumn.setText(series.getXAxisLabel());
			mYColumn.setText(series.getYAxisLabel());
		}
    
		holder.tableRow.addView(mXColumn);
		holder.tableRow.addView(mYColumn);
		return rowView;
	}

	@Override
	public int getCount() { return series == null ? 0 : series.size() + 1; }

	@Override
	public Object getItem(int position) {
		return position == 0 ? 0 : entries[position - 1];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
