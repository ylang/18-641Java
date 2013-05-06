package com.ece.smartGallery.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.activity.bluetooth.BluetoothChat;
import com.ece.smartGallery.activity.fb.FBActivity;
import com.ece.smartGallery.entity.Photo;

public class DisplayActivity extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	private final String TAG = this.getClass().getName();
	private static String mFileName = null;

	private Button mPlayButton = null;
	private MediaPlayer mPlayer = null;
	private boolean mStartPlaying = true;
	private Photo photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		Intent intent = getIntent();

		photo = (Photo) intent.getSerializableExtra(Photo.PHOTO);

		mFileName = photo.getVoice();

		String t = photo.getText();
		if (t != null && !t.isEmpty()) {
			TextView text_comment = (TextView) findViewById(R.id.display_text_comment);
			text_comment.setText(photo.getText());
		}
		updatePhotoImage();

		String location = photo.getLocation();
		if (location != null && !location.isEmpty()) {
			String text = "Picture taken in: " + location;
			TextView tv = (TextView) findViewById(R.id.display_geolocation);
			tv.setText(text);
		}

		mPlayButton = (Button) findViewById(R.id.play_voice_comment);
		mPlayButton.setText("Start playing");
		mPlayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mFileName != null && !mFileName.isEmpty())
					onPlay(mStartPlaying);
				if (mStartPlaying) {
					mPlayButton.setText("Stop playing");
				} else {
					mPlayButton.setText("Start playing");
				}
				mStartPlaying = !mStartPlaying;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_share_via_fb:
			intent = new Intent(this, FBActivity.class);
			intent.putExtra(Photo.IMAGE, photo.getImage());
			startActivity(intent);
			return true;
		case R.id.action_share_via_nfc:
			intent = new Intent(this, BeamActivity.class);
			intent.putExtra(Photo.PHOTO, photo);
			startActivity(intent);
			return true;
		case R.id.action_share_via_bluetooth:
			intent = new Intent(this, BluetoothChat.class);
			intent.putExtra(Photo.PHOTO, photo);
			intent.setAction(Intent.ACTION_SEND);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void updateScratch() {
		//Make sure the scratch is updated first.
		if (photo.getScratchURI() != null) {
			LinearLayout s = (LinearLayout) findViewById(R.id.scratchBlock);
			s.setVisibility(View.VISIBLE);
			ImageView scratchView = ((ImageView) findViewById(R.id.display_scratch));
			LoadAsyncTaskScratch taskS = new LoadAsyncTaskScratch(scratchView,
					photo, this);
			taskS.execute();
		}
	}

	private void updatePhotoImage() {
		ImageView imageView = ((ImageView) findViewById(R.id.display_image));
		LoadAsyncTask task = new LoadAsyncTask(imageView, photo, this);
		task.execute();
	}

	class PlayButton extends Button {
		boolean mStartPlaying = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying) {
					setText("Stop playing");
				} else {
					setText("Start playing");
				}
				mStartPlaying = !mStartPlaying;
			}
		};

		public PlayButton(Context ctx) {
			super(ctx);
			setText("Start playing");
			setOnClickListener(clicker);
		}
	}

	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 600;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private int getCameraPhotoOrientation(Context context, Uri imageUri,
			String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.v(TAG, "Exif orientation: " + orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	private void setImage(ImageView view, Bitmap b, final Photo photo) {
		view.setImageBitmap(b);
		ScrollView v = (ScrollView) findViewById(R.id.scrollView);
		v.fullScroll(ScrollView.FOCUS_UP);
	}

	class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

		private Photo photo;
		private ImageView view;
		private Bitmap bitmap;
		private Context context;

		LoadAsyncTask(ImageView view, Photo photo, Context context) {
			this.photo = photo;
			this.view = view;
			this.context = context;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			File file = new File(photo.getImage().getPath());
			Bitmap b = decodeFile(file);
			Matrix mat = new Matrix();
			mat.postRotate(getCameraPhotoOrientation(context, photo.getImage(),
					file.getAbsolutePath()));
			bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
					mat, true);
			return null;
		}

		@Override
		protected void onPostExecute(Void arg0) {
			setImage(view, bitmap, photo);
			Log.d(TAG, "onPostExecute");
			updateScratch();
		}
	}

	class LoadAsyncTaskScratch extends AsyncTask<Void, Void, Void> {

		private Photo photo;
		private ImageView view;
		private Bitmap bitmap;
		private Context context;

		LoadAsyncTaskScratch(ImageView view, Photo photo, Context context) {
			this.photo = photo;
			this.view = view;
			this.context = context;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			File file = new File(photo.getScratchURI().getPath());
			Bitmap b = decodeFile(file);
			Matrix mat = new Matrix();
			mat.postRotate(getCameraPhotoOrientation(context, photo.getImage(),
					file.getAbsolutePath()));
			bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
					mat, true);
			return null;
		}

		@Override
		protected void onPostExecute(Void arg0) {
			setImage(view, bitmap, photo);
			Log.d(TAG, "onPostExecute");
		}

	}

	public void to_edit_page(View view) {
		Intent intent = new Intent(this, EditActivity.class);
		if (this.photo != null) {
			intent.putExtra(Photo.PHOTO, this.photo);
			intent.setAction(Intent.ACTION_EDIT);
			startActivity(intent);
			finish();
		}
	}

	public void to_home_page(View view) {
		finish();
	}

}
