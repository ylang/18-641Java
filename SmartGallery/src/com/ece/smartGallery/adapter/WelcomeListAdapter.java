package com.ece.smartGallery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.entity.Album;

public class WelcomeListAdapter extends BaseAdapter {

	private List<Album> albumList;
	private LayoutInflater layoutInflater;
	private final String TAG = this.getClass().getName();
	private Context context;

	public WelcomeListAdapter(Context context, List<Album> albumList) {
		this.layoutInflater = LayoutInflater.from(context);
		this.albumList = albumList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return albumList.size();
	}

	@Override
	public Object getItem(int pos) {
		return albumList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return albumList.get(pos).getId();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = layoutInflater.inflate(R.layout.welcome_list_layout, null);
		}
		TextView name = (TextView) row.findViewById(R.id.list_item_name);
		TextView count = (TextView) row.findViewById(R.id.list_item_count);

		final Album album = this.albumList.get(pos);
		name.setText(album.getName());
		int cnt = album.getCount();
		if (cnt == 0) {
			count.setText("empty album");
		} else if (cnt == 1) {
			count.setText("contains 1 photo");
		} else {
			count.setText("contains " + cnt + " photos");
		}
		return row;
	}

}
