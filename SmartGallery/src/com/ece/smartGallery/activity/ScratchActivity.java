package com.ece.smartGallery.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import com.ece.smartGallery.R;
import com.ece.smartGallery.entity.Photo;
import com.ece.smartGallery.util.DrawView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class ScratchActivity extends Activity {

    DrawView drawView;
    private Photo photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        
		Intent intent = getIntent();
		photo = (Photo) intent.getSerializableExtra(Photo.PHOTO);
		
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.scratch_area);

        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        parent.addView(drawView);
        drawView.requestFocus();

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scratch, menu);
		return true;
	}
	
	public void clear_scratch(View view){
		drawView.clear();
	}
	
	public void save_scratch(View view){
		RelativeLayout parent = (RelativeLayout) findViewById(R.id.scratch_area);
		parent.setDrawingCacheEnabled(true);
		Bitmap b = parent.getDrawingCache();

		FileOutputStream fos = null;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String name = UUID.randomUUID().toString();
        String path2 = path + "/"+name+ ".png";
        
		try {

		fos = new FileOutputStream(path2);
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		}

		b.compress(CompressFormat.PNG, 95, fos);
		
		Intent intent = new Intent(this, EditActivity.class);
		File f = new File(path, name+".png");
		
		photo.setScratchURI(Uri.fromFile(f));
		intent.putExtra(Photo.PHOTO,photo);
		intent.setAction(Intent.ACTION_EDIT);
		
		startActivity(intent);
		finish();
	}
	

}
