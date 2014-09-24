package ru.asia.testmobidevwikipedia.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WikiIntentReceiver extends BroadcastReceiver {
	
	String finalResult;

	public WikiIntentReceiver() {
	}

	@Override
	public void onReceive(Context contex, Intent intent) {
		switch (intent.getAction()) {
			case Constants.BROADCAST_ACTION: {
				finalResult = intent
					.getStringExtra(Constants.EXTENDED_DATA_STATUS);
								
				Intent listIntent = new Intent();
				listIntent.setClassName("ru.asia.testmobidevwikipedia.app", "ru.asia.testmobidevwikipedia.app.ListActivity");
				listIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				listIntent.putExtra(Constants.EXTENDED_DATA_STATUS, finalResult);
		        contex.startActivity(listIntent);
			}
		}
	}
}
