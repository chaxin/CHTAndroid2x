package com.damenghai.chahuitong2.bean;

import java.util.ArrayList;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class Store {
    private String freight;
    private String store_mansong_rule_list;
    private String store_name;
    private String store_goods_total;

    private ArrayList<Goods> goods_list;

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getStore_mansong_rule_list() {
        return store_mansong_rule_list;
    }

    public void setStore_mansong_rule_list(String store_mansong_rule_list) {
        this.store_mansong_rule_list = store_mansong_rule_list;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_goods_total() {
        return store_goods_total;
    }

    public void setStore_goods_total(String store_goods_total) {
        this.store_goods_total = store_goods_total;
    }

    public ArrayList<Goods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<Goods> goods_list) {
        this.goods_list = goods_list;
    }

}
