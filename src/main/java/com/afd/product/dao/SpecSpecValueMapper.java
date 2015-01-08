package com.afd.product.dao;

import com.afd.model.product.SpecSpecValue;

public interface SpecSpecValueMapper {
    int deleteByPrimaryKey(Integer sSVId);

    int insert(SpecSpecValue record);

    int insertSelective(SpecSpecValue record);

    SpecSpecValue selectByPrimaryKey(Integer sSVId);

    int updateByPrimaryKeySelective(SpecSpecValue record);

    int updateByPrimaryKey(SpecSpecValue record);
}