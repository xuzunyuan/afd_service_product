package com.afd.product.dao;

import com.afd.model.product.AttrAttrValue;

public interface AttrAttrValueMapper {
    int deleteByPrimaryKey(Integer aAvId);

    int insert(AttrAttrValue record);

    int insertSelective(AttrAttrValue record);

    AttrAttrValue selectByPrimaryKey(Integer aAvId);

    int updateByPrimaryKeySelective(AttrAttrValue record);

    int updateByPrimaryKey(AttrAttrValue record);
}