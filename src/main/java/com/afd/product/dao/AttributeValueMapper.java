package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.AttributeValue;

public interface AttributeValueMapper {
	/**
     * 删除属性值
     * @param attrValueId 属性值ID
     * @return
     */
    boolean deleteAttributeValueById(Long attrValueId);

    /**
     * 插入属性值
     * @param attrValue
     * @return
     */
    boolean insertAttributeValue(AttributeValue attrValue);

    /**
     * 根据ID获取
     * @param attrValueId 属性名ID
     * @return
     */
    AttributeValue getAttributeValueById(Long attrValueId);

    /**
     * 修改属性值 
     * @param attrValue
     * @return
     */
    boolean updateAttributeValueById(AttributeValue attrValue);

    /**
     * @param attrValueName 属性名 精确匹配
     * @param status 状态 1：有效,0：无效
     * @return
     */
    AttributeValue getAttributeValueByName(@Param(value = "attrValueName") String attrValueName, @Param(value = "status") String status);
    
    
    /**
     * 根据条件模糊查询
     * @param map 查询条件
     * @param status 状态 1：有效,0：无效
     * @return
     */
    List<AttributeValue> getAttributeValues(@Param("cond") Map<?, ?> map);
    
    /**
     * 根据条件模糊分页查询
     * @param map 查询条件
     * @param page 分页信息
     * @return
     */
    List<AttributeValue> getAttributeValueByPage(@Param("cond") Map<?, ?> map, @Param(value = "page") Page<AttributeValue> page);
    
}