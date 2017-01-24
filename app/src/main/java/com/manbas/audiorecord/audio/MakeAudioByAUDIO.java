package com.manbas.audiorecord.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * AudioRecord 制作录音
 *
 * @author Manbas
 */
public class MakeAudioByAUDIO {
    public String path;
    public static boolean isRecording;
    String randUUID = UUID.randomUUID().toString();
    private AudioRecord mRecord;
    private AudioTrack mTrack;
    private File file;

    int frequency = 11025;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    public MakeAudioByAUDIO(String path) {
        this.path = sanitizaPath(path);
    }

    private String sanitizaPath(String path) {

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path = path + randUUID + ".pcm";
        }
        file = new File(Environment.getExternalStorageDirectory()
                .getAbsoluteFile() + "/audioRecord");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + path;
    }

    public void startRecording() throws IOException {
        String state = Environment.getExternalStorageState();
        //检查sd卡是否可读可写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD card is not mounted,it is" + state + ".");
        }
        //检查上级目录是否创建
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("ParentFile is not create.");

        }
        //创建录音文件
        file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            // 创建数据流
            OutputStream os = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            // 创建bufferSize的大小，必须大于等于AudioRecord缓存空间大小的最小值
            int bufferSize = AudioRecord.getMinBufferSize(frequency,
                    channelConfiguration, audioEncoding);
            // 实例化AudioRecord
            mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                    channelConfiguration, audioEncoding, bufferSize);
            // 创建buffer
            short[] buffer = new short[bufferSize];
            // 开启录音
            mRecord.startRecording();

            isRecording = true;

            while (isRecording) {
                // 将AudioRecord中的录音存入buffer中
                int bufferReadResult = mRecord.read(buffer, 0, bufferSize);
                // 将buffer中的缓存语音写入数据流中
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }

            }
            //停止录音
            mRecord.stop();
            //关闭刘
            dos.close();

        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
    }
}
