package com.ece.smartGallery.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Album;
import com.ece.smartGallery.DBLayout.Photo;
import com.ece.smartGallery.adapter.HomeGridAdapter;
import com.ece.smartGallery.entities.Datastorage;

public class HomeActivity extends Activity {
	private GridView gridView;
	private Album album;
	private final String TAG = this.getClass().getName();
	private List<Photo> photoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		try {
			this.album = Album.getAlbum(this);
			Log.d(TAG,
					"Album retrieved successfully, length = "
							+ this.album.getCount());
			photoList = new ArrayList<Photo>(album.getCount());
			for (String fileName : this.album.getPhotoFiles()) {
				photoList.add(Datastorage.loagPhoto(this, fileName));
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(this, e.getMessage(),
					Toast.LENGTH_LONG);
			toast.show();
		}
		gridView = (GridView) findViewById(R.id.gallery_list);
		HomeGridAdapter adapter = new HomeGridAdapter(this, this.photoList);
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
