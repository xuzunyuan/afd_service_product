package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Attribute;

public interface AttributeMapper {
	/**
     * 根据ID删除
     * @param attrId
     * @return
     */
    boolean deleteAttributeById(Long attrId);

    /**
     * 新增属性名信息
     * @param attrbute
     * @return
     */
    boolean insertAttribute(Attribute attrbute);

    int insertSelective(Attribute record);

    /**
     * 根据属性ID查询
     * @param attrId 属性ID
     * @return
     */
    Attribute getAttributeById(Long attrId);

    /**
     * 修改属性名信息
     * @param attrbute
     * @return
     */
	boolean updateAttributeById(Attribute attrbute);

    int updateByPrimaryKey(Attribute record);
    
    
    /**
     * 根据属性名称精确查询
     * @param name 属性名称
     * @return
     */
    Attribute getAttributeByName(@Param(value = "attrName")String attrName, @Param(value = "status") String status);
    
    /**
   	 * @param map 查询条件(若名称不为空则匹配以名称开头的即name%)
   	 * @return
   	 */
   	public List<Attribute> getAttributes(@Param("cond") Map<?, ?> map);
   	
   	/**
	 * 按属性名模糊分页查询
	 * @param map 查询条件
	 * @param page 分页信息
	 * @return
	 */
	public List<Attribute> getAttributeByPage(@Param("cond") Map<?, ?> map, @Param(value = "page") Page<Attribute> page);
}