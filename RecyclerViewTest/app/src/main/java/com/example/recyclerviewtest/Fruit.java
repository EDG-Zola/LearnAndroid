package com.example.recyclerviewtest;

/**
 * Created by Administrator on 2018\3\7 0007.
 * 新建的一个Fruit类
 * 成员变量：
 * 水果名---name
 * 水果图片Id号---imageId
 */

public class Fruit {
    private String name;
    private int imageId;

    public Fruit(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
