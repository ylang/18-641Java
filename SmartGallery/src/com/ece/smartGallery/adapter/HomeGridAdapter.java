package com.ece.smartGallery.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.smartGallery.R;
import com.ece.smartGallery.activity.DisplayActivity;
import com.ece.smartGallery.entity.Album;
import com.ece.smartGallery.entity.Photo;

public class HomeGridAdapter extends BaseAdapter {
	private final String TAG = this.getClass().getName();
	private int count;
	private List<Photo> list;
	private Context context;
	private LayoutInflater layoutInflater;

	public HomeGridAdapter(Context context, List<Photo> list) {
		this.context = context;
		this.count = list.size();
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return this.count;
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return list.get(pos).getId();
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		View grid;
		if (view == null) {
			grid = new View(context);
			grid = layoutInflater.inflate(R.layout.home_grid_layout, null);
		} else {
			grid = (View) view;
		}
		final Photo photo = list.get(pos);
		ImageView imageView = (ImageView) grid
				.findViewById(R.id.grid_item_image);
		LoadAsyncTask task = new LoadAsyncTask(imageView, photo);
		task.execute();
		TextView geoTextView = (TextView) grid
				.findViewById(R.id.grid_item_geolocation);
		TextView dateTextView = (TextView) grid
				.findViewById(R.id.grid_item_date);
		geoTextView.setText(photo.getLocation());
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MMM-dd",
				Locale.getDefault());
		dateTextView.setText(sdf.format(new Date(photo.getTimeStamp())));
		return grid;
	}

	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 200;

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
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DisplayActivity.class);
				intent.putExtra(Photo.PHOTO, photo);
				Log.d(TAG,
						"start display activity, photo id = " + photo.getId());
				context.startActivity(intent);
			}
		});
	}
	
	class LoadAsyncTask extends AsyncTask<Void, Void, Void> {
		
		private Photo photo;
		private ImageView view;
		private Bitmap bitmap;
		LoadAsyncTask(ImageView view, Photo photo) {
			this.photo = photo;
			this.view = view;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			File file = new File(photo.getImage().getPath());
			Bitmap b = decodeFile(file);
			Matrix mat = new Matrix();
			mat.postRotate(getCameraPhotoOrientation(context,
					photo.getImage(), file.getAbsolutePath()));
			bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
					b.getHeight(), mat, true);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			setImage(view, bitmap, photo);
			Log.d(TAG, "onPostExecute");
		}
		
	}
}
