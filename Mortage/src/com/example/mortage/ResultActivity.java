package com.example.mortage;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Intent intent = getIntent();
		double monthly = intent.getDoubleExtra("monthly", 0);
		double total = intent.getDoubleExtra("total", 0);
		int payCount = intent.getIntExtra("payCount", 360);
		String month = intent.getStringExtra("payoffMonth");
		int year = intent.getIntExtra("payoffYear", 2030);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		((TextView) findViewById(R.id.monthlyPayment)).setText(format.format(monthly));
		((TextView) findViewById(R.id.totalPay)).setText(format.format(total));
		((TextView) findViewById(R.id.payCount)).setText("Total of " + payCount + " payments");
		((TextView) findViewById(R.id.payOffDate)).setText(month + " " + year);
		Button back = (Button) findViewById(R.id.backButton);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
