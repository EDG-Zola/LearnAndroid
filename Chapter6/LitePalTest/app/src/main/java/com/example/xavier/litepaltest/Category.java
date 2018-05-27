package com.example.xavier.litepaltest;

/**
 * Created by Administrator on 2018\5\27 0027.
 */

public class Category {
    private int id;
    private String categoryName;
    private String categoryCode;

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getId() {
        return id;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
