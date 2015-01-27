package com.dev.opencalc.utils;

import android.content.Intent;
import android.net.Uri;

/**
 * Utility class containing functions for basic android functionality.
 * @author John Riley
 *
 */
public class AppUtils {
	/**
	 * Returns an intent designated for the play store page for this application.
	 * @param packageName - Package name of the application.
	 * @return
	 */
	public static Intent getPlayStoreIntent(String packageName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + packageName));
		return intent;
	}
	
	/**
	 * Returns an intent designated for sending an email to the provided recipient.
	 * @param to - Email address of the recipient.
	 * @param subj - Subject of the email.
	 * @param body - Content of the email.
	 * @return
	 */
	public static Intent getEmailIntent(String to, String subj, String body){
		return getEmailIntent(new String[]{to}, subj, body);
	}
	
	/**
	 * Returns an intent designated for sending an email to the provided recipient.
	 * @param to - Email address' of the recipients.
	 * @param subj - Subject of the email.
	 * @param body - Content of the email.
	 * @return
	 */
	public static Intent getEmailIntent(String[] to, String subj, String body){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , to);
		i.putExtra(Intent.EXTRA_SUBJECT, subj);
		i.putExtra(Intent.EXTRA_TEXT   , body);
		
		return Intent.createChooser(i, "Send mail\u2026");
	}
}
