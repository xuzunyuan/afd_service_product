package com.afd.product.dao;

import com.afd.model.product.SellerBrand;

public interface SellerBrandMapper {
    int deleteByPrimaryKey(Integer sellerBrandId);

    int insert(SellerBrand record);

    int insertSelective(SellerBrand record);

    SellerBrand selectByPrimaryKey(Integer sellerBrandId);

    int updateByPrimaryKeySelective(SellerBrand record);

    int updateByPrimaryKey(SellerBrand record);
}