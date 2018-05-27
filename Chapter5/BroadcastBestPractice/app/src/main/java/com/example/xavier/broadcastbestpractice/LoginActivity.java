package com.example.xavier.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private SharedPreferences pref;//用于获取文件内的数据
    private SharedPreferences.Editor editor;//用于向文件存储数据
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//获取一个默认的SharedPreference
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remenber_pass);//绑定CheckBox
        login = (Button) findViewById(R.id.login);
        boolean isremmber = pref.getBoolean("remeber password", false);//获取文件内remeber password的值，此时默认为false
        if (isremmber){
            //将账号和密码（从editor中获取）加入到文本框中，
            String account = pref.getString("account", "");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);//记住密码打上勾
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (account.equals("admin") && password.equals("123456")){
                    editor = pref.edit();
                    if(rememberPass.isChecked()){//如果复选框被选中，则将account和password的值传入editor
                        editor.putBoolean("remeber password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }else {
                        editor.clear();//如果没有按下记住密码，则清除editor的内容
                    }
                    editor.apply();//应用editor,进行数据存储
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
