package com.afd.product.dao;

import com.afd.model.product.ProductCommentAdditional;

public interface ProductCommentAdditionalMapper {
    int deleteByPrimaryKey(Integer pCAId);

    int insert(ProductCommentAdditional record);

    int insertSelective(ProductCommentAdditional record);

    ProductCommentAdditional selectByPrimaryKey(Integer pCAId);

    int updateByPrimaryKeySelective(ProductCommentAdditional record);

    int updateByPrimaryKey(ProductCommentAdditional record);
}