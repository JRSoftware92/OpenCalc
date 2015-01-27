package com.dev.opencalc.activity;

import com.dev.opencalc.R;
import com.dev.opencalc.fragment.FeedbackFragment;

import android.os.Bundle;

/**
 * Activity for encapsulating feedback fragment.
 * @author John Riley
 *
 */
public class FeedbackActivity extends SupportFragmentActivity {
	
	protected FeedbackFragment mFragment;

	@Override
	protected int getLayoutId() { return R.layout.activity_fragment; }

	@Override
	protected int getMenuId() { return R.menu.credits; }

	@Override
	protected void initializeDefaultActivity() {
		mFragment = FeedbackFragment.newInstance();
		addFragment(R.id.fragmentContainer, mFragment);
	}

	@Override
	protected void initializeActivity(Bundle args) {
		initializeDefaultActivity();
	}
}
