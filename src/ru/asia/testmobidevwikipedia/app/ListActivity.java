package ru.asia.testmobidevwikipedia.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends Activity {

	List<String> list;
	ArrayAdapter<String> adapter;

	public ListActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		ListView lvResult = (ListView) findViewById(R.id.lvResult);

		Intent intent = getIntent();
		String result = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);
		List<String> array = new ArrayList<>();
		try {
			array = getTextFromJson(result);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (array != null) {
			adapter = new WikiListAdapter(this, R.id.lvResult, array);
			lvResult.setAdapter(adapter);
		}
	}

	private List<String> getTextFromJson(String result) throws JSONException,
			IOException {
		final String QUERY_TAG = "query";
		final String PAGES_TAG = "pages";
		final String TITLE_TAG = "title";
		final String EXTRACT_TAG = "extract";

		JSONObject resultJson = new JSONObject(result);
		JSONObject query = resultJson.getJSONObject(QUERY_TAG);
		JSONObject pages = query.getJSONObject(PAGES_TAG);
		Iterator<String> iterator = pages.keys();
		String id_tag = (String) iterator.next();
		Log.e("id_tag", id_tag);
		JSONObject id = pages.getJSONObject(id_tag);
		String title = id.getString(TITLE_TAG);
		String extract = id.getString(EXTRACT_TAG);
		Log.e("Title", title);
		Log.e("Extact", extract);
		List<String> listResult = new ArrayList<String>();
		listResult.add(title);
		listResult.add(extract);
		Log.e("LIST", listResult.toString());
		return listResult;
	}
}
