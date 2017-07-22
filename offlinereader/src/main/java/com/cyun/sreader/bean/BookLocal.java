package com.cyun.sreader.bean;

import java.io.Serializable;

/**
 * 本地图书
 */
public class BookLocal implements Serializable {

    private String name;
    private String path;
    private String rate;

    public BookLocal() {

    }

    public BookLocal(String name, String path, String rate) {
        this.name = name;
        this.path = path;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "BookLocal{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookLocal bookLocal = (BookLocal) o;

        return getPath().equals(bookLocal.getPath());

    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }
}
