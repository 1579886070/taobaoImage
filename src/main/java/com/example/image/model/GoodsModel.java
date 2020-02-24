package com.example.image.model;

import java.util.List;

/**
 * @author Administrator
 * @date 2020/2/23 13:29
 */
public class GoodsModel {
    private String id;
    private String title;
    private String price;
    private List<String> images;
    private String basicInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    @Override
    public String toString() {
        return "GoodsModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", images=" + images +
                ", basicInfo='" + basicInfo + '\'' +
                '}';
    }
}
