package com.dev.opencalc.adapter;

import java.util.Collection;

import com.dev.opencalc.R;
import com.dev.opencalc.model.FunctionMeta;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Currently not used.
 * Expandable list adapter for functions and parameters.
 * @author John Riley
 *
 */
@Deprecated
public class ExpandableFunctionAdapter extends BaseExpandableListAdapter{
	
	//TODO Dialog for adding a new function with number of parameters.
	
	private Context mContext = null;
	private FunctionMeta[] mFunctions = null;
	private LayoutInflater mInflater = null;
	private OnValueChangedListener mListener = null;
	
	private int[] mActiveAxes = null;
	private double[][] mParams = null;

	public ExpandableFunctionAdapter(Context context, FunctionMeta[] functions) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		
		setFunctions(functions);
	}
	
	public ExpandableFunctionAdapter(Context context, Collection<FunctionMeta> functions){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		
		setFunctions(functions);
	}

	@Override
	public int getGroupCount() {
		return mFunctions == null ? 0 : mFunctions.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mFunctions == null){
			return 0;
		}
		else{
			return mFunctions[groupPosition].getNumberOfParameters();
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mFunctions == null ? null : mFunctions[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(mFunctions == null){
			return null;
		}
		else{
			if(mFunctions[groupPosition].getNumberOfParameters() == 0){
				return null;
			}
			else{
				return mFunctions[groupPosition].getParameterArray()[childPosition];
			}
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return mFunctions == null ? -1 : mFunctions[groupPosition].getId();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView text = new TextView(mContext);
		if(mFunctions == null){
			text.setText("");
		}
		else{
			text.setText(mFunctions[groupPosition].getName());
		}
		return text;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View rootView = mInflater.inflate(R.layout.item_function_parameter, parent, false);
		
		final int funcPos = groupPosition, paramPos = childPosition;
		
		TextView text = (TextView) rootView.findViewById(R.id.parameterTextView);
		RadioButton axis = (RadioButton) rootView.findViewById(R.id.parameterAxisCheckButton);
		EditText value = (EditText) rootView.findViewById(R.id.parameterValueText);
		
		text.setText(mFunctions[groupPosition].getParameterArray()[childPosition] + ": ");
		if(mListener != null){
			value.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s == null || s.toString().equals("")){
						s.clear();
						s.append("0");
					}
					try{
						double val = Double.parseDouble(s.toString());
						onParameterValueUpdated(funcPos, paramPos, val);
					}
					catch(NumberFormatException nfe){
						Toast.makeText(mContext, "Value entered wasn't a number.", Toast.LENGTH_SHORT).show();
						s.append("0");
					}
				}
			});
		}
		if(mActiveAxes[groupPosition] == childPosition){
			axis.setChecked(true);
		}
		axis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mListener != null){
					mListener.onPrimaryAxisSet(funcPos, paramPos);
				}
			}
		});
		return rootView;
	}
	
	public int getActiveParameterIndex(int groupPosition){
		if(mFunctions == null){
			return 0;
		}
		else{
			return mActiveAxes[groupPosition];
		}
	}
	
	public char getActiveParameter(int groupPosition){
		if(mFunctions == null){
			return 0;
		}
		else{
			return mFunctions[groupPosition].getParameterArray()[mActiveAxes[groupPosition]];
		}
	}
	
	public void setActiveParameterIndex(int groupPosition, int index){
		if(mActiveAxes != null && index < mActiveAxes.length && index > -1){
			mActiveAxes[groupPosition] = index;
		}
	}

	public void setFunctions(FunctionMeta[] arr){ 
		mFunctions = arr; 
		if(arr != null){
			mActiveAxes = new int[arr.length];
		}
		else{
			mActiveAxes = null;
		}
		
		for(int i = 0; i < mActiveAxes.length; i++){
			mActiveAxes[i] = 0;
		}
		
		mParams = new double[mFunctions.length][];
		for(int i = 0; i < mFunctions.length; i++){
			mParams[i] = new double[mFunctions[i].getNumberOfParameters()];
		}
	}
	
	public void setFunctions(Collection<FunctionMeta> funcs){
		if(funcs != null){
			mFunctions = new FunctionMeta[funcs.size()];
			mActiveAxes = new int[mFunctions.length];
			int count = 0;
			mParams = new double[mFunctions.length][];
			for(FunctionMeta func : funcs){
				mActiveAxes[count] = 0;
				mParams[count] = new double[func.getNumberOfParameters()];
				mFunctions[count++] = func;
			}
		}
		else{
			mFunctions = null;
		}
	}
	
	public void setOnValueChangedListener(OnValueChangedListener listener){
		mListener = listener;
	}

	public void onParameterValueUpdated(int groupPosition, int childPosition, double value) {
		mParams[groupPosition][childPosition] = value;
		if(mListener != null){
			mListener.onParameterValueUpdated(groupPosition, childPosition, value);
		}
	}
	
	public interface OnValueChangedListener {
		public void onParameterValueUpdated(int funcPos, int paramPos, double value);
		
		public void onPrimaryAxisSet(int funcPos, int paramPos);
	}
}
