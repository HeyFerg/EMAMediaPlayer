package com.example.emamediaplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public ImageButton pauseBtn, playBtn, stopBtn;
    public MediaPlayer mp;
    public SeekBar durationSeek;
    public TextView songDetails;
    public ImageView albumArt;
    public static final int REQUEST_STORAGE_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        stopBtn = (ImageButton) findViewById(R.id.stopBtn);
        requestReadPermissions();
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSong();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSong();
            }
        });
        durationSeek = (SeekBar) findViewById(R.id.songSeekbar);
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

    public void playSong() {
        if (mp.isPlaying()) {
            return;
        }
        try {
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void stopSong() {
        mp.stop();
    }

    public void pauseSong() {
        if(mp.isPlaying())
        mp.pause();
        return;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.songs) {
            Intent intent = new Intent(this, SongsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return true;
    }

    public void rewindSong() {
        mp.seekTo(0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        songDetails = findViewById(R.id.currentArtist);
        albumArt = findViewById(R.id.albumArt);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String filePath = (String) extras.get("path");
                String songName = (String) extras.get("song");
                Integer duration = (Integer) extras.get("duration");
                durationSeek.setMax(duration);
                try {
                    mp.setDataSource(filePath);
                    songDetails.setText(String.valueOf(songName));
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(filePath);
                    byte[] art = mmr.getEmbeddedPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                    albumArt.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void requestReadPermissions(){
        int externalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(externalStorage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_STORAGE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission has been granted.", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, "Permission has been granted", Toast.LENGTH_LONG).show();
        }
    }

    public void onDestroy() {

        super.onDestroy();
        mp.release();
    }
}
