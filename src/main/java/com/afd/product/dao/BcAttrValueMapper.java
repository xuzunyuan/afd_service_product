package com.afd.product.dao;

import com.afd.model.product.BcAttrValue;

public interface BcAttrValueMapper {
    int deleteByPrimaryKey(Integer bcAvId);

    int insert(BcAttrValue record);

    int insertSelective(BcAttrValue record);

    BcAttrValue selectByPrimaryKey(Integer bcAvId);

    int updateByPrimaryKeySelective(BcAttrValue record);

    int updateByPrimaryKey(BcAttrValue record);
}