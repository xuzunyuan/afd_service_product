package com.afd.product.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.common.util.DateUtils;
import com.afd.constants.product.ProductConstants;
import com.afd.constants.product.ProductConstants.BrandShow$Status;
import com.afd.model.product.BrandShow;
import com.afd.model.product.BrandShowDetail;
import com.afd.product.dao.BrandShowDetailMapper;
import com.afd.product.dao.BrandShowMapper;
import com.afd.service.product.IBrandShowService;

@Service("brandShowService")
public class BrandShowServiceImpl implements IBrandShowService {

	private static final Logger log = LoggerFactory
			.getLogger(BrandShowServiceImpl.class);

	@Autowired
	private BrandShowMapper brandShowMapper;
	@Autowired
	private BrandShowDetailMapper brandShowDetailMapper;
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public BrandShow getBrandShowById(Long brandShowId) {
		return this.brandShowMapper.selectByPrimaryKey(brandShowId.intValue());
	}

	@Override
	public BrandShowDetail getBrandShowDetailById(Long brandShowDetailId) {
		return this.brandShowDetailMapper.selectByPrimaryKey(brandShowDetailId
				.intValue());
	}

	@Override
	public List<BrandShow> getBrandShowByIds(List<Long> brandShowIds) {
		return this.brandShowMapper.getBrandShowByIds(brandShowIds);
	}

	@Override
	public List<BrandShowDetail> getBrandShowDetailsByIds(
			List<Long> brandShowDetailIds) {
		List<BrandShowDetail> bsds = this.brandShowDetailMapper
				.getBrandShowDetailsByIds(brandShowDetailIds);
		if (null != bsds && bsds.size() != 0) {
			for (BrandShowDetail bsd : bsds) {
				Long stock = 0l;
				if (null == bsd.getShowBalance() || bsd.getShowBalance() <= 0) {
					bsd.setShowBalance(0l);
					stock = 0l;
				} else if (null == bsd.getSaleAmount()
						|| bsd.getSaleAmount() <= 0) {
					bsd.setSaleAmount(0l);
					stock = bsd.getShowBalance();
				} else {
					stock = bsd.getShowBalance() - bsd.getSaleAmount();
				}
				try {
					String strStock = this.redisTemplate.opsForValue().get(
							ProductConstants.CACHE_PERFIX_INVENTORY
									+ bsd.getbSDId());
					if (StringUtils.isBlank(strStock)
							|| "null".equals(strStock)) {
						strStock = stock + "";
						this.redisTemplate.opsForValue().set(
								ProductConstants.CACHE_PERFIX_INVENTORY
										+ bsd.getbSDId(), strStock);
					} else {
						stock = Long.parseLong(strStock);
						Long saleAmount = bsd.getShowBalance() - stock;
						if (saleAmount >= 0) {
							bsd.setSaleAmount(saleAmount);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return bsds;
	}

	@Override
	public List<BrandShow> getValidBrandShows(BrandShow record) {
		return this.brandShowMapper.getValidBrandShows(record);
	}

	@Override
	public BigDecimal getLowestPrice(Long bsid) {
		return this.brandShowDetailMapper.getLowestPrice(bsid);
	}

	@Override
	public Page<BrandShowDetail> getBrandShowDetailByPage(Map<?, ?> map,
			Page<BrandShowDetail> page) {

		page.setResult(this.brandShowDetailMapper.getBrandShowDetailByPage(map,
				page));
		return page;
	}

	/**
	 * minus saled amount
	 * 
	 * @param stockMap
	 */
	public void addStock(Map<Long, Long> stockMap) {
		if (null != stockMap && stockMap.size() > 0) {
			for (Entry<Long, Long> entry : stockMap.entrySet()) {
				Long bsdId = entry.getKey();
				Long stock = entry.getValue();
				this.brandShowDetailMapper.addStock(bsdId, stock);
			}
		}
	}

	@Override
	public long newBrandShow(BrandShow brandShow) {
		brandShow.setStatus(BrandShow$Status.EDITING);
		brandShow.setCreateByDate(DateUtils.currentDate());

		int result = brandShowMapper.insert(brandShow);

		return result > 0 ? brandShow.getBrandShowId() : 0;
	}

	@Override
	public long submitNewBrandShow(long brandShowId, BrandShowDetail[] details) {
		// BrandShow brandShow =
		// brandShowMapper.selectByPrimaryKey(brandShowId);

		return 0;
	}

	@Override
	public long modifyBrandShow(BrandShow brandShow) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long submitModifyBrandShow(long brandShowId,
			BrandShowDetail[] details) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<BrandShow> queryMyBrandShowByPage(int sellerId,
			Map<String, ?> cond, int... page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<BrandShow> queryWaitAuditBrandShowByPage(Map<String, ?> cond,
			int... page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long passAuditBrandShow(long brandShowId, Date startDate,
			Date endDate, String auditor, String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long rejectAuditBrandShow(long brandShowId, String auditor,
			String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long startBrandShow(long brandShowId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long endBrandSow(long brandShowId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BrandShowDetail> getDetailsOfBrandShow(long brandShowId) {
		// TODO Auto-generated method stub
		return null;
	}
}
