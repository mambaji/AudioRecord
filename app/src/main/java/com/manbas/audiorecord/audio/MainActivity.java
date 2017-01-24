
package com.manbas.audiorecord.audio;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.manbas.audiorecord.R;

import java.io.IOException;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener {
    public static boolean isMediaRecording, isAudioRecording;

    private Button Media_btn, Audio_btn, PlayAudio_btn;
    private MakeAudioByAUDIO makeAudio;
    private MakeAudioByMEDIA makeMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Media_btn = (Button) findViewById(R.id.MediaR_btn);
        Audio_btn = (Button) findViewById(R.id.AudioR_btn);
        Media_btn.setOnTouchListener(this);
        Audio_btn.setOnTouchListener(this);
        PlayAudio_btn = (Button) findViewById(R.id.PlayAudio_btn);
        PlayAudio_btn.setEnabled(true);
        PlayAudio_btn.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            //MeidaRecorder录音
            case R.id.MediaR_btn:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        makeMedia = new MakeAudioByMEDIA("meida");
                        isMediaRecording = true;
                        if (isMediaRecording) {
                            try {
                                makeMedia.startRecording();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isMediaRecording) {
                            isMediaRecording = false;
                            makeMedia.stop();
                            PlayAudio_btn.setEnabled(true);
                        }
                        break;
                }

                break;
            //AudioRecord录音
            case R.id.AudioR_btn:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        makeAudio = new MakeAudioByAUDIO("audio");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    makeAudio.startRecording();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        break;
                    case MotionEvent.ACTION_UP:
                        MakeAudioByAUDIO.isRecording = false;
                        break;
                }

        }
        return false;
    }

    //播放录音
    @Override
    public void onClick(View v) {
        if (v == PlayAudio_btn) {
            PlayAudio play = new PlayAudio(makeAudio.path);
            play.play();


        }
    }


}
