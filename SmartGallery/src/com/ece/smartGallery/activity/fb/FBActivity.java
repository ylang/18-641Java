package com.ece.smartGallery.activity.fb;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.ece.smartGallery.R;

public class FBActivity extends FragmentActivity {

	private FBFragment loginFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			loginFragment = new FBFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, loginFragment).commit();
		} else {
			// Or set the fragment from restored state info
			loginFragment = (FBFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}
}