package com.afd.product.dao;

import com.afd.model.product.Spec;

public interface SpecMapper {
    int deleteByPrimaryKey(Integer specId);

    int insert(Spec record);

    int insertSelective(Spec record);

    Spec selectByPrimaryKey(Integer specId);

    int updateByPrimaryKeySelective(Spec record);

    int updateByPrimaryKey(Spec record);
}