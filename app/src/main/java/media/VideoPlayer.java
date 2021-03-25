package media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sd.carc.R;
import com.sd.carc.Sipdroid;

import constants.AppConstants;

public class VideoPlayer extends Activity implements AppConstants {

    String videoName;
    Button back;
    String playFrom;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        back = findViewById(R.id.back1);
        videoName = getIntent().getExtras().getString("Video_Name");
        playFrom = getIntent().getExtras().getString("playFrom");

        final VideoView videoView = findViewById(R.id.videoView1);
        /*if(videoName.endsWith(".mp4")){
            videoName = videoName.substring(0, videoName.indexOf('.'));
        }*/
        //Creating MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        //String path = "android.resource://"+getPackageName()+"/raw/"+ videoName;
        if (playFrom.equals("local")) {
            path = PATH_FOR_VIDEO + videoName;
        } else if (playFrom.equals("remote")) {
            if ((videoName.startsWith("http://")) || (videoName.startsWith("https://")) ) {
                path = videoName;
            } else {
                path = SERVER_PATH + "images/" + videoName;
            }
        } else if(playFrom.equals("external")) {
            path = videoName;
        }
        Uri uri = Uri.parse(path);

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setZOrderOnTop(true);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                videoView.stopPlayback();
                Intent dataIntent = new Intent(VideoPlayer.this,
                        Sipdroid.class);

                setResult(Activity.RESULT_OK, dataIntent);
                finish();
            }
        });
    }
}
