package ru.asia.testmobidevwikipedia.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	AlertDialog dialog;
	Button btnSearch;
	EditText etSearch;
	String finalResult;
	IntentFilter mStatusIntentFilter;
	WikiIntentReceiver mWikiIntentReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isInternetAvailable();

		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.etSearch);

		mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
		mWikiIntentReceiver = new WikiIntentReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mWikiIntentReceiver, mStatusIntentFilter);

	}

	private void isInternetAvailable() {
		boolean hasWifiNet = false;
		boolean hasMobileNet = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = manager.getAllNetworkInfo();
		for (NetworkInfo ni : networkInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected()) {
					hasWifiNet = true;
				}
			} else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected()) {
					hasMobileNet = true;
				}
			}
		}
		if (hasMobileNet || hasWifiNet) {
			// Everything just fine
		} else {
			showAlertDialog(this);
		}
	}

	public void showAlertDialog(Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle(Constants.ALERT_DIALOG_TITLE);
		alertDialogBuilder.setMessage(Constants.ALERT_DIALOG_MESSAGE);
		alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		dialog = alertDialogBuilder.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (etSearch.getText().toString().matches(Constants.EMPTY_STRING)) {
			Toast.makeText(this, "Поле поиска пусто", Toast.LENGTH_LONG).show();
		} else {
			wikiSearch(etSearch.getText().toString());
		}
	}

	private void wikiSearch(String theQuest) {
		Intent serviceIntent = new Intent(this, WikiIntentService.class);
		serviceIntent.putExtra(Intent.EXTRA_TEXT, theQuest);
		startService(serviceIntent);
	}

	@Override
	protected void onStop() {
		super.onStop();
		dialog.dismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialog.dismiss();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mWikiIntentReceiver);
	}
}
