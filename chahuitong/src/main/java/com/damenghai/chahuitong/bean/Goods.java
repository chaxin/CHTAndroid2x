package com.damenghai.chahuitong.bean;

import java.io.Serializable;

/**
 * Created by Sgun on 15/8/13.
 */
public class Goods implements Serializable {
    String goods_id;
    String goods_name;
    String xianshi_price;
    String goods_image;
    String xianshi_explain;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return goods_name;
    }

    public void setName(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return xianshi_price;
    }

    public void setPrice(String xianshi_price) {
        this.xianshi_price = xianshi_price;
    }

    public String getImageUrl() {
        return goods_image;
    }

    public void setImageUrl(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getDescription() {
        return xianshi_explain;
    }

    public void setDescription(String xianshi_explain) {
        this.xianshi_explain = xianshi_explain;
    }
}
