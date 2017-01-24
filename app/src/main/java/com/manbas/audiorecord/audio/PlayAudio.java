package com.manbas.audiorecord.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 播放AudioRecord录音
 *
 * @author Manbas
 */
public class PlayAudio {
    String path;

    public PlayAudio(String path) {
        this.path = path;
    }

    public void play() {

        //获取目标文件
        File file = new File(path);
        //创建short数组
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];

        try {
            //打开数据流
            InputStream is = new FileInputStream(file);
            BufferedInputStream bs = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bs);

            int i = 0;
            //将数据流中的数据写入music中，将字节流转化为字节数组
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }

            dis.close();
            //实例化AudioTrack
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2,
                    AudioTrack.MODE_STREAM);
            //开启AudioTrack
            audioTrack.play();
            //将music中的数据写入audioTrack中
            audioTrack.write(music, 0, musicLength);

            audioTrack.stop();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
