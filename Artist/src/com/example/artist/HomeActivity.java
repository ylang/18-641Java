package com.example.artist;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class HomeActivity extends Activity {

	private MediaPlayer mediaPlayer;
	private boolean playerPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ImageView profileView = (ImageView) findViewById(R.id.profile);
		loadFromAssets(profileView);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		Button baby = (Button) findViewById(R.id.music_button);
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
				.createFromResource(this, R.array.music_array,
						android.R.layout.simple_spinner_item);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		Spinner videoSpinner = (Spinner) findViewById(R.id.video_spinner);
		
		ArrayAdapter<CharSequence> videoAdapter = ArrayAdapter
				.createFromResource(this, R.array.video_array,
						android.R.layout.simple_spinner_item);
		videoAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		videoSpinner.setAdapter(videoAdapter);

		baby.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (playerPlaying == false) {
					// not playing yet
					String music = ((Spinner) findViewById(R.id.spinner))
							.getSelectedItem().toString();
					String musicFile;
					if (music.equals("Baby")) {
						musicFile = "baby.mp3";
					} else {
						musicFile = "boyfriend.mp3";
					}
					playerPlaying = true;
					((Button) v).setText(getApplication().getResources()
							.getString(R.string.stop));
					playAudio(musicFile);
				} else {
					// playing
					stopAudio();
					((Button) v).setText(getApplication().getResources()
							.getString(R.string.play));
					playerPlaying = false;
				}
			}
		});
		Button video = (Button) findViewById(R.id.video_button);
		video.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), VideoActivity.class);
				String video = ((Spinner) findViewById(R.id.video_spinner))
						.getSelectedItem().toString();
				intent.putExtra("video", video);
				startActivity(intent);
			}
		});
		
		Button gallery = (Button) findViewById(R.id.gallery_button);
		gallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(), GalleryActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button mailList = (Button) findViewById(R.id.mail_button);
		mailList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplication(), SubscribeActivity.class));
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		killMediaPlayer();
	}

	private void loadFromAssets(ImageView view) {
		try {
			InputStream ims = getAssets().open("jb_png.png");
			Drawable d = Drawable.createFromStream(ims, null);
			view.setImageDrawable(d);
		} catch (IOException ex) {
			return;
		}
	}

	private void playAudio(String fileName) {
		killMediaPlayer();
		mediaPlayer = new MediaPlayer();
		try {
			AssetFileDescriptor descriptor = getAssets().openFd(fileName);
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void stopAudio() {
		mediaPlayer.stop();
		killMediaPlayer();
	}

	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
