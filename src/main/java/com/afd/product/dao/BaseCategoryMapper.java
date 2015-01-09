package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BaseCategory;

public interface BaseCategoryMapper {
	
	public boolean deleteByBcId(Integer bcId);

    public boolean insertBaseCategory(BaseCategory record);

    public int insertSelective(BaseCategory record);

    public BaseCategory getByBcId(Integer bcId);

    public int updateByPrimaryKeySelective(BaseCategory record);

    public boolean updateBaseCategory(BaseCategory record);
    
    /**
	 * 获取指定类目的一级子类目
	 * @param pId 基本类目ID
	 * @return
	 */
	public List<BaseCategory> getBaseCategorysByPId(@Param("pId") Integer pId, @Param("status") String status);
	
	/**
	 * @param name 基本类目名称
	 * @param pBcId 父类目ID
	 * @param status 0:无效,1:有效
	 * @return
	 */
	public BaseCategory getBaseCategoryByNameAndPid(@Param("pId") Integer pId, @Param("name") String name, @Param("status") String status);
	
	/**
	 * @param pId 父ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateBaseOrder(@Param("pId") Integer pId, @Param("order") Integer order, @Param("value") Integer value);
	
	/**
	 * 获取要修改顺序的类目ID列表
	 * @param pId 父ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	List<Integer> getBCOrderIds(@Param("pId") Integer pId, @Param("order") Integer order, @Param("value") Integer value);
}