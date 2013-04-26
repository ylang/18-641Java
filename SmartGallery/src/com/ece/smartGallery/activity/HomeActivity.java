package com.ece.smartGallery.activity;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Album;
import com.ece.smartGallery.DBLayout.Photo;
import com.ece.smartGallery.adapter.HomeGridAdapter;
import com.ece.smartGallery.entities.DatabaseHandler;

public class HomeActivity extends Activity {
	private LinearLayout addPhoto;
	private GridView gridView;
	private int albumId;
	private final String TAG = this.getClass().getName();
	private List<Photo> photoList;
	private ProgressDialog progressDialog;
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
		// loadPhoto();
		LoadPhotoTask load = new LoadPhotoTask();
		load.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void addNewPhoto() {
		Photo p = new Photo();
		File path = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File sample = new File(path, "1.jpg");
		p.setImage(Uri.fromFile(sample));
		p.setLocation("Pittsburgh");
		p.setTimeStamp(System.currentTimeMillis());
		Album album = db.getAlbum(albumId);
		boolean success = db.addPhoto(album, p);
		if (success) {
			Log.d(TAG, "add new photo success!");
		}
		this.loadPhoto();

	}

	public void loadPhoto() {
		photoList = db.getAllPhotos(albumId);
		Log.d(TAG, "Album retrieved successfully, length = " + photoList.size());
		gridView = (GridView) findViewById(R.id.gallery_list);
		HomeGridAdapter adapter = new HomeGridAdapter(this, this.photoList);
		gridView.setAdapter(adapter);
		Log.d(TAG, "grid view adapter set");
	}

	private class LoadPhotoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			/*
			 * This is executed on UI thread before doInBackground(). It is the
			 * perfect place to show the progress dialog.
			 */
			progressDialog = ProgressDialog.show(HomeActivity.this, "",
					"Loading...");
			Log.d(TAG, "onPreExecute");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadPhoto();
				}

			});

			return null;
		}

		@Override
		protected void onPostExecute(Void arg0) {
			progressDialog.dismiss();
			Log.d(TAG, "onPostExecute");
		}

	}

	// this method is used to go to edit page directly to test more easily
	// will be removed once integrate all parts together.
	public void test_edit(View view) {
		Intent intent = new Intent(this, EditActivity.class);
		startActivity(intent);
	}

}
