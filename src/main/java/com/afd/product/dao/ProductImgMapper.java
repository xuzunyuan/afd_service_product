package com.afd.product.dao;

import com.afd.model.product.ProductImg;

public interface ProductImgMapper {
    int deleteByPrimaryKey(Integer prodImgId);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Integer prodImgId);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);
}