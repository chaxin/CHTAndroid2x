package com.damenghai.chahuitong.bean;

/**
 * Created by Sgun on 15/8/18.
 */
public class Brand {
    private int brand_id;
    private String brand_name;
    private String brand_initial;
    private String brand_class;
    private String brand_pic;
    private String brand_sort;
    private String brand_recommend;
    private String store_id;
    private String brand_apply;
    private String class_id;
    private String show_type;

    public int getId() {
        return brand_id;
    }

    public void setId(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return brand_name;
    }

    public void setName(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getInitial() {
        return brand_initial;
    }

    public void setInitial(String brand_initial) {
        this.brand_initial = brand_initial;
    }

    public String getCategory() {
        return brand_class;
    }

    public void setCategory(String brand_class) {
        this.brand_class = brand_class;
    }

    public String getPic() {
        return brand_pic;
    }

    public void setPic(String brand_pic) {
        this.brand_pic = brand_pic;
    }

    public String getSort() {
        return brand_sort;
    }

    public void setSort(String brand_sort) {
        this.brand_sort = brand_sort;
    }

    public String getRecommend() {
        return brand_recommend;
    }

    public void setRecommend(String brand_recommend) {
        this.brand_recommend = brand_recommend;
    }

    public String getStoreId() {
        return store_id;
    }

    public void setStoreId(String store_id) {
        this.store_id = store_id;
    }

    public String getApply() {
        return brand_apply;
    }

    public void setApply(String brand_apply) {
        this.brand_apply = brand_apply;
    }

    public String getCategoryId() {
        return class_id;
    }

    public void setCategoryId(String class_id) {
        this.class_id = class_id;
    }

    public String getType() {
        return show_type;
    }

    public void setType(String show_type) {
        this.show_type = show_type;
    }
}
