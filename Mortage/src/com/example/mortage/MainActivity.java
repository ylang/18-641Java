package com.example.mortage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button submit = (Button) findViewById(R.id.submit);

		Spinner monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
		ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter
				.createFromResource(this, R.array.month_array,
						android.R.layout.simple_spinner_item);
		monthAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinner.setAdapter(monthAdapter);

		Spinner yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
		ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter
				.createFromResource(this, R.array.year_array,
						android.R.layout.simple_spinner_item);
		yearAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinner.setAdapter(yearAdapter);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String priceRaw = ((EditText) findViewById(R.id.purchasePriceInput))
						.getText().toString();
				String downPaymentRaw = ((EditText) findViewById(R.id.downPaymentInput))
						.getText().toString();
				String termRaw = ((EditText) findViewById(R.id.mortageTermInput))
						.getText().toString();
				String rateRaw = ((EditText) findViewById(R.id.interestRateInput))
						.getText().toString();
				String taxRaw = ((EditText) findViewById(R.id.propertyTaxInput))
						.getText().toString();
				String insuranceRaw = ((EditText) findViewById(R.id.propertyInsuranceInput))
						.getText().toString();

				try {
					double price = Double.parseDouble(priceRaw);
					double downPayment = Double.parseDouble(downPaymentRaw);
					int term = Integer.parseInt(termRaw);
					double rate = Double.parseDouble(rateRaw) / 100;
					double tax = Double.parseDouble(taxRaw);
					double insurance = Double.parseDouble(insuranceRaw);

					boolean flag = (price > 0)
							&& (downPayment > 0 && downPayment < 100)
							&& (term > 0) && (rate >= 0 && rate < 1)
							&& (tax >= 0) && (insurance >= 0);
					if (flag == false) {
						throw new Exception("format error");
					} else {
						double loan = price * (1 - downPayment / 100);
						
						double monthly = (rate / 12 * loan)
								/ (1 - Math.pow(1 + rate / 12, 0 - term * 12));
						monthly += tax / 12;
						monthly += insurance / 12;
						double total = monthly * term * 12;
						
						String month = "Jan";
						String year = "2013";
						month = ((Spinner) findViewById(R.id.monthSpinner)).getSelectedItem().toString();
						year = ((Spinner) findViewById(R.id.yearSpinner)).getSelectedItem().toString();
						int y = Integer.parseInt(year);
						y += term;
						Intent intent = new Intent(getApplicationContext(),
								ResultActivity.class);
						intent.putExtra("monthly", monthly);
						intent.putExtra("total", total);
						intent.putExtra("payCount", term * 12);
						intent.putExtra("payoffMonth", month);
						intent.putExtra("payoffYear", y);
						startActivity(intent);
					}

				} catch (Exception e) {
					e.printStackTrace();
					Log.v("MORTGAGE", e.getLocalizedMessage());
					Context context = getApplicationContext();
					CharSequence text = "Format Error";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}
		});
	}
}
