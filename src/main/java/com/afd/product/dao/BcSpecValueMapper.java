package com.afd.product.dao;

import com.afd.model.product.BcSpecValue;

public interface BcSpecValueMapper {
    int deleteByPrimaryKey(Integer bcSvId);

    int insert(BcSpecValue record);

    int insertSelective(BcSpecValue record);

    BcSpecValue selectByPrimaryKey(Integer bcSvId);

    int updateByPrimaryKeySelective(BcSpecValue record);

    int updateByPrimaryKey(BcSpecValue record);
}