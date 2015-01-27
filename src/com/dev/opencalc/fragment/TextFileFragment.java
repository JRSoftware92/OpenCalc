package com.dev.opencalc.fragment;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * Fragment for loading a text file into a text view.
 * @author John Riley
 *
 */
public class TextFileFragment extends SimpleTextFragment {
	
	private static final String TEXT_SIZE_KEY = "com.dev.textSize";
	
	public static TextFileFragment newInstance(Context context, int resId){
		TextFileFragment frag = new TextFileFragment();
		String s = loadFile(context, resId);
		if(!s.equals("")){
			Bundle args = new Bundle();
			args.putString(DATA_KEY, s);
			frag.setArguments(args);
		}
		return frag;
	}
	
	public static TextFileFragment newInstance(Context context, int resId, float size){
		TextFileFragment frag = new TextFileFragment();
		String s = loadFile(context, resId);
		if(!s.equals("")){
			Bundle args = new Bundle();
			args.putString(DATA_KEY, s);
			args.putFloat(TEXT_SIZE_KEY, size);
			frag.setArguments(args);
		}
		return frag;
	}

	@Override
	protected void initializeFragment(View rootView, Bundle args){
		super.initializeFragment(rootView, args);
		if(args.containsKey(TEXT_SIZE_KEY)){
			mTextView.setTextSize(args.getFloat(TEXT_SIZE_KEY));
		}
	}
	
	public static String loadFile(Context context, int resId){
		try {
	        Resources res = context.getResources();
	        InputStream input = res.openRawResource(resId);

	        byte[] bytes = new byte[input.available()];
	        input.read(bytes);
	        return new String(bytes);
	    } 
		catch (Exception e) {
	    	return "Error loading file.";
	    }
	}
}
