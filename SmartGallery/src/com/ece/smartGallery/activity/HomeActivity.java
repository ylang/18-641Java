package com.ece.smartGallery.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ece.smartGallery.R;
import com.ece.smartGallery.adapter.HomeGridAdapter;
import com.ece.smartGallery.db.DatabaseHandler;
import com.ece.smartGallery.entity.Album;
import com.ece.smartGallery.entity.Photo;

public class HomeActivity extends Activity {
	private LinearLayout addPhoto;
	private GridView gridView;
	private int albumId;
	private final String TAG = this.getClass().getName();
	private List<Photo> photoList;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Log.d(TAG, "on Create");
		this.addPhoto = (LinearLayout) findViewById(R.id.add_new_photo);
		this.addPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewPhoto();
			}
		});
		db = new DatabaseHandler(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = this.getIntent();
		if (intent != null) {
			albumId = intent.getIntExtra(Album.ALBUM, 0);
		}
		loadPhoto();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void addNewPhoto() {
		Album album = db.getAlbum(albumId);
		Intent intent = new Intent(this, EditActivity.class);
		intent.setAction(Intent.ACTION_INSERT);
		intent.putExtra(Album.ALBUM, album.getId());
		startActivity(intent);
	}

	public void loadPhoto() {
		photoList = db.getAllPhotos(albumId);
		Log.d(TAG, "Album retrieved successfully, length = " + photoList.size());
		gridView = (GridView) findViewById(R.id.gallery_list);
		HomeGridAdapter adapter = new HomeGridAdapter(this, this.photoList);
		gridView.setAdapter(adapter);
		Log.d(TAG, "grid view adapter set");
	}
}
