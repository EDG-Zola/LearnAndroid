package com.example.xavier.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "Bookstore.db", null, 1);//创建一个数据库Bookstore.db，版本为1
        dbHelper = new MyDatabaseHelper(this, "Bookstore.db", null, 2);//创建一个数据库Bookstore.db，版本为2
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        //增加数据
        Button addData = (Button)findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();//用一个ContentValues类来添加数据库的数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null,values);//插入第一条数据Book
                Toast.makeText(MainActivity.this, "Add data succeeded", Toast.LENGTH_SHORT).show();
                values.clear();

                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null,values);//插入第二条数据Book
                Toast.makeText(MainActivity.this, "Add data succeeded", Toast.LENGTH_SHORT).show();
                values.clear();
            }
        });
        //更新数据
        Button upgradeData = (Button) findViewById(R.id.upgrade_data);
        upgradeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();//用一个ContentValues类来添加数据库的数据
                values.put("price", 10.09);
                db.update("Book", values, "name=?", new String[] {"The Da Vinci Code"});
            }
        });
        //删除数据
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] {"500"});
            }
        });
        //查询数据
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //*****这里只是简单的查询Book表中的所有数据,会返回一个Cursor对象，所有数据存在Cursor对象中*****//
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    //遍历cursor中的所有对象
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int page = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        //利用log.d()在Logcat窗口中查看数据
                        Log.d(TAG, "book name is " + name);
                        Log.d(TAG, "book author is " + author);
                        Log.d(TAG, "book page is " + page);
                        Log.d(TAG, "book price is " + price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
