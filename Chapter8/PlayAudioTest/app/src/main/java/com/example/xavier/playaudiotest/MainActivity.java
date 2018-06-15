package com.example.xavier.playaudiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play_audio = (Button) findViewById(R.id.play_audio);
        Button pause_audio = (Button) findViewById(R.id.pause_audio);
        Button stop_audio = (Button) findViewById(R.id.stop_audio);
        play_audio.setOnClickListener(this);
        pause_audio.setOnClickListener(this);
        stop_audio.setOnClickListener(this);
        //申请访问写外部文件权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else {
            initMediaPlayer();
        }
    }

    private void initMediaPlayer(){
        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "music.m4a");//这是在什么路径下？
            File file = new File("/storage/emulated/0/data/music.m4a");//这里的路径在手机的内部存储/data下
            if (file.exists()){
                mediaPlayer.setDataSource(file.getPath());//获取指定路径的文件
                mediaPlayer.prepare();//启动准备前的工作
            }
           else {
                Toast.makeText(MainActivity.this, "没有music.m4a文件", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
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
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.play_audio:
                if (!mediaPlayer.isPlaying()){
                    //这有点问题，如果点击了stop后，在play会无法重新播放
                    mediaPlayer.start();//如未开始播放，则开始播发
                }
                break;
            case R.id.pause_audio:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//如果已经开始播放，则暂停播放
                }
                break;
            case R.id.stop_audio:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();//如果已经开始播放，则停止播放
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
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        
    }
}
