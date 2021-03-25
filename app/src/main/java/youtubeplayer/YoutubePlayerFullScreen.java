package youtubeplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sd.carc.R;
import com.sd.carc.Sipdroid;


/**
 * Created by devaraj on 31/1/17.
 */

public class YoutubePlayerFullScreen extends YouTubeFailureRecoveryActivity
        implements YouTubePlayer.OnFullscreenListener, YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView playerView;
    protected YouTubePlayer player;
    String videoID, backbtnName;
    boolean isFullScreen;
    Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        videoID = getIntent().getExtras().getString("VIDEO_ID");
        isFullScreen = getIntent().getBooleanExtra("isFullScreen", false);
        backbtnName = getIntent().getExtras().getString("SKIPBTN_NAME");

        back = findViewById(R.id.back1);
        try {
            if (!backbtnName.isEmpty()) {
                System.out.println("Back Btn Name :" + backbtnName);
                back.setText(backbtnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerView = findViewById(R.id.player);
        playerView.setKeepScreenOn(true);
        playerView.initialize(getResources()
                .getString(R.string.youtube_api_key), this);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                player.release();
                Intent dataIntent = new Intent(YoutubePlayerFullScreen.this,
                        Sipdroid.class);

                setResult(Activity.RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        final YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        player.setPlayerStyle(PlayerStyle.DEFAULT);
        if (!wasRestored) {
            player.loadVideo(videoID);
            player.setFullscreen(false);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    public void onFullscreen(boolean bool) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }

}
