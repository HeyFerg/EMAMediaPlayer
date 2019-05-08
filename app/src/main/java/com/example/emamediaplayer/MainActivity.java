package com.example.emamediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public ImageButton rewindBtn, playBtn, forwardBtn;
    public MediaPlayer mp;
    public SeekBar durationSeek;
    public TextView songDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("My error", "Break me");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        rewindBtn = (ImageButton) findViewById(R.id.rewindBtn);
        forwardBtn = (ImageButton) findViewById(R.id.forwardBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        rewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewindSong();
            }
        });
        durationSeek = (SeekBar)findViewById(R.id.songSeekbar);
        durationSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean user) {
                if (!mp.isPlaying()) {
                    return;
                }
                mp.seekTo(progress);

            }
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





    }

    public void playSong(){
        if (mp.isPlaying()) {
            return;
        }
        try{
            mp.prepare();
            mp.start();
        }catch (IOException e){
            e.printStackTrace();
            Log.d("My error", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.songs){
            Intent intent = new Intent(this, SongsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return true;
    }

    public void rewindSong(){
        mp.seekTo(0);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        songDetails = findViewById(R.id.currentArtist);
        Log.d("My error", "I am here");
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                String filePath = (String) extras.get("path");
                String songName = (String) extras.get("song");
                Integer duration = (Integer) extras.get("duration");
                durationSeek.setMax(duration);
                Log.d("Duration error fam", String.valueOf(duration));
                Log.d("My error", songName);
                try{
                    Log.d("My error", filePath);
                    mp.setDataSource(filePath);
                    songDetails.setText(String.valueOf(songName));

                }catch (IOException e){
                    e.printStackTrace();
                    Log.d("My error", e.getMessage());
                }

            }
        }
    }

}
