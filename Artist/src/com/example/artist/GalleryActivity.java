package com.example.artist;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GalleryActivity extends Activity {
	private Gallery gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		gallery = (Gallery) findViewById(R.id.gallery);

		// load images into memory
		Bitmap[] mBitArray = new Bitmap[4];
		try {
			// these images are stored in the root of "assets"
			mBitArray[0] = getBitmapFromAsset("1.jpg");
			mBitArray[1] = getBitmapFromAsset("2.jpg");
			mBitArray[2] = getBitmapFromAsset("3.jpg");
			mBitArray[3] = getBitmapFromAsset("4.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}

		gallery.setAdapter(new GalleryAdapter(this, mBitArray));
	}

	public class GalleryAdapter extends BaseAdapter {
		// member variables
		private Context mContext;
		private Bitmap[] mImageArray;

		// constructor
		public GalleryAdapter(Context context, Bitmap[] imgArray) {
			mContext = context;
			mImageArray = imgArray;
		}

		public int getCount() {
			return mImageArray.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// returns the individual images to the widget as it requires them
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imgView = new ImageView(mContext);

			imgView.setImageBitmap(mImageArray[position]);

			// put black borders around the image
			final RelativeLayout borderImg = new RelativeLayout(mContext);
			borderImg.setPadding(20, 20, 20, 20);
			borderImg.setBackgroundColor(0xffffff);// black
			borderImg.addView(imgView);
			return borderImg;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	private Bitmap getBitmapFromAsset(String strName) throws IOException {
		AssetManager assetManager = getAssets();
		InputStream istr = assetManager.open(strName);
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		istr.close();

		return bitmap;
	}
}
