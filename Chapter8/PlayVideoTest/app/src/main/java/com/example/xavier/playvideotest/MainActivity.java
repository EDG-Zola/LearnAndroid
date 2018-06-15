package com.example.xavier.playvideotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.video_view);
        Button play_video = (Button) findViewById(R.id.play);
        Button pause_video = (Button) findViewById(R.id.pause);
        Button replay_video = (Button) findViewById(R.id.reply);
        play_video.setOnClickListener(this);
        pause_video.setOnClickListener(this);
        replay_video.setOnClickListener(this);
        //申请访问写外部文件权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else {
            initVideoPath();
        }
    }

    private void initVideoPath() {
        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "music.m4a");//这是在什么路径下？
            File file = new File("/storage/emulated/0/data/Siren.mp4");//这里的路径在手机的内部存储/data下
            if (file.exists()){
                videoView.setVideoPath(file.getPath());//获取指定路径的文件
            }
            else {
                Toast.makeText(MainActivity.this, "没有Siren.mp4文件", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.play:
                if (!videoView.isPlaying()){
                    //这有点问题，如果点击了stop后，在play会无法重新播放
                    videoView.start();//如未开始播放，则开始播发
                    /*
                    在视频播放时，是否可以获取图片的每一帧，然后在做图片处理
                     */
                }
                break;
            case R.id.pause:
                if (videoView.isPlaying()){
                    videoView.pause();//如果已经开始播放，则暂停播放
                }
                break;
            case R.id.reply:
                if (videoView.isPlaying()){
                    videoView.resume();//如果已经开始播放，则重新播放
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }
                else {
                    Toast.makeText(MainActivity.this, "你拒绝了权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    //释放资源
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();

        }

    }
}
