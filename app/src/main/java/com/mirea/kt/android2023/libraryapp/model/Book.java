package com.mirea.kt.android2023.libraryapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject {

    @PrimaryKey
    private int article;
    private String title;
    private String author;
    private int rackNumber;
    private int shelfNumber;

    public Book(int article, String title, String author, int rackNumber, int shelfNumber) {
        this.article = article;
        this.title = title;
        this.author = author;
        this.rackNumber = rackNumber;
        this.shelfNumber = shelfNumber;
    }

    public Book() {
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRackNumber() {
        return rackNumber;
    }

    public void setRackNumber(int rackNumber) {
        this.rackNumber = rackNumber;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }
}
