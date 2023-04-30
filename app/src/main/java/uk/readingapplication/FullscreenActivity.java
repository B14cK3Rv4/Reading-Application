package uk.readingapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.Collections;

public class FullscreenActivity extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView  playerView;
    private String url;

    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Fullscreen");

        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        playerView  = findViewById(R.id.exoVideo);

        Intent intent = getIntent();
        url = intent.getExtras().getString("Video1");

    }


    private void initializeExoplayerView(){
        try {
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            Uri videouri = Uri.parse(url);
            MediaItem mediaItem = MediaItem.fromUri(videouri);
            player.addMediaItems(Collections.singletonList(mediaItem));
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();


        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onStart(){
        super.onStart();

        if(Util.SDK_INT >= 26){
            initializeExoplayerView();
        }
    }

    protected void onResume(){
        super.onResume();

        if(Util.SDK_INT >= 26 || player == null){
            //initializeplayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Util.SDK_INT > 26 ){
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(Util.SDK_INT >= 26){
            releasePlayer();
        }
    }

    private void releasePlayer(){
        if(player != null){
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentMediaItemIndex();
            player = null;
        }
    }
}