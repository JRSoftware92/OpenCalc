package com.dev.opencalc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * Abstract support fragment with safe initialization.
 * @author John Riley
 *
 */
public abstract class SupportFragment extends Fragment {

	public static Fragment newInstance(){
		Fragment frag = new Fragment();
		return frag;
	}
	
	protected SupportFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(getLayoutId(), container, false);
		Bundle args = getArguments();
		
		if(args == null){
			initializeDefaultFragment(rootView);
		}
		else{
			initializeFragment(rootView, args);
		}
		return rootView;
	}
	
	/**
	 * Accessor for the layout id for this given fragment.
	 * @return The integer id of the layout for the fragment.
	 */
	protected abstract int getLayoutId();
	
	/**
	 * Initializes the fragment with the given arguments.
	 * @param rootView - Inflated view of the fragment.
	 * @param args - Bundle of arguments provided to the fragment for initialization.
	 */
	protected abstract void initializeFragment(View rootView, Bundle args);
	
	/**
	 * Initializes the fragment with default values.
	 * @param rootView - Inflated view of the fragment.
	 */
	protected abstract void initializeDefaultFragment(View rootView);
}
