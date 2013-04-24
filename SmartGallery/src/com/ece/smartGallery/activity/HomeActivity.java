package com.ece.smartGallery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.adapter.HomeGridAdapter;

public class HomeActivity extends Activity {
	GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gridView = (GridView) findViewById(R.id.gallery_list);
		HomeGridAdapter adapter = new HomeGridAdapter(this, null);
        gridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
