package com.example.xavier.litepaltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.lang.reflect.Constructor;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建数据库
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();//使用LitePal创建了BookStore.db数据库
            }
        });

        //增加数据,
        //注意，这里没有加入id,这是因为DataSupport类里的setid（）是自动增加1的
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unknown");
                book.save();//增加一条数据
            }
        });

        //更新数据
        Button upgradeData = (Button) findViewById(R.id.upgrade_data);
        upgradeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Lost Symbol");
                book.setAuthor("Dan Brown");
                book.setPages(510);
                book.setPrice(19.95);
                book.setPress("Unknown");
                book.save();//增加一条数据
                book.setPrice(10.05);
                book.setPress("Anchor");
                book.updateAll("name = ? and author = ?", "The Lost Symbol", "Dan Brown");//更新数据
            }
        });

        //删除数据,
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Book.class, "price < ?", "12");//删除价格小于12的数据
            }
        });

        //查询数据
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> books = DataSupport.findAll(Book.class);//查询表Book的所有数据
                for (Book book : books){
                    Log.d(TAG, "book name is " + book.getName());
                    Log.d(TAG, "book author is " + book.getAuthor());
                    Log.d(TAG, "book pages is " + book.getPages());
                    Log.d(TAG, "book price is " + book.getPrice());
                    Log.d(TAG, "book press is " + book.getPress());
                }
                //查询第一本书
                Book firstBook = DataSupport.findFirst(Book.class);
                Log.d(TAG, "firstBook name is " + firstBook.getName());
                Log.d(TAG, "firstBook author is " + firstBook.getAuthor());
                Log.d(TAG, "firstBook pages is " + firstBook.getPages());
                Log.d(TAG, "firstBook price is " + firstBook.getPrice());
                Log.d(TAG, "firstBook press is " + firstBook.getPress());
                //查询最后一本书
                Book lastBook = DataSupport.findLast(Book.class);
                Log.d(TAG, "lastBook name is " + lastBook.getName());
                Log.d(TAG, "lastBook author is " + lastBook.getAuthor());
                Log.d(TAG, "lastBook pages is " + lastBook.getPages());
                Log.d(TAG, "lastBook price is " + lastBook.getPrice());
                Log.d(TAG, "lastBook press is " + lastBook.getPress());

                //综合DataSupport的查找方法来进行查找
                List<Book> queryBooks = DataSupport.select("name","author","pages")
                                                    .where("pages > ?", "400")
                                                    .limit(3)
                                                    .find(Book.class);
                for (Book queryBook : queryBooks){
                    Log.d(TAG, "queryBook name is " + queryBook.getName());
                    Log.d(TAG, "queryBook author is " + queryBook.getAuthor());
                    Log.d(TAG, "queryBook pages is " + queryBook.getPages());
                    Log.d(TAG, "queryBook price is " + queryBook.getPrice());
                    Log.d(TAG, "queryBook press is " + queryBook.getPress());
                }
            }

        });
    }
}
