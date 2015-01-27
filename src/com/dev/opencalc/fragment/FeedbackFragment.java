package com.dev.opencalc.fragment;

import com.dev.opencalc.R;
import com.dev.opencalc.utils.AppUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Fragment for entering feedback about the application experience.
 * @author John Riley
 *
 */
public class FeedbackFragment extends SupportFragment {
	
	private EditText mSubject, mFeedback;
	private Spinner mFeedbackType;
	private Button mSubmitButton;
	
	public static FeedbackFragment newInstance(){
		return new FeedbackFragment();
	}

	public FeedbackFragment(){}

	@Override
	protected int getLayoutId() { return R.layout.fragment_feedback; }

	@Override
	protected void initializeFragment(View rootView, Bundle args) {
		initializeDefaultFragment(rootView);
	}

	@Override
	protected void initializeDefaultFragment(View rootView) {
		mSubject = (EditText) rootView.findViewById(R.id.feedbackSubjectText);
		mFeedback = (EditText) rootView.findViewById(R.id.feedbackContentText);
		mFeedbackType = (Spinner) rootView.findViewById(R.id.feedbackCategorySpinner);
		mSubmitButton = (Button) rootView.findViewById(R.id.feedbackAddButton);
		
		mSubmitButton.setOnClickListener(SUBMIT_LISTENER);
	}
	
	public void onSubmit(View view){
		String subj = mSubject.getText().toString();
		String content = mFeedback.getText().toString();
		String type = mFeedbackType.getSelectedItem().toString();	
		String to = view.getContext().getString(R.string.email_app_feedback);
		
		Intent mailIntent = AppUtils.getEmailIntent(to, type + ": " + subj, content);
		startActivity(mailIntent);
		
		Toast.makeText(view.getContext(), "Sending feedback report\u2026", Toast.LENGTH_SHORT).show();
	}
	
	private final Button.OnClickListener SUBMIT_LISTENER = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			onSubmit(v);
		}
	};
}
