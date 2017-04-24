package com.yokara.testforkara;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAgg";
    MediaPlayer mediaplayer;
    Player videoView;
    Button btnMute, btnUnmute;
    MediaController myMediaController;
    private MediaPlayer.OnCompletionListener onComplete;
    private MediaPlayer.OnPreparedListener onPrepared;
    private MediaPlayer.OnErrorListener onVideoErrors;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (Player) findViewById(R.id.vView);
        btnUnmute = (Button) findViewById(R.id.unMute);
        btnMute = (Button) findViewById(R.id.mute);
        loadVieo("http://html5demos.com/assets/dizzy.mp4");
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.mute();
            }
        });

    }

    private void loadVieo(String url) {
        myMediaController = new MediaController(MainActivity.this);
        uri = Uri.parse(url);

        initListenter();

        videoView.setMediaController(myMediaController);
        myMediaController.setAnchorView(videoView);
        videoView.setVideoURI(uri);
        videoView.resume();
        videoView.start();
        videoView.setClickable(true);
        videoView.setOnCompletionListener(onComplete);
        videoView.setOnPreparedListener(onPrepared);
        videoView.setOnErrorListener(onVideoErrors);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clickwd", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initListenter() {
        onComplete =
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer player) {
                        Log.d(TAG, "End of Video");


                    }
                };
        onPrepared = new MediaPlayer.OnPreparedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onPrepared(MediaPlayer player) {
                long duration = videoView.getDuration(); //in millisecond
                Log.d(TAG + "duration", String.valueOf(duration));
            }
        };
        onVideoErrors = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer player, int what, int extra) {
                String errWhat = "";
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        errWhat = "MEDIA_ERROR_UNKNOWN";
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        errWhat = "MEDIA_ERROR_SERVER_DIED";

                        break;
                    default:
                        errWhat = "unknown what";
                }

                String errExtra = "";
                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        errExtra = "MEDIA_ERROR_IO";
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        errExtra = "MEDIA_ERROR_IO";
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        errExtra = "MEDIA_ERROR_IO";
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        errExtra = "MEDIA_ERROR_IO";
                        break;
                    default:
                        errExtra = "...others";

                }

                Toast.makeText(MainActivity.this,
                        "Error!!!\n" +
                                "what: " + errWhat + "\n" +
                                "extra: " + errExtra,
                        Toast.LENGTH_LONG).show();
                return true;
            }
        };

    }

}
