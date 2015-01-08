package com.afd.product.dao;

import com.afd.model.product.AttributeValue;

public interface AttributeValueMapper {
    int deleteByPrimaryKey(Integer attrValueId);

    int insert(AttributeValue record);

    int insertSelective(AttributeValue record);

    AttributeValue selectByPrimaryKey(Integer attrValueId);

    int updateByPrimaryKeySelective(AttributeValue record);

    int updateByPrimaryKey(AttributeValue record);
}