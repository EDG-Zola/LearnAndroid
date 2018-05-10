package com.example.xavier.bradcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private  IntentFilter intentFilterLocal;
    //***********网络状态改变的广播接收器*****//
    private NetworkChangeReceiver networkChangeReceiver;
    //***********本地广播接收器与本地广播管理*****//
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();

        localBroadcastManager =  LocalBroadcastManager.getInstance(this); //获取本地广播管理器的实例
        //******动态注册广播********//
        registerReceiver(networkChangeReceiver, intentFilter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //************点击事件，发送值为com.example.broadcastsettest.MY_BROADCAST的广播**********//
                Intent intent = new Intent("com.example.broadcastsettest.MY_BROADCAST");
                //********发送有序广播，注意还要到AndroidManifest.xml中修改优先级，然后在广播中可以使用abortBroadcast()方法进行阶段***//
                sendOrderedBroadcast(intent, null);
                //定义一个Intent对象，传入值com.example.broadcastset.LOCAL_BROADCAST，并在发送本地广播
                Intent intentLocal = new Intent("com.example.broadcastset.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intentLocal);//发送本地广播
            }
        });
        //******注册本地广播********//
        intentFilterLocal = new IntentFilter();
        intentFilterLocal.addAction("com.example.broadcastset.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilterLocal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //******注销广播*****//
        unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    //****************本地广播的接收器************//
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Received in LocalBroadcastReceiver.", Toast.LENGTH_SHORT).show();
        }
    }

    //****************网络状态广播的接收器************//
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //********获取网络连接服务**********//
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //********获取网络连接状态信息*******//
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //********有可用网络***********//
            if (connectivityManager != null && networkInfo.isAvailable()){
                Toast.makeText(context, "有可用网络", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }
}
