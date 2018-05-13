package com.example.xavier.broadcastbestpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018\5\11 0011.
 * 作为所有活动的父类
 * 在此处定义了广播接收器，以及接受到消息后的操作逻辑onReceive()函数
 * 这样所有继承BaseActivity的类都有了广播接收器
 */

public class BaseActivity extends AppCompatActivity {
    private ForeceOfflineReceiver offlineReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcastbestpractice.FOECE_OFFLINE");
        offlineReceiver = new ForeceOfflineReceiver();
        registerReceiver(offlineReceiver, intentFilter);//注册广播接收器
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (offlineReceiver != null){
            unregisterReceiver(offlineReceiver);
            offlineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForeceOfflineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//建立一个对话框
            builder.setTitle("warning");//设置标题
            builder.setMessage("You are forced to be offline. Please try to login again.");//输入对话框的信息
            builder.setCancelable(true);//设置为不能取消
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();//销毁所有活动
                    Intent intent = new Intent(context, LoginActivity.class);//返回登录界面
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }
}
