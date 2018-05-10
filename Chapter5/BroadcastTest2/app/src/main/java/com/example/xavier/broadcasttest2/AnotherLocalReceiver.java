package com.example.xavier.broadcasttest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AnotherLocalReceiver extends BroadcastReceiver {
    public AnotherLocalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "try to receive BroadcastTest1's LocalBroadcast.", Toast.LENGTH_SHORT).show();
    }
}
