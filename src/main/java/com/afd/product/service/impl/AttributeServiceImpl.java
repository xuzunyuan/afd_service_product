package com.afd.product.service.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.constants.SystemConstants;
import com.afd.constants.product.ProductConstants;
import com.afd.model.product.AttrAttrValue;
import com.afd.model.product.Attribute;
import com.afd.model.product.AttributeValue;
import com.afd.model.product.BcAttrValue;
import com.afd.model.product.vo.AttrAttrValueVO;
import com.afd.product.dao.AttrAttrValueMapper;
import com.afd.product.dao.AttributeMapper;
import com.afd.product.dao.AttributeValueMapper;
import com.afd.product.dao.BcAttrValueMapper;
import com.afd.product.dao.BcAttributeMapper;
import com.afd.service.product.IAttributeService;

@Service("attributeService")
public class AttributeServiceImpl implements IAttributeService {

	@Autowired
	private BcAttributeMapper bcAttrMapper;
	@Autowired
	private AttributeMapper attrMapper;
	@Autowired
	private AttributeValueMapper attrValueMapper;
	@Autowired
	private AttrAttrValueMapper attrAttrValueMapper;
	@Autowired
	private BcAttrValueMapper bcAttrValueMapper;
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public Long insertAttribute(Attribute attrbute) {
		Long result = 0l;
		
		if(this.attrMapper.insertAttribute(attrbute)){
			result = attrbute.getAttrId();
		}
		
		return result;
	}

	@Override
	public int deleteAttributeById(Long attrId) {
		int result = 1;
		//根据属性ID获取关联该属性的类目ID列表
		List<Integer> bcIds = this.bcAttrMapper.getBcAttributeByAttrId(attrId, SystemConstants.DB_STATUS_VALID);
		if(bcIds==null || bcIds.isEmpty()){
			if(!this.attrMapper.deleteAttributeById(attrId)){
				result = 0;
			}else{
				//删除引用该属性的子级属性关系
				List<AttrAttrValueVO> attrAttrValues = this.attrAttrValueMapper.getAttrAttrValueByAttrId(attrId);
				if(attrAttrValues!=null && attrAttrValues.size()>0){
					this.attrAttrValueMapper.deleteSubAttrAttrValueByAttrId(attrId);
					
					//删除属性属性关系表中引用该属性的所有关系
					this.attrAttrValueMapper.deleteAttrAttrValueByAttrId(attrId);
				}
			}
		}else{
			result = -1;
		}
		
		return result;
	}

	@Override
	public Attribute getAttributeById(Long attrId) {
		return this.attrMapper.getAttributeById(attrId);
	}

	@Override
	public Attribute getAttributeByName(String attrName, String status) {
		return this.attrMapper.getAttributeByName(attrName, status);
	}

	@Override
	public List<Attribute> getAttributes(Map<?, ?> map) {
		return this.attrMapper.getAttributes(map);
	}

	@Override
	public Page<Attribute> getAttributeByPage(Map<?, ?> map, Page<Attribute> page) {
		page.setResult(this.attrMapper.getAttributeByPage(map, page));
		
		return page;
	}

