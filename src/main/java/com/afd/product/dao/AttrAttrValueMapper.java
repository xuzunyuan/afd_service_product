package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.AttrAttrValue;
import com.afd.model.product.vo.AttrAttrValueVO;

public interface AttrAttrValueMapper {
	 /**
     * @param aAvId 属性和属性值关系ID
     * @return
     */
    boolean deleteAttrAttrValueById(Long aAvId);

    /**
	 * @param AttrAttrValue
	 * @return 成功:属性和属性值关系ID,否则:0
	 */
	boolean insertAttrAttrValue(AttrAttrValue attrAttrValue);

    int insertSelective(AttrAttrValue record);

    AttrAttrValue getAttrAttrValueById(Long aAvId);

    int updateByPrimaryKeySelective(AttrAttrValue record);

    boolean updateByAAvId(AttrAttrValue attrAttrValue);
    
    
    /**
	 * @param attrId 属性ID
	 * @param attrValueId 属性值ID
	 * @return
	 */
	AttrAttrValue getAttrAttrValueByAttrIdAndAVId(@Param("attrId")Long attrId, @Param("attrValueId")Long attrValueId);
	
	/**
     * 删除子属性关系中所属指定属性ID的关系
     * @param attrId
     * @return
     */
    boolean deleteSubAttrAttrValueByAttrId(Long attrId);
    
    /**
     * @param attrId 属性ID
     * @return
     */
    boolean deleteAttrAttrValueByAttrId(Long attrId);
    
    /**
	 * @param attrId 属性ID
	 * @return
	 */
	List<AttrAttrValueVO> getAttrAttrValueByAttrId(Long attrId);
	
	/**
     * @param aAvId 父ID
     * @return
     */
    boolean deleteAttrAttrValueByPAAvId(Long aAvId);
    
    /**
	 * @param pAAvId 父关系ID
	 * @param attrValueId 属性值ID
	 * @return
	 */
	AttrAttrValue getAttrAttrValueByPAAvIdAndAVId(@Param("pAAvId")Long pAAvId, @Param("attrValueId")Long attrValueId);
	
	/**
	 * @param pAAvId 属性和属性值关系父ID
	 * @return
	 */
	List<AttrAttrValueVO> getAttrAttrValueByPAAvId(Long pAAvId);
	
	/**
	 * @param aAvId 关系ID
	 * @param displayOrder 要显示的位置
	 * @return
	 */
	boolean updateAttrAttrValueOrderByAAvId(@Param("aAvId") Long aAvId, @Param("displayOrder") Integer displayOrder);
	
	
	/**
	 * @param flag 0:按属性,1:按父属性和值关系
	 * @param id flag＝0时:按属性ID,flag＝1时:按父属性值关系ID,
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateAttrAttrValueOrders(@Param("flag") Integer flag, @Param("id") Long id, @Param("order") Integer order, @Param("value") Integer value);
	
	/**
	 * 获取指定属性值ID并且有子级关系的关系列表
	 * @param attrValueId 属性值ID
	 * @return
	 */
	List<AttrAttrValue> getAttrAttrValueByAttrAttrValueId(@Param("attrValueId")Long attrValueId);
	
	/**
     * 删除子属性关系中所属指定属性值ID的关系
     * @param attrValueId
     * @return
     */
    boolean deleteSubAttrAttrValueByAttrValueId(Long attrValueId);
    
    /**
     * @param attrValueId 属性值ID
     * @return
     */
    boolean deleteAttrAttrValueByAttrValueId(Long attrValueId);
}