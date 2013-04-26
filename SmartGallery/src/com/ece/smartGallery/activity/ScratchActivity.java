package com.ece.smartGallery.activity;

import com.ece.smartGallery.R;
import com.ece.smartGallery.util.DrawView;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class ScratchActivity extends Activity {

    DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        
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
		drawView.clear();
	}

}
