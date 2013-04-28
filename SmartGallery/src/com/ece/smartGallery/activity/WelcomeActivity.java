package com.ece.smartGallery.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.smartGallery.R;
import com.ece.smartGallery.DBLayout.Album;
import com.ece.smartGallery.activity.bluetooth.BluetoothChat;
import com.ece.smartGallery.adapter.WelcomeListAdapter;
import com.ece.smartGallery.entities.DatabaseHandler;

public class WelcomeActivity extends Activity {
	private final String TAG = this.getClass().getName();
	LinearLayout addAlbum;
	ListView list;
	List<Album> albums;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		db = new DatabaseHandler(this);
		addAlbum = (LinearLayout) findViewById(R.id.add_new_album);
		addAlbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createNewAlbum();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_bluetooth:
			intent = new Intent(this, BluetoothChat.class);
			intent.setAction(Intent.ACTION_ANSWER);
			startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "action = " + getIntent().getAction());
		Toast.makeText(this, getIntent().getAction(), Toast.LENGTH_LONG).show();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Log.d(TAG, "recieved NFC push");
			processIntent(getIntent());
		} 
		loadAlbums();
	}

	public void pushToHomeActivity(int albumId) {
		Intent intent = new Intent(this,
				com.ece.smartGallery.activity.HomeActivity.class);
		intent.putExtra(Album.ALBUM, albumId);
		Log.d(TAG, "album " + albumId + " is selected");
		db.close();
		startActivity(intent);
	}

	public void createNewAlbum() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Album");
		alert.setMessage("Please enter the new album name");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if (value != null && value.length() != 0) {
					db.addAlbum(value);
					Log.d(TAG, "new album created: " + value);
					loadAlbums();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

	public void loadAlbums() {
		this.albums = db.getAllAlbums();
		if (this.albums.size() == 0) {
			db.addAlbum("default");
			this.albums = db.getAllAlbums();
		}
		WelcomeListAdapter adapter = new WelcomeListAdapter(this, albums);
		list = (ListView) findViewById(R.id.album_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				pushToHomeActivity(albums.get(pos).getId());
			}

		});
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		Log.d(TAG, "in new intent");
		setIntent(intent);
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Log.d(TAG, "recieved NFC push");
			processIntent(getIntent());
		} 
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	void processIntent(Intent intent) {
		Log.d(TAG, "process intent, NFC message received");
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present

		Log.d(TAG, "recieved NFC: has " + msg.getRecords().length + " records");
		int cnt = 0;
		for (NdefRecord record : msg.getRecords()) {
			Log.d(TAG, "record " + cnt + " has length: "
					+ record.getPayload().length);
			Log.d(TAG,
					"record " + cnt + "is a type of "
							+ new String(record.getType()));
			cnt++;
		}
	}

}
