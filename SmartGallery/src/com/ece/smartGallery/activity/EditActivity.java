package com.ece.smartGallery.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Photo;

public class EditActivity extends Activity {

	private static final String LOG_TAG = "AudioRecordTest";
	private String mFileName = null;

	private Button mRecordButton = null;
	private MediaRecorder mRecorder = null;
	private boolean mStartRecording = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		Intent intent = getIntent();

		Photo photo = (Photo) intent.getSerializableExtra(Photo.PHOTO);
		if (photo != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.display_image));
			LoadAsyncTask task = new LoadAsyncTask(imageView, photo, this);
			task.execute();

			mFileName = photo.getVoice();
		} else {

			mFileName = GetVoiceCommentPath();
		}

		mRecordButton = (Button) findViewById(R.id.add_voice_button);
		mRecordButton.setText("Start recording");
		mRecordButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onRecord(mStartRecording, mFileName);
				if (mStartRecording) {
					mRecordButton.setText("Stop recording");
				} else {
					mRecordButton.setText("Start recording");
				}
				mStartRecording = !mStartRecording;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	public void save(View view) {
		Intent intent = new Intent(this, DisplayActivity.class);

		// retrieve user input
		String input_text_comment = ((EditText) findViewById(R.id.edit_comment_input))
				.getText().toString();

		Photo photo = new Photo();
		photo.setText(input_text_comment);
		File path = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File sample = new File(path, "1.jpg");
		photo.setImage(Uri.fromFile(sample));
		photo.setVoice(mFileName);

		intent.putExtra(Photo.PHOTO, photo);

		startActivity(intent);
	}

	private void onRecord(boolean start, String mFileName) {
		if (start) {
			startRecording(mFileName);
		} else {
			stopRecording();
		}
	}

	private void startRecording(String mFileName) {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	public String GetVoiceCommentPath() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		path = path + "/" + UUID.randomUUID().toString() + ".3gp";
		return path;
	}

	public void scratch(View view) {
		Intent intent = new Intent(this, ScratchActivity.class);
		Photo photo = new Photo();
		// photo.setText(input_text_comment);
		File path = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File sample = new File(path, "1.jpg");
		photo.setImage(Uri.fromFile(sample));
		photo.setVoice(mFileName);

		intent.putExtra(Photo.PHOTO, photo);
		startActivity(intent);
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

			Log.v(LOG_TAG, "Exif orientation: " + orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	private void setImage(ImageView view, Bitmap b, final Photo photo) {
		view.setImageBitmap(b);
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
			Log.d(LOG_TAG, "onPostExecute");
		}

	}

}
