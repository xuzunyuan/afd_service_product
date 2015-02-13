package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Spec;

public interface SpecMapper {
	/**
     * 删除规格信息
     * @param specId
     * @return
     */
    boolean deleteSpecById(Long specId);

    /**
	 * 新增规格名称
	 * @param spec
	 * @return
	 */
	boolean insertSpec(Spec spec);

    int insertSelective(Spec record);

    /**
     * 根据规格ID获取规格信息
     * @param specId
     * @return
     */
    Spec getSpecById(Long specId);

    /**
	 * 根据规格ID修改规格信息
	 * @param spec
	 * @return
	 */
	boolean updateSpecById(Spec spec);

    int updateByPrimaryKey(Spec record);
    
    /**
     * 根据规格名称精确查询
     * @param specName 规格名称
     * @return
     */
    Spec getSpecByName(@Param(value = "specName")String specName, @Param(value = "status") String status);
    
    /**
     * 按规格名模糊分页查询
	 * @param map 查询条件
	 * @param page 分页信息
	 * @return
	 */
	public List<Spec> getSpecByPage(@Param("cond") Map<?, ?> map, @Param(value = "page") Page<Spec> page);
	
	/**
	 * @param map 查询条件
	 * @return
	 */
	public List<Spec> getSpecs(@Param("cond") Map<?, ?> map);
}