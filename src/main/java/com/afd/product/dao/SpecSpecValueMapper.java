package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.SpecSpecValue;
import com.afd.model.product.vo.SpecSpecValueVO;

public interface SpecSpecValueMapper {
	boolean insertSpecSpecValue(SpecSpecValue specSpecValue);

	boolean deleteSpecSpecValueById(Long sSVId);

    boolean deleteSpecSpecValueBySpecId(Long specId);
    
    boolean deleteSpecSpecValueBySpecValueId(Long specValueId);
    
    int insertSelective(SpecSpecValue record);

    SpecSpecValue getSpecSpecValueById(Long sSVId);

    int updateByPrimaryKeySelective(SpecSpecValue record);

    int updateByPrimaryKey(SpecSpecValue record);
    
    /**
	 * @param sSVIds
	 * @return
	 */
	int deleteSpecSpecValuesById(@Param("sSVIds") List<Long> sSVIds);
	
	/**
	 * @param specId 规格ID
	 * @return
	 */
	List<SpecSpecValueVO> getSpecSpecValueByspecId(Long specId);
	
	/**
     * 检查规格名和规格值关系是否存在
     * @param specId 
     * @param specValueId
     * @return
     */
	SpecSpecValue getSpecSpecValueBySpecIdAndSpecValueId(@Param("specId") Long specId, @Param("specValueId") Long specValueId);
	
	/**
	 * @param sSVId 关系ID
	 * @param displayOrder 要显示的位置
	 * @return
	 */
	boolean updateSpecSpecOrderById(@Param("sSVId") Long sSVId, @Param("displayOrder") Integer displayOrder);
	
	/**
	 * @param specId 规格名ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateSpecSpecOrders(@Param("specId") Long specId, @Param("order") Integer order, @Param("value") Integer value);
}