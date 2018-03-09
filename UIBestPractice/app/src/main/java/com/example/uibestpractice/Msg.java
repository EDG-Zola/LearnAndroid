package com.example.uibestpractice;

/**
 * Created by Administrator on 2018\3\8 0008.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;
    public Msg(String content, int type){
        content = this.content;
        type = this.type;
    }
    public String getContent(){
        return content;
    }
    public int getType(){
        return type;
    }
}
