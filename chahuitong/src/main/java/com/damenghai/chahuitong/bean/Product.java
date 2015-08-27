package com.damenghai.chahuitong.bean;

import com.damenghai.chahuitong.config.Constants;

import java.io.Serializable;

public class Product implements Serializable {
    private int productId, userId;

    private String imgUrls;
    private String phone;

    /**
     * 产品数量
     */
    private String weight;

    private String arrow_order;
    private String pic;
    private String content;
    private String id;
    private String unit;
    private String addtime;
    private String price;
    private String address;
    private String name;

    /**
     * 图片名称
     */
    private String depic;
    private String brand;
    private String user_id;
    private String year;
    private String saleway;

    public Product() {
        super();
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSaleway() {
        return saleway;
    }

    public void setSaleway(String saleway) {
        this.saleway = saleway;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
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

    public String getQuantity() {
        return weight;
    }

    public void setQuantity(String quantity) {
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
