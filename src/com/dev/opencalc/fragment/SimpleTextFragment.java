package com.dev.opencalc.fragment;

import com.dev.opencalc.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * A Simple text fragment class.
 * @author John Riley
 * 
 */
public class SimpleTextFragment extends SupportFragment {
	
	protected static String DATA_KEY = "com.fragment.text.data";
	
	protected TextView mTextView = null;

	public static SimpleTextFragment newInstance(String text){
		SimpleTextFragment frag = new SimpleTextFragment();
		if(text != null){
			Bundle args = new Bundle();
			args.putString(DATA_KEY, text);
			frag.setArguments(args);
		}
		return frag;
	}
	
	@Override
	protected int getLayoutId() { return R.layout.fragment_simple_text; }
	
	@Override
	protected void initializeFragment(View rootView, Bundle args){
		mTextView = (TextView) rootView.findViewById(R.id.simpleTextView);
		if(args.containsKey(DATA_KEY)){
			String text = args.getString(DATA_KEY);
			if(text != null){
				mTextView.setText(text);
			}
		}
	}

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mTextView = (TextView) rootView.findViewById(R.id.simpleTextView);
		mTextView.setText("");
	}
	
	public TextView getTextView(){ return mTextView; }
	
	public void setTextView(TextView textView){ mTextView = textView; }
	
	public String getText(){
		if(mTextView != null){
			return mTextView.getText().toString();
		}
		else{
			return null;
		}
	}
	
	public void setText(String text){
		if(mTextView != null){
			mTextView.setText(text);
		}
	}
}
