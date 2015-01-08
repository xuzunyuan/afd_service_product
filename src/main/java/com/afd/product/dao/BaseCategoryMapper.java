package com.afd.product.dao;

import com.afd.model.product.BaseCategory;

public interface BaseCategoryMapper {
    int deleteByPrimaryKey(Short bcId);

    int insert(BaseCategory record);

    int insertSelective(BaseCategory record);

    BaseCategory selectByPrimaryKey(Short bcId);

    int updateByPrimaryKeySelective(BaseCategory record);

    int updateByPrimaryKey(BaseCategory record);
}