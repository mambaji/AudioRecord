package com.manbas.audiorecord.audio;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * MeidaRecorder 制作录音
 *
 * @author Manbas
 */
public class MakeAudioByMEDIA {


    String path;
    //获取随机的uuid
    String randUUID = UUID.randomUUID().toString();
    private MediaRecorder mRecorder;

    public MakeAudioByMEDIA(String path) {
        this.path = sanitizePath(path);
    }

    public String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path + randUUID;
        }
        if (!path.contains(".")) {
            path = path + ".amr";
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/media");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + path;
    }

    public void startRecording() throws IOException {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD card is not mounted,it is" + state + ".");
        }
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("ParentFile is not create.");
        }
        //实例化
        mRecorder = new MediaRecorder();
        //设置音频源
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置输出格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //设置输出文件
        mRecorder.setOutputFile(path);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            //准备
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() is faild");
        }
        //开启
        mRecorder.start();
    }

    //停止录音
    public void stop() {
        mRecorder.stop();
        mRecorder.release();
    }

}