	@Override
	public boolean updateAttributeById(Attribute attrbute) {
		boolean result = this.attrMapper.updateAttributeById(attrbute);
		
		//修改成功更新缓存
		if(result){
			//根据属性ID获取关联该属性的类目ID列表
			List<Integer> bcIds = this.bcAttrMapper.getBcAttributeByAttrId(attrbute.getAttrId(), SystemConstants.DB_STATUS_VALID);
			
			if(bcIds!=null && bcIds.size()>0){
				Object[] keys = new Object[bcIds.size()];
				
				for(int i=0; i<bcIds.size(); i++){
					keys[i] = ProductConstants.BC_ATTR_SPEC_BRAND + bcIds.get(i);
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean deleteAttrAttrValueById(Long aAvId, Boolean sub) {
		boolean re = true;
		if(sub){
			re = this.attrAttrValueMapper.deleteAttrAttrValueByPAAvId(aAvId) && re;
		}
		re = this.attrAttrValueMapper.deleteAttrAttrValueById(aAvId) && re;
		
		return re;
	}

	@Override
	public Long insertAttrAttrValue(AttrAttrValue attrAttrValue) {
		Long id = 0l;
		if(this.attrAttrValueMapper.insertAttrAttrValue(attrAttrValue)){
			id = attrAttrValue.getaAvId();
		}
		 
		return id;
	}

	@Override
	public boolean updateAttrAttrValueById(AttrAttrValue attrAttrValue) {
		return this.attrAttrValueMapper.updateByAAvId(attrAttrValue);
	}

	@Override
	public AttrAttrValue getAttrAttrValueById(Long aAvId) {
		return this.attrAttrValueMapper.getAttrAttrValueById(aAvId);
	}

	@Override
	public AttrAttrValue getAttrAttrValueByAttrIdAndAVId(Long attrId, Long attrValueId) {
		return this.attrAttrValueMapper.getAttrAttrValueByAttrIdAndAVId(attrId, attrValueId);
	}

	@Override
	public AttrAttrValue getAttrAttrValueByPAAvIdAndAVId(Long pAAvId, Long attrValueId) {
		return this.attrAttrValueMapper.getAttrAttrValueByPAAvIdAndAVId(pAAvId, attrValueId);
	}

	@Override
	public List<AttrAttrValueVO> getAttrAttrValueByAttrId(Long attrId, Boolean sub) {
		List<AttrAttrValueVO> attrAttrValues = this.attrAttrValueMapper.getAttrAttrValueByAttrId(attrId);
		
		if(sub && attrAttrValues!=null && attrAttrValues.size()>0){
			for(AttrAttrValueVO attrAttrValue: attrAttrValues){
				if(attrAttrValue.getHasSub()){
					attrAttrValue.setSubList(this.getAttrAttrValueByPAAvId(attrAttrValue.getaAvId()));
				}
			}
		}
		
		return attrAttrValues;
	}

	@Override
	public List<AttrAttrValueVO> getAttrAttrValueByPAAvId(Long pAAvId) {
		return this.attrAttrValueMapper.getAttrAttrValueByPAAvId(pAAvId);
	}

	@Override
	public boolean updateAttrAttrValueOrders(Integer flag, Long sAAvId, Long dAAvId) {
		boolean result = false;
		AttrAttrValue saav = this.getAttrAttrValueById(sAAvId);
		AttrAttrValue daav = this.getAttrAttrValueById(dAAvId);
		
		//向上或向下
		if(Math.abs(saav.getDisplayOrder()-daav.getDisplayOrder()) == 1){
			if(this.attrAttrValueMapper.updateAttrAttrValueOrderByAAvId(sAAvId, daav.getDisplayOrder()) && 
					this.attrAttrValueMapper.updateAttrAttrValueOrderByAAvId(dAAvId, saav.getDisplayOrder())){
				result = true;
			}
		}else{
			Integer value = 0;
			if(saav.getDisplayOrder()>daav.getDisplayOrder() && saav.getDisplayOrder()-daav.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(saav.getDisplayOrder()<daav.getDisplayOrder() && daav.getDisplayOrder()-saav.getDisplayOrder()>1){//置底
				value = -1;
			}	
			
			if(this.attrAttrValueMapper.updateAttrAttrValueOrders(flag, flag==0?saav.getAttrId():saav.getpAAvId(), saav.getDisplayOrder(), value) && 
					this.attrAttrValueMapper.updateAttrAttrValueOrderByAAvId(sAAvId, daav.getDisplayOrder())){
				result = true;
			}
		}
		
		return result;
	}

	@Override
	public int deleteAttributeValueById(Long attrValueId) {
		int result = 1;
		
		//根据属性值ID获取关联该属性值的类目属性关系ID列表
		List<BcAttrValue> bcAttrValues = this.bcAttrValueMapper.getBcAttrValueByAttrValueId(attrValueId);
		if(bcAttrValues==null || bcAttrValues.isEmpty()){
			if(this.attrValueMapper.deleteAttributeValueById(attrValueId)){
				List<AttrAttrValue> attrAttrValues = this.attrAttrValueMapper.getAttrAttrValueByAttrAttrValueId(attrValueId);
				if(attrAttrValues!=null && attrAttrValues.size()>0){
					//删除引用该属性值的子级属性关系
					this.attrAttrValueMapper.deleteSubAttrAttrValueByAttrValueId(attrValueId);
				}
				//删除属性属性值关系表中引用该属性值的所有关系数据
				this.attrAttrValueMapper.deleteAttrAttrValueByAttrValueId(attrValueId);
			}else{
				result = 0;
			}
		}else{
			result = -1;
		}
		
		return result;
	}

	@Override
	public Long insertAttributeValue(AttributeValue attrValue) {
		Long result = 0l;
		
		if(this.attrValueMapper.insertAttributeValue(attrValue)){
			result = attrValue.getAttrValueId();
		}
		
		return result;
	}

	@Override
	public AttributeValue getAttributeValueById(Long attrValueId) {
		return this.attrValueMapper.getAttributeValueById(attrValueId);
	}

	@Override
	public AttributeValue getAttributeValueByName(String attrValueName, String status) {
		return this.attrValueMapper.getAttributeValueByName(attrValueName, status);
	}

	@Override
	public List<AttributeValue> getAttributeValues(Map<?, ?> map) {
		return this.attrValueMapper.getAttributeValues(map);
	}

	@Override
	public Page<AttributeValue> getAttributeValueByPage(Map<?, ?> map, Page<AttributeValue> page) {
		page.setResult(this.attrValueMapper.getAttributeValueByPage(map, page));
		
		return page;
	}

	@Override
	public boolean updateAttributeValueById(AttributeValue attrValue) {
		boolean result = this.attrValueMapper.updateAttributeValueById(attrValue);
		//修改属性值成功,清空引用该属性值的类目的缓存
		if(result){
			//根据属性值ID获取关联该属性值的类目属性值列表
			List<BcAttrValue> bcAttrValues = this.bcAttrValueMapper.getBcAttrValueByAttrValueId(attrValue.getAttrValueId());
			if(bcAttrValues!=null && bcAttrValues.size()>0){
				Set<Object> keys = new HashSet<Object>();
				
				for(BcAttrValue av : bcAttrValues){
					keys.add(ProductConstants.BC_ATTR_SPEC_BRAND + av.getBcId());
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys.toArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

}
