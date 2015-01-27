package com.dev.opencalc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * Generic abstract List fragment class for displaying a list of data.
 * @author John Riley
 *
 * @param <T> Type of object to be displayed in the list fragment.
 */
public abstract class SupportListFragment<T> extends SupportFragment {

	protected T[] mData = null;
	protected ListView mListView = null;
	protected OnListItemClickListener mListener = null;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnListItemClickListener) activity;
		} 
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnListItemClickListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(mListView);
	}
	
	@Override
	protected void initializeDefaultFragment(View rootView){
		mListView = (ListView) rootView.findViewById(getListViewId());
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListener.onListItemClick(position, id);
			}
		});
	}
	
	public T getItem(int index){
		if(mData == null || index < -1 || index > mData.length){
			return null;
		}
		else{
			return mData[index];
		}
	}
	
	public T[] getListArray(){ return mData; }
	
	public void setList(T[] data){ 
		mData = data; 
		setListAdapter(getAdapter(data));
	}
	
	public void setListView(ListView listView){ mListView = listView; }
	
	public void setListAdapter(ArrayAdapter<T> adapter){ mListView.setAdapter(adapter); }
	
	public void setOnListItemClickListener(OnListItemClickListener listener){
		mListener = listener;
	}
	
	protected void onListItemClick(int pos, long id){
		if(mListener != null){
			mListener.onListItemClick(pos, id);
		}
	}
	
	protected abstract int getListViewId();
	
	protected abstract ArrayAdapter<T> getAdapter(T[] data);
	
	public interface OnListItemClickListener {
		public void onListItemClick(int pos, long id);
	}
}
