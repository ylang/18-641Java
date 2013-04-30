package com.ece.smartGallery.activity.bluetooth;

import android.view.View;
import android.view.View.OnClickListener;

import com.ece.smartGallery.util.TransferablePhoto;

public class SendListener implements OnClickListener {

	private BluetoothChat bc;
	private TransferablePhoto tPhoto;

	public SendListener(BluetoothChat bc, TransferablePhoto tPhoto) {
		this.bc = bc;
		this.tPhoto = tPhoto;
	}

	public void onClick(View v) {
		// Send a message using content of the edit text widget
		this.bc.sendMessage(this.tPhoto);
	}
}