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
    private SharedPreferences pref;//���ڻ�ȡ�ļ��ڵ�����
    private SharedPreferences.Editor editor;//�������ļ��洢����
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//��ȡһ��Ĭ�ϵ�SharedPreference
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remenber_pass);//��CheckBox
        login = (Button) findViewById(R.id.login);
        boolean isremmber = pref.getBoolean("remeber password", false);//��ȡ�ļ���remeber password��ֵ����ʱĬ��Ϊfalse
        if (isremmber){
            //���˺ź����루��editor�л�ȡ�����뵽�ı����У�
            String account = pref.getString("account", "");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);//��ס������Ϲ�
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (account.equals("admin") && password.equals("123456")){
                    editor = pref.edit();
                    if(rememberPass.isChecked()){//�����ѡ��ѡ�У���account��password��ֵ����editor
                        editor.putBoolean("remeber password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }else {
                        editor.clear();//���û�а��¼�ס���룬�����editor������
                    }
                    editor.apply();//Ӧ��editor,�������ݴ洢
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
