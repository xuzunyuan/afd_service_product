package com.afd.product.dao;

import com.afd.model.product.BrandShowDetail;

public interface BrandShowDetailMapper {
    int deleteByPrimaryKey(Integer bSDId);

    int insert(BrandShowDetail record);

    int insertSelective(BrandShowDetail record);

    BrandShowDetail selectByPrimaryKey(Integer bSDId);

    int updateByPrimaryKeySelective(BrandShowDetail record);

    int updateByPrimaryKey(BrandShowDetail record);
}