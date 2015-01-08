package com.afd.product.dao;

import com.afd.model.product.BcSpec;

public interface BcSpecMapper {
    int deleteByPrimaryKey(Integer bcSpecId);

    int insert(BcSpec record);

    int insertSelective(BcSpec record);

    BcSpec selectByPrimaryKey(Integer bcSpecId);

    int updateByPrimaryKeySelective(BcSpec record);

    int updateByPrimaryKey(BcSpec record);
}