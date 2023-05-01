package uk.readingapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.LineHeightSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.Collections;

public class FullscreenActivity extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView  playerView;

    boolean fullscreen = false;

    ImageView fullscreenButton;
    private String url;

    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);


        playerView  = findViewById(R.id.exoVideo);

        Intent intent = getIntent();
        url = intent.getExtras().getString("Video1");


        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullscreen){
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenActivity.this, R.drawable.max96white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() !=null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;

                }else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenActivity.this, R.drawable.shrink2white96));
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN|
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() !=null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });

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