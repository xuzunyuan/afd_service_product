package com.afd.product.dao;

import com.afd.model.product.ProductCommentStat;

public interface ProductCommentStatMapper {
    int deleteByPrimaryKey(Integer pCSId);

    int insert(ProductCommentStat record);

    int insertSelective(ProductCommentStat record);

    ProductCommentStat selectByPrimaryKey(Integer pCSId);

    int updateByPrimaryKeySelective(ProductCommentStat record);

    int updateByPrimaryKey(ProductCommentStat record);
}