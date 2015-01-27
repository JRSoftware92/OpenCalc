package com.dev.opencalc.activity;

import com.dev.opencalc.R;
import com.dev.opencalc.fragment.TextFileFragment;

import android.os.Bundle;

/**
 * Activity for displaying credits relating to the development of the application.
 * @author John Riley
 *
 */
public class CreditsActivity extends SupportFragmentActivity {
	
	public static final float TEXT_SIZE = 18;
	
	protected TextFileFragment mFragment;

	@Override
	protected int getLayoutId() { return R.layout.activity_fragment; }

	@Override
	protected int getMenuId() { return R.menu.credits; }

	@Override
	protected void initializeDefaultActivity() {
		mFragment = TextFileFragment.newInstance(this, R.raw.credits, TEXT_SIZE);
		addFragment(R.id.fragmentContainer, mFragment);
	}

	@Override
	protected void initializeActivity(Bundle args) {
		initializeDefaultActivity();
	}
}
