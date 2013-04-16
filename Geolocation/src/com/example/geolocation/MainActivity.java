package com.example.geolocation;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button button;
	private LocationService service;
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		service = new LocationService(MainActivity.this);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (service.canGetLocation()) {

					double latitude = service.getLatitude();
					double longitude = service.getLongitude();
					
					text = "Your geolocation is lat: " + latitude + " long: " + longitude; 
					String num = "+1-412-999-8639";
					
					TextView tv = (TextView) findViewById(R.id.textView2);
					tv.setText(text);
					try {
						SmsManager smsManager = SmsManager.getDefault();
						PendingIntent sentPI;
						String SENT = "SMS_SENT";
						sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,new Intent(SENT), 0);
						smsManager.sendTextMessage(num, null, text, sentPI, null);
						Toast.makeText(getApplicationContext(), "SMS Sent!",
									Toast.LENGTH_LONG).show();
					  } catch (Exception e) {
						Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later!",
							Toast.LENGTH_LONG).show();
						e.printStackTrace();
					  }
				} else {
					service.showSettingsAlert();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void update(double lat, double lng) {
		
		text = "Your geolocation is lat: " + lat + " long: " + lng; 
		
		TextView tv = (TextView) findViewById(R.id.textView2);
		tv.setText(text);
	}
}
