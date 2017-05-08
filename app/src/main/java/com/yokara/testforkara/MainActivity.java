package com.yokara.testforkara;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAgg";
    MediaPlayer mediaplayer = new MediaPlayer();
    Player videoView;
    Button btnMute, btnUnmute;
    MediaController myMediaController;
    private MediaPlayer.OnCompletionListener onComplete;
    private MediaPlayer.OnPreparedListener onPrepared;
    private MediaPlayer.OnErrorListener onVideoErrors;
    private MediaPlayer.OnBufferingUpdateListener onBuffering;
    private Uri uri;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        videoView = (Player) findViewById(R.id.vView);
        btnUnmute = (Button) findViewById(R.id.unMute);
        btnMute = (Button) findViewById(R.id.mute);
        seekBar = (SeekBar) findViewById(R.id.seek);
        loadVieo("http://192.168.1.102/raw/vyvy.mp4");
        mutesetting();
        handler = new Handler();
        setingSeekBar();
        playCycle();
    }

    private void mutesetting() {
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.mute();
            }
        });
        btnUnmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.unmute();
            }
        });

    }

    private void setingSeekBar() {
        if (videoView.isPlaying()) {
            Log.d("playing", "playing");
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("status,", progress + "");
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("status,", "start");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("status,", "stop");

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
//        videoView.start();
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
                        videoView.start();


                    }
                };
        onPrepared = new MediaPlayer.OnPreparedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onPrepared(MediaPlayer player) {
                long timeStart = videoView.getDuration(); //in millisecond
                long timeEnd = videoView.getCurrentPosition();

                ((TextView) findViewById(R.id.tvEnd)).setText(MyUtils.convertDuration(timeStart));
                seekBar.setMax(videoView.getDuration());
                videoView.setMediaPlayer(player);
                videoView.start();
                Log.d(TAG + "DM- duration", String.valueOf(timeStart));
                playCycle();

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


    private void playCycle() {
        if (videoView.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {


                    playCycle();
                    seekBar.setProgress(videoView.getCurrentPosition());

                    ((TextView) findViewById(R.id.tvStart)).setText(MyUtils.convertDuration((long) videoView.getCurrentPosition()));
                    Log.d("playing", "playing " + videoView.getCurrentPosition());

                }
            };
            videoView.start();
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle, resume: ", "resume");
        playCycle();
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle, onPause: ", "onPause1");
        videoView.pause();
        Log.d("lifecycle, onPause: ", "onPause2");

    }

}
