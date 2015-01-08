package com.afd.product.dao;

import com.afd.model.product.BcAttribute;

public interface BcAttributeMapper {
    int deleteByPrimaryKey(Integer bcAttrId);

    int insert(BcAttribute record);

    int insertSelective(BcAttribute record);

    BcAttribute selectByPrimaryKey(Integer bcAttrId);

    int updateByPrimaryKeySelective(BcAttribute record);

    int updateByPrimaryKey(BcAttribute record);
}