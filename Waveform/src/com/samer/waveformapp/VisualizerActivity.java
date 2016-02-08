package com.samer.waveformapp;

import java.util.concurrent.TimeUnit;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;


	public class VisualizerActivity extends ActionBarActivity 
	{
		 VisualizerView mVisualizerView;

		 private TextView audioDuration;
		 //private TextView estimatedTime;
		 //private TextView cureentDuration;
		 private Button startPlay;
		 private Button stopPlay;
		 private Visualizer mVisualizer;
		 
		 private MediaPlayer mMediaPlayer;
		 private String path = "";
		 //private boolean ifTimer = false;
		 //private long startTime, millis;
		 //private int seconds, minutes;

		 
		 @Override
		 protected void onCreate(Bundle savedInstanceState) 
		 {
			  super.onCreate(savedInstanceState);
			  setContentView(R.layout.activity_visualizer);
			  
			  audioDuration = (TextView)findViewById(R.id.audioDuration);
			  //estimatedTime = (TextView)findViewById(R.id.estimatedTime);
			  //cureentDuration = (TextView)findViewById(R.id.cureentDuration);
			  mVisualizerView = (VisualizerView) findViewById(R.id.myvisualizerview);
			  startPlay = (Button)findViewById(R.id.startplay);
		      stopPlay = (Button)findViewById(R.id.stopplay);
		  
		      Intent intent = getIntent();
			  path = intent.getStringExtra("path");
			  
			  try
			  {
				  Uri uri = Uri.parse(path);
				  mMediaPlayer = MediaPlayer.create(getBaseContext(), uri);
			  }
			  catch(Exception e){ }
			  audioDuration.setText(TimeUnit.SECONDS.convert(mMediaPlayer.getDuration(), TimeUnit.MILLISECONDS) + "s");
	
			  /*
			  Thread t = new Thread() {
				  @Override
				  public void run() {
				    try {
				      while (!isInterrupted()) {
				        Thread.sleep(1000);
				        runOnUiThread(new Runnable() {
				        	@Override
				          public void run() {
				        	    if(ifTimer) {
				        	    	RunTimer();
				        	    }
				          }
				        });
				      }
				    } catch (InterruptedException e) {
				    }
				  }
				};
				t.start();
				*/
			  
			  
			  startPlay.setOnClickListener(new OnClickListener(){
		        	@Override
		        	public void onClick(View arg0)
		        	{
		        		startPlay.setEnabled(false);
		        		stopPlay.setEnabled(true);
		        		startPlay.setBackgroundColor(Color.BLACK);
		        		stopPlay.setBackgroundColor(Color.WHITE);
	                     
		        		// Timer ..
			        	//startTime = System.currentTimeMillis();
			        	//ifTimer = true;
			        	
		        		initAudio();
		        	}
		        }
		        		);
			  
		      stopPlay.setOnClickListener(new OnClickListener(){
		        	@Override
		        	public void onClick(View arg0)
		        	{
		        		try
		                {
		        			if (mMediaPlayer != null)
		                    {
		        				startPlay.setEnabled(true);
		    	        		stopPlay.setEnabled(false);
		    	        		startPlay.setBackgroundColor(Color.WHITE);
				        		stopPlay.setBackgroundColor(Color.BLACK);
				        		
		        				mMediaPlayer.stop();
		        				mMediaPlayer.release();
		        				//ifTimer = false;
		        				mMediaPlayer = null;
		                    }
		                }
		                catch (Exception ex) {
		                    
		                }
		        	}
		        }
		        		);
		 }

		 @Override
		 protected void onPause() 
		 {
			 super.onPause();
		  
			 if (isFinishing() && mMediaPlayer != null) 
			 {
				 mVisualizer.release();
				 mMediaPlayer.release();
				 startPlay.setEnabled(true);
				 stopPlay.setEnabled(false);
				 //ifTimer = false;
				 mMediaPlayer = null;
			 }
		 }

		 private void initAudio()
		 {
			 setVolumeControlStream(AudioManager.STREAM_MUSIC);

			 // Re-Initially to avoid unfortunately exception ..
			 try
			  {
				  Uri uri = Uri.parse(path);
				  mMediaPlayer = MediaPlayer.create(getBaseContext(), uri);
			  }
			  catch(Exception e){ }
			 
			 setupVisualizerFxAndUI();
			  // Make sure the visualizer is enabled only when you actually want to
			  // receive data, and
			  // when it makes sense to receive data.
			  mVisualizer.setEnabled(true);
			  // When the stream ends, we don't need to collect any more data. We
			  // don't do this in
			  // setupVisualizerFxAndUI because we likely want to have more,
			  // non-Visualizer related code
			  // in this callback.
			  mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
			  {
			     public void onCompletion(MediaPlayer mediaPlayer) 
			     {
			    	 mVisualizer.setEnabled(false);
			     }
			  }
					  );
			  mMediaPlayer.start();
		 }

		 /*
		 private void RunTimer()
		 {
			 millis 	= System.currentTimeMillis() - startTime;
		     seconds 	= (int) (millis / 1000);
		     minutes 	= seconds / 60;
		     seconds    = seconds % 60;
		     
		     estimatedTime.setText(String.format("%d:%02d", minutes, seconds));
		     cureentDuration.setText(mMediaPlayer.getCurrentPosition());
		 }
		 */
		 
		 private void setupVisualizerFxAndUI() 
		 {
			  // Create the Visualizer object and attach it to our media player.
			  mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
			  mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
			  mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() 
			  {
			     public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) 
			     {
			    	 mVisualizerView.updateVisualizer(bytes);
			     }
			     public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) { }
			  }, Visualizer.getMaxCaptureRate() / 2, true, false);
		 }
	}
