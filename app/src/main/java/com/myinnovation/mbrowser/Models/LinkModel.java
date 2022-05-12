package com.myinnovation.mbrowser.Models;

public class LinkModel {

    int url_img;
    String Url_image, Url_name, url;

    public LinkModel(String url_name, String url_image, String url) {
        this.Url_image = url_image;
        this.Url_name = url_name;
        this.url = url;
    }

    public LinkModel(String url_name, int url_img, String url) {
        this.url_img = url_img;
        this.Url_name = url_name;
        this.url = url;
    }

    public LinkModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUrl_img() {
        return url_img;
    }

    public void setUrl_img(int url_img) {
        this.url_img = url_img;
    }

    public String getUrl_image() {
        return Url_image;
    }

    public void setUrl_image(String url_image) {
        Url_image = url_image;
    }

    public String getUrl_name() {
        return Url_name;
    }

    public void setUrl_name(String url_name) {
        Url_name = url_name;
    }
}
