package com.example.xavier.sharedpreferencetest;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveData = (Button) findViewById(R.id.saveData);
        Button restoreData = (Button) findViewById(R.id.restoreData);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();//以覆盖模式存储数据到名为data的文件夹
                editor.putString("name","Xavier");
                editor.putInt("age", 24);
                editor.putString("school","重庆大学");
                editor.putBoolean("master", true);
                editor.apply();
            }
        });

        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String name = pref.getString("name","");
                int age = pref.getInt("age",0);
                String school = pref.getString("school","");
                Boolean master = pref.getBoolean("master",false);
                Log.d(TAG, "name is " + name);
                Log.d(TAG, "age is " + age);
                Log.d(TAG, "school is " + school);
                Log.d(TAG, "master?" + master);
            }
        });
    }
}
