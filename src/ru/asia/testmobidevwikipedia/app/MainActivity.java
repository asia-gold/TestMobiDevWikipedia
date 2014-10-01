package ru.asia.testmobidevwikipedia.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnSearch;
	EditText etSearch;
	String finalResult;
	IntentFilter mStatusIntentFilter;
	WikiIntentReceiver mWikiIntentReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkInternetAvailable();

		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.etSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (etSearch.getText().toString()
						.matches(Constants.EMPTY_STRING)) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.str_no_text),
							Toast.LENGTH_LONG).show();
				} else {
					wikiSearch(etSearch.getText().toString());
				}
			}
		});
		
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					wikiSearch(etSearch.getText().toString());
				}
				return false;
			}
		});

		mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
		mWikiIntentReceiver = new WikiIntentReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mWikiIntentReceiver, mStatusIntentFilter);

	}
	
	private void checkInternetAvailable() {
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
			Toast.makeText(this,
					getResources().getString(R.string.str_intenet),
					Toast.LENGTH_LONG).show();
		}
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

	private void wikiSearch(String theQuest) {
		Intent serviceIntent = new Intent(this, WikiIntentService.class);
		serviceIntent.putExtra(Intent.EXTRA_TEXT, theQuest);
		startService(serviceIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mWikiIntentReceiver);
	}
}
