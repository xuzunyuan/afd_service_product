package com.afd.product.dao;

import com.afd.model.product.Product;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer prodId);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer prodId);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);
}