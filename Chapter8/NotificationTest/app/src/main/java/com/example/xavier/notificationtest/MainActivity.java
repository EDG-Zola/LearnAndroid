package com.example.xavier.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotification = (Button) findViewById(R.id.send_notification);
        sendNotification.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_notification:
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                //获得通知服务，返回一个NotificationManager对象
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("疲劳驾驶检测")
                        .setContentText("您已疲劳驾驶")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent)//用PendingIntent对象来设置点击通知后进行跳转
                        .setSound(Uri.fromFile(new File("system/media/audio/ringtones/Luna.ogg")))//设置通知消息来之后播放文件内的内置音频
                        .setVibrate(new long[]{0, 1000, 1000, 1000, 1000})//设置震动，静止0s,震动1s，静止1s，震动1s
                        .setLights(Color.GREEN, 1000, 1000)//设置LED为绿色，亮起的时间为1s，暗区的时间为1s
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.sunset)))//在通知栏中添加大图片
                        .build();
                manager.notify(1,notification);//通知的id为1
                break;
            default:
                break;
        }
    }
}
