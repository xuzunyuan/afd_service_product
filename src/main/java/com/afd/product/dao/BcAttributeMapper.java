package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BcAttribute;
import com.afd.model.product.vo.BcAttributeVO;

public interface BcAttributeMapper {
	boolean deleteBcAttributeById(Long bcAttrId);

    int insert(BcAttribute record);

    boolean insertBcAttribute(BcAttribute record);

    BcAttribute getBcAttributeById(Long bcAttrId);

    int updateByPrimaryKeySelective(BcAttribute record);

    boolean updateBcAttributeById(BcAttribute record);
    
    /**
     * 获取最大显示数
     * @param bcId 类目ID
     * @return
     */
    Integer getMaxOrder(Integer bcId);
    
    
    /**
     * @param bcId 类目ID
     * @param attrId 属性ID
     * @param status
     * @return
     */
    BcAttribute getBcAttributeByAttrIdAndBcId(@Param(value = "bcId") Integer bcId, @Param(value = "attrId")Long attrId, @Param(value = "status") String status);
    
    /**
     * 获取类目的属性名列表
     * @param bcId 类目ID
     * @param status
     * @return
     */
    List<BcAttributeVO> getBcAttributeByBcId(@Param(value = "bcId") Integer bcId, @Param(value = "status") String status);
    
    /**
	 * @param bcId 类目ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateBcAttributeOrders(@Param("bcId") Integer bcId, @Param("order") Integer order, @Param("value") Integer value);
}