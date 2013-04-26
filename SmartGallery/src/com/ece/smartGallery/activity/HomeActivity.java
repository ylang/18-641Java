package com.ece.smartGallery.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadPhoto();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void loadPhoto() {
		try {
			this.album = Album.getAlbum(this);
			if (album.getCount() == 0) {
				Photo p = new Photo();
				File path = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
				File sample = new File(path, "1.jpg");
				p.setImage(Uri.fromFile(sample));
				p.setLocation("Pittsburgh");
				p.setTimeStamp(System.currentTimeMillis());
				int id = album.addNewPhoto(this, p);
				//p.setId(id);
				album.addNewPhoto(this, p);
			}
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
		Log.d(TAG, "grid view adapter set");
	}
	
	private class LoadPhotoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			loadPhoto();
			return null;
		}
		
	}
	
	
	// this method is used to go to edit page directly to test more easily
	// will be removed once integrate all parts together.
	public void test_edit(View view){
		Intent intent = new Intent(this,EditActivity.class);
		startActivity(intent);
	}

}
