package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BcSpec;
import com.afd.model.product.vo.BcSpecVO;

public interface BcSpecMapper {
	/**
     * 根据ID删除
     * @param bcSpecId
     * @return
     */
    boolean deleteBcSpecById(Long bcSpecId);

    /**
     * 插入类目规格
     * @param bcSpec
     * @return
     */
    boolean insertBcSpec(BcSpec bcSpec);

    int insertSelective(BcSpec record);

    /**
     * 获取类目配置信息
     * @param bcSpecId
     * @return
     */
    BcSpec getBcSpecById(Long bcSpecId);

    /**
     * 更新信息
     * @param bcSpec
     * @return
     */
    boolean updateBcSpecById(BcSpec bcSpec);

    int updateByPrimaryKey(BcSpec record);
    
    /**
	 * @param bcId 类目ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateBcSpecOrders(@Param("bcId") Integer bcId, @Param("order") Integer order, @Param("value") Integer value);
	
	/**
     * 获取指定类目的规格名称列表
     * @param bcId 类目ID
     * @param status 
     * @return
     */
    List<BcSpecVO> getBcSpecByBcId(@Param(value = "bcId")Integer bcId, @Param(value = "status") String status);
    
    /**
     * 根据规格ID获取关联的基本类目ID列表
     * @param specId 规格ID
     * @param status 
     * @return
     */
    List<Integer> getBcIdBySpecId(@Param(value = "specId")Long specId, @Param(value = "status") String status);
}