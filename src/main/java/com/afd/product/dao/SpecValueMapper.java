package com.afd.product.dao;

import com.afd.model.product.SpecValue;

public interface SpecValueMapper {
    int deleteByPrimaryKey(Integer specValueId);

    int insert(SpecValue record);

    int insertSelective(SpecValue record);

    SpecValue selectByPrimaryKey(Integer specValueId);

    int updateByPrimaryKeySelective(SpecValue record);

    int updateByPrimaryKey(SpecValue record);
}