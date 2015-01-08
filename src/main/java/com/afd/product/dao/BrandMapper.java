package com.afd.product.dao;

import com.afd.model.product.Brand;

public interface BrandMapper {
    int insert(Brand record);

    int insertSelective(Brand record);
}