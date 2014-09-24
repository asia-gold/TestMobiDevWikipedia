package ru.asia.testmobidevwikipedia.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WikiListAdapter extends ArrayAdapter<String>{
	
	Context context;
	LayoutInflater inflater;
	List<String> list = new ArrayList<>();
	
	public WikiListAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		list = objects;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	public String getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item, parent, false);
		}
		String s = getItem(position);
		
		((TextView)	view.findViewById(R.id.text)).setText(Html.fromHtml(s));
		return view;
	}

}
