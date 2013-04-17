package com.ece.smartGallery.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Photo;

public class HomeGridAdapter extends BaseAdapter{

	private int count;
	private ArrayList<Photo> list;
	private Bitmap[] bitmaps;
	private Context context;
	private LayoutInflater layoutInflater;
	public HomeGridAdapter(Context context, ArrayList<Photo> list) {
		this.context = context;
		this.count = list.size();
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		bitmaps = new Bitmap[count];
		for(int i = 0; i < count; i++){
		    bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		}
	}

	@Override
	public int getCount() {
		return this.count;
	}

	@Override
	public Object getItem(int pos) {
		return bitmaps[pos];
	}

	@Override
	public long getItemId(int pos) {
		return list.get(pos).getId();
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		 View grid;
		 if (view==null) {
			 grid = new View(context);
			 grid = layoutInflater.inflate(R.layout.home_grid_layout, null); 
		 } else {
			 grid = (View)view; 
		 }
		    
		 ImageView imageView = (ImageView)grid.findViewById(R.id.grid_item_image);
		 imageView.setImageBitmap(bitmaps[pos]);
		 TextView geoTextView = (TextView)grid.findViewById(R.id.grid_item_geolocation);
		 TextView dateTextView = (TextView)grid.findViewById(R.id.grid_item_date);
		 geoTextView.setText(list.get(pos).getLocation());
		 dateTextView.setText(list.get(pos).getTimeStamp() + "");   
		 return grid;
	}
}
