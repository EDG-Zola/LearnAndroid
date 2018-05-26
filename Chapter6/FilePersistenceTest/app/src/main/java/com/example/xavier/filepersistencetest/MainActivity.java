package com.example.xavier.filepersistencetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit);
        String inputText = load();//载入文件名为data中的数据
        if(!TextUtils.isEmpty(inputText)){
            editText.setText(inputText);
            editText.setSelection(inputText.length());
            Toast.makeText(this, "Restore successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = editText.getText().toString();
        try {
            save(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String inputText) throws IOException {
        FileOutputStream out = null;
        BufferedWriter writer = null;
            try {
                out = openFileOutput("data", Context.MODE_APPEND);//打开名为data的文件，将返回的值赋给FileOutputStream类对象out
                writer = new BufferedWriter(new OutputStreamWriter(out));//将BufferedWriter
                writer.write(inputText);//向缓存输出流中写数据
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(writer != null)
                        writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
    public String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");//打开名为data的文件，赋值给文件输入流in
            reader = new BufferedReader(new InputStreamReader(in)); //将文件输入流in转为BufferReader
            String line = "";
            while((line = reader.readLine()) != null)//未达到文件末尾，将line继续追加到content中
                content.append(line);
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
