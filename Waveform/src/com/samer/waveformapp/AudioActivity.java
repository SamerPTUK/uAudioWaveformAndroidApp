package com.samer.waveformapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;


public class AudioActivity extends Activity
{
    private TextView audioPath;
    private Button startRecord;
    private Button stopRecord;
    private Button browseAudio;
    private Button tovisualizeractivity;
    

    private MediaRecorder _recorder;
    private String toPath = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		
		audioPath = (TextView)findViewById(R.id.browseName);
		startRecord = (Button)findViewById(R.id.start);
        stopRecord = (Button)findViewById(R.id.stop);
        browseAudio = (Button)findViewById(R.id.browseaudio);
        tovisualizeractivity = (Button)findViewById(R.id.toVisualizerActivity);
        
        final String path = Environment.getExternalStorageDirectory() + "/test.mp3";
        
        startRecord.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0)
        	{
        		 try
                 {
                     startRecord.setEnabled(false);
                     browseAudio.setEnabled(false);
                     tovisualizeractivity.setEnabled(false);
                     stopRecord.setEnabled(true);
                     
                     startRecord.setBackgroundColor(Color.BLACK);
                     browseAudio.setBackgroundColor(Color.BLACK);
                     tovisualizeractivity.setBackgroundColor(Color.BLACK);
                     stopRecord.setBackgroundColor(Color.WHITE);
                     toPath = Environment.getExternalStorageDirectory() + "/test.mp3";
                     
                     _recorder = new MediaRecorder();
                     _recorder.setAudioSource(AudioSource.MIC);
                     _recorder.setOutputFormat(OutputFormat.DEFAULT);
                     _recorder.setAudioEncoder(AudioEncoder.DEFAULT);
                     _recorder.setOutputFile(path);
                     _recorder.prepare();
                     _recorder.start();
                 }
                 catch (Exception ex) {
                     
                 }
        	}
        }
        		);
        
        stopRecord.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0)
        	{
        		try
                {
                    startRecord.setEnabled(true);
                    browseAudio.setEnabled(true);
                    tovisualizeractivity.setEnabled(true);
                    stopRecord.setEnabled(false);
                    
                    startRecord.setBackgroundColor(Color.WHITE);
                    browseAudio.setBackgroundColor(Color.WHITE);
                    tovisualizeractivity.setBackgroundColor(Color.WHITE);
                    stopRecord.setBackgroundColor(Color.BLACK);
                    audioPath.setText("Action: Record Audio");

                    if (_recorder != null)
                    {
                        _recorder.stop();
                        _recorder.release();
                        _recorder = null;
                    }
                }
                catch (Exception ex) {
                    
                }
        	}
        }
        		);
        
        browseAudio.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0)
        	{
        		Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecte a Audio"), 1);
        	}
        }
        		);
        
        tovisualizeractivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getBaseContext(), VisualizerActivity.class);
				intent.putExtra("path", toPath);
                startActivity(intent);
			}
        }
	        );
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
	    if (requestCode == 1 && resultCode== Activity.RESULT_OK) 
	    {
	    	Uri contactUri = data.getData();
	    	  
	    	  Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
	    	  cursor.moveToFirst();
	    	  String document_id = cursor.getString(0);
	    	  document_id = document_id.substring(document_id.lastIndexOf(":")+1);
	    	  cursor.close();

	    	  cursor = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
	    			  null, MediaStore.Audio.Media._ID + " = ? ", new String[]{document_id}, null);
	    	  cursor.moveToFirst();
	    	  String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
	    	  cursor.close();
	    	  toPath = path;

	    	  tovisualizeractivity.setBackgroundColor(Color.WHITE);
	    	  audioPath.setText("Action: browse:" + toPath);  
	    	  tovisualizeractivity.setEnabled(true);
	    }
	}
}

        