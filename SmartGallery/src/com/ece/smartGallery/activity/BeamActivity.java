package com.ece.smartGallery.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Photo;
import com.ece.smartGallery.util.Utility;

public class BeamActivity extends Activity implements
		CreateNdefMessageCallback, OnNdefPushCompleteCallback {
	private static final int MESSAGE_SENT = 1;
	NfcAdapter mNfcAdapter;
	TextView textView;
	private final String TAG = this.getClass().getName();
	private Photo photo;
	private byte[] imageBytes;
	private byte[] payload = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);
		Log.d(TAG, "onCreate.");
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

		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		Log.d(TAG, "ndef message creating");
		NdefRecord photoRecord = createPhotoRecord();
		// NdefRecord imageRecord = createImageRecord();
		NdefMessage msg = new NdefMessage(new NdefRecord[] { photoRecord });
		return msg;
	}

	public NdefRecord createPhotoRecord() {
		if (payload != null) {
			byte[] mimeBytes = "application/com.ece.smartgallery"
					.getBytes(Charset.forName("US-ASCII"));
			NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
					mimeBytes, new byte[0], payload);
			Log.d(TAG, "photo record created, length = " + payload.length);
			return mimeRecord;
		} else {
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Log.d(TAG, "recieved NFC push");
			processIntent(getIntent());
		} else {
			photo = (Photo) getIntent().getSerializableExtra(Photo.PHOTO);
			try {
				//payload = IO.getBase64ByteArray(new TransforablePhoto(photo));
				Bitmap b = Utility.scaleImage(this, photo.getImage());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				b.compress(Bitmap.CompressFormat.JPEG, 10, stream);
				payload = stream.toByteArray();
				Log.d(TAG, "payload created, length = " + payload.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		Log.d(TAG, "in new intent");
		setIntent(intent);
		// if
		// (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
		// Log.d(TAG, "recieved NFC push");
		// processIntent(getIntent());
		// }
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

		NdefRecord record = msg.getRecords()[0];
		byte[] img = record.getPayload();
		Log.d(TAG, "recieved NFC: has " + msg.getRecords().length + " record");
		Log.d(TAG, "record has length: " + record.getPayload().length);
		Log.d(TAG, "record is a type of " + new String(record.getType()));
		ImageView image = (ImageView) findViewById(R.id.beam_image);           
		Bitmap bMap = BitmapFactory.decodeByteArray(img, 0, img.length);
		image.setImageBitmap(bMap);
	}

	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		// A handler is needed to send messages to the activity when this
		// callback occurs, because it happens from a binder thread
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}

	/** This handler receives a message from onNdefPushComplete */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "Message sent!",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
}
