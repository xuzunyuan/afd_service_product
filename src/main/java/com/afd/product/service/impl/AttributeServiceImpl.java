package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;

import com.afd.common.mybatis.Page;
import com.afd.model.product.AttrAttrValue;
import com.afd.model.product.Attribute;
import com.afd.model.product.AttributeValue;
import com.afd.model.product.vo.AttrAttrValueVO;
import com.afd.service.product.IAttributeService;

public class AttributeServiceImpl implements IAttributeService {

	@Override
	public Long insertAttribute(Attribute attrbute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteAttributeById(Long attrId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Attribute getAttributeById(Long attrId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute getAttributeByName(String attrName, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Attribute> getAttributes(Map<?, ?> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Attribute> getAttributeByPage(Map<?, ?> map,
			Page<Attribute> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAttributeById(Attribute attrbute) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAttrAttrValueById(Long aAvId, Boolean sub) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long insertAttrAttrValue(AttrAttrValue attrAttrValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAttrAttrValueById(AttrAttrValue attrAttrValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AttrAttrValue getAttrAttrValueById(Long aAvId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttrAttrValue getAttrAttrValueByAttrIdAndAVId(Long attrId,
			Long attrValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttrAttrValue getAttrAttrValueByPAAvIdAndAVId(Long pAAvId,
			Long attrValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttrAttrValueVO> getAttrAttrValueByAttrId(Long attrId,
			Boolean sub) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttrAttrValueVO> getAttrAttrValueByPAAvId(Long pAAvId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAttrAttrValueOrders(Integer flag, Long sAAvId,
			Long dAAvId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int deleteAttributeValueById(Long attrValueId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long insertAttributeValue(AttributeValue attrValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeValue getAttributeValueById(Long attrValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeValue getAttributeValueByName(String attrValueName,
			String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttributeValue> getAttributeValues(Map<?, ?> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<AttributeValue> getAttributeValueByPage(Map<?, ?> map,
			Page<AttributeValue> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAttributeValueById(AttributeValue attrValue) {
		// TODO Auto-generated method stub
		return false;
	}

}
