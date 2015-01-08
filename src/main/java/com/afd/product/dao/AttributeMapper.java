package com.afd.product.dao;

import com.afd.model.product.Attribute;

public interface AttributeMapper {
    int deleteByPrimaryKey(Integer attrId);

    int insert(Attribute record);

    int insertSelective(Attribute record);

    Attribute selectByPrimaryKey(Integer attrId);

    int updateByPrimaryKeySelective(Attribute record);

    int updateByPrimaryKey(Attribute record);
}