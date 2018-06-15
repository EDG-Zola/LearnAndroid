package com.example.xavier.cameraalbumtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int TAKE_PHOTO = 1;//用于拍照的intent
    private static final int CHOOSE_PHOTO = 2;//用于从相册里选取照片的intent

    private ImageView picture;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takePhoto = (Button) findViewById(R.id.take_photo);//拍照的按键
        Button choosePhoto = (Button) findViewById(R.id.choose_photo);//打开相册的按键
        picture = (ImageView) findViewById(R.id.picture);
        //拍照的按键监听器
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.首先创建一个文件流，用于存储图片,这里放在外部应用关联缓存路径cache中
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    //如果图片已经存在，则删除原来的图片
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    //图片不存在，则新建图片文件
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //2.判断android版本，从而获取文件的Uri对象
                //如果Android版本高于Android7.0，则要用内容提供器来获取应用关联缓存路径的Uri对象，否则会出现FileUriExposedException
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.xavier.cameraalbumtest.fileprovider", outputImage);
                    Log.d(TAG, "onClick: SDK_INT >= 24");
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //3.启动相机应用程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //相机程序的Action
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将图片的输出地址设置为imageUri
                startActivityForResult(intent, TAKE_PHOTO);//以待返回结果的形式启动相机
            }
        });
        //打开相册的按键监听器
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否具有相册权限，没有的话就申请
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //具有权限的话，直接打开相册
                    openAlbum();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //显示拍摄的图片
                        //用Bitmap的工厂函数的decodeStream方法解析imageUri为Bitmap对象，
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        /*
                        可在此处用OpenCV做图像处理
                        imageProcessing(Bitmap bitmap);
                        */
                        //然后在ImageView中显示出来
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //判断Android版本号
                        if (Build.VERSION.SDK_INT >= 19) {
                            //如果系统版本超过4.4,则用这个方法处理图片
                            handleImageOnKitKat(data);
                        } else {
                            //如果系统版本低于4.4,则用这个方法处理图片
                            handleImageBeforeKitKat(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            default:
                break;
        }
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析数字格式的Id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if ("com.android.providers.downloads.document".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,则直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath); //根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        Log.d(TAG, "getImagePath: ");
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }

    private void displayImage(String imagePath) {
        Log.d(TAG, "displayImage: ");
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
