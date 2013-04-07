package com.example.artist;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		String video = getIntent().getStringExtra("video");
		VideoView view = (VideoView) findViewById(R.id.video);
		String path = "android.resource://" + getPackageName() + "/" + R.raw.never;
		if (!video.equals("Never say never")) {
			path = "android.resource://" + getPackageName() + "/" + R.raw.beauty;
		}
		view.setVideoURI(Uri.parse(path));
		view.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

}
