package com.damenghai.chahuitong.bean;

import com.damenghai.chahuitong.config.Constants;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String brand;
    private String name;
    private String year;
    private String address;
    private String price;
    private int weight;
    private int timeout;
    private String arrow_order;
    private String phone;
    private String pic;
    private String saleway;
    private String unit;
    private int recommend;
    private String addtime;
    private String depic;
    private String content;

    public String getArrow_order() {
        return arrow_order;
    }

    public void setArrow_order(String arrow_order) {
        this.arrow_order = arrow_order;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRecommend() {
        return recommend != 0;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend ? 1 : 0;
    }

    public String getSaleway() {
        return saleway;
    }

    public void setSaleway(String saleway) {
        this.saleway = saleway;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrls() {
        return depic;
    }

    public void setImgUrls(String imgName) {
        this.depic = imgName;
    }

    public String getImgUrl() {
        return Constants.IMAGE_URL + pic;
    }

    public void setImgUrl(String imgName) {
        this.pic = imgName;
    }

    public String getTitle() {
        return brand + name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getQuantity() {
        return weight;
    }

    public void setQuantity(int quantity) {
        this.weight = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return addtime;
    }

    public void setDate(String date) {
        this.addtime = date;
    }

    public String getDesc() {
        return content;
    }

    public void setDesc(String desc) {
        this.content = desc;
    }

}
