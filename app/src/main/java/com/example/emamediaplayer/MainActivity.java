package com.example.emamediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    public ImageButton rewindBtn, playBtn, forwardBtn;
    public MediaPlayer mediaPlayer;
    public SeekBar duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource();

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


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                String filePath = (String) extras.get("song");
            }
        }
    }

}
