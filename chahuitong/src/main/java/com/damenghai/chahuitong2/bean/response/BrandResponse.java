package com.damenghai.chahuitong2.bean.response;

import com.damenghai.chahuitong2.bean.Ad;
import com.damenghai.chahuitong2.bean.Brand;

import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class BrandResponse {
    private List<Brand> brands;

    private Ad adv;

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public Ad getAdv() {
        return adv;
    }

    public void setAdv(Ad adv) {
        this.adv = adv;
    }
}
