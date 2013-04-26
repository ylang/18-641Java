package com.ece.smartGallery.activity;

import com.ece.smartGallery.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}
	
	public void save(View view){
		Intent intent = new Intent(this,DisplayActivity.class);
		
		// retrieve user input
		String input_text_comment = ((EditText) findViewById(R.id.edit_comment_input)).getText().toString();
		intent.putExtra("text_comment", input_text_comment);
		
		startActivity(intent);
	}

}
