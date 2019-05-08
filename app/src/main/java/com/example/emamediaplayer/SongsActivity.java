package com.example.emamediaplayer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class SongsActivity extends ListActivity {

    static List<String> filePaths = new ArrayList<>();
    static List<Integer> songDurations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        File musicDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tracks");
        File[] allSongs = musicDirectory.listFiles();
        String[] songArray;
        songArray = new String[allSongs.length];

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();


        for (int i = 0; i < allSongs.length; i++) {
            String trackFilename = musicDirectory + "/" + allSongs[i].getName();
            filePaths.add(trackFilename);

            mmr.setDataSource(trackFilename);

            String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String songArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            songDurations.add(Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

            songArray[i] = songName + " by " + songArtist;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songArray);
        setListAdapter(adapter);

    }

    public void onListItemClick(ListView listView, View view, int index, long id) {

        Log.d("My error", "I am here");
        String selectedSong = (String) getListAdapter().getItem(index);
        String filePath = filePaths.get(index);
        Integer duration = songDurations.get(index);
        Intent intent = new Intent();
        Bundle bundle  = new Bundle();
        bundle.putString("song", selectedSong);
        bundle.putString("path", filePath);
        bundle.putInt("duration", duration);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}