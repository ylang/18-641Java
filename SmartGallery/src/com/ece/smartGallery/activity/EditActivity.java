package com.ece.smartGallery.activity;

import com.ece.smartGallery.R;
import com.ece.smartGallery.util.DrawView;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import android.util.Log;

public class EditActivity extends Activity {
	
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private Button mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private boolean mStartRecording = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		AudioRecordTest();
		
		mRecordButton = (Button) findViewById(R.id.add_voice_button);
		mRecordButton.setText("Start recording");
		mRecordButton.setOnClickListener(new View.OnClickListener() {
			
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    mRecordButton.setText("Stop recording");
                } else {
                    mRecordButton.setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
		});
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
	
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }
	
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }
    
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    
    public void AudioRecordTest() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }
    
	public void scratch(View view){
		Intent intent = new Intent(this,ScratchActivity.class);
		startActivity(intent);
	}


}
