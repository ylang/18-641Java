package com.ece.smartGallery.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.smartGallery.R;

public class BeamActivity extends Activity implements CreateNdefMessageCallback {
	NfcAdapter mNfcAdapter;
	TextView textView;
	private final String TAG = this.getClass().getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);
		TextView textView = (TextView) findViewById(R.id.beam_text);
		textView.setText("on Create");
		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}
		// Register callback
		mNfcAdapter.setNdefPushMessageCallback(this, this);
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		Log.d(TAG, "ndef message created");
		String text = ("Beam me up, Android!\n\n" + "Beam Time: " + System
				.currentTimeMillis());
		Log.d(TAG, text);
		NdefMessage msg = new NdefMessage(
				new NdefRecord[] {
						NdefRecord
								.createMime(
										"application/vnd.com.ece.smartGallery.activity",
										text.getBytes())
						/**
						 * The Android Application Record (AAR) is commented
						 * out. When a device receives a push with an AAR in it,
						 * the application specified in the AAR is guaranteed to
						 * run. The AAR overrides the tag dispatch system. You
						 * can add it back in to guarantee that this activity
						 * starts when receiving a beamed message. For now, this
						 * code uses the tag dispatch system.
						 */
						,
						NdefRecord
								.createApplicationRecord("com.ece.smartGallery.activity") });
		return msg;
	}

	public NdefRecord createTextRecord(String payload, Locale locale,
			boolean encodeInUtf8) {
		// byte[] langBytes =
		// locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
		// Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") :
		// Charset.forName("UTF-16");
		// byte[] textBytes = payload.getBytes(utfEncoding);
		// int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		// char status = (char) (utfBit + langBytes.length);
		// byte[] data = new byte[1 + langBytes.length + textBytes.length];
		// data[0] = (byte) status;
		// System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		// System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
		// textBytes.length);
		// NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
		// new byte[0], new byte[0], data);
		// return record;
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	void processIntent(Intent intent) {
		Log.d(TAG, "process intent, NFC message received");
		textView = (TextView) findViewById(R.id.beam_text);
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present
		if (msg.getRecords().length > 1) {
			textView.setText(new String(msg.getRecords()[1].getPayload()));
		} else {
			textView.setText(new String(msg.getRecords()[0].getPayload()));
		}
	}
}
