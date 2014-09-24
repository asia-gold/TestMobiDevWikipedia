package ru.asia.testmobidevwikipedia.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class WikiIntentService extends IntentService {
	
	private String resultJson;

	public WikiIntentService() {
		super(WikiIntentService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String theQuest = intent.getStringExtra(Intent.EXTRA_TEXT);
		String action = "query";
		String format = "json";
		String prop = "extracts";
		String titles = theQuest;
		String exintro = "1";

		try {
			final String SEARCH_URL = "http://en.wikipedia.org/w/api.php?";

			final String ACTION_PARAM = "action";
			final String FORMAT_PARAM = "format";
			final String PROP_PARAM = "prop";
			final String TITLES_PARAM = "titles";
			final String EXINTRO_PARAM = "exintro";

			Uri builtUri = Uri.parse(SEARCH_URL).buildUpon()
					.appendQueryParameter(ACTION_PARAM, action)
					.appendQueryParameter(FORMAT_PARAM, format)
					.appendQueryParameter(PROP_PARAM, prop)
					.appendQueryParameter(TITLES_PARAM, titles)
					.appendQueryParameter(EXINTRO_PARAM, exintro).build();

			URL url = new URL(builtUri.toString());

			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				resultJson = null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}

			if (buffer.length() == 0) {
				resultJson = null;
			}
			resultJson = buffer.toString();

			Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
					.putExtra(Constants.EXTENDED_DATA_STATUS, resultJson);
			LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
