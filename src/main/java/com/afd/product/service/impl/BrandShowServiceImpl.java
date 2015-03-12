package com.afd.product.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.common.util.DateUtils;
import com.afd.constants.product.ProductConstants;
import com.afd.constants.product.ProductConstants.BrandShow$Status;
import com.afd.constants.product.ProductConstants.BrandShowDetail$Status;
import com.afd.model.order.LogisticsCompany;
import com.afd.model.product.BrandShow;
import com.afd.model.product.BrandShowDetail;
import com.afd.product.dao.BrandShowDetailMapper;
import com.afd.product.dao.BrandShowMapper;
import com.afd.service.order.ILogisticsCompanyService;
import com.afd.service.product.IBrandShowService;
import com.afd.service.schedule.IBrandShowScheduleService;
import com.google.common.collect.Lists;

@Service("brandShowService")
public class BrandShowServiceImpl implements IBrandShowService {
	private static Logger logger = Logger.getLogger(BrandShowServiceImpl.class);

	@Autowired
	private BrandShowMapper brandShowMapper;

	@Autowired
	private BrandShowDetailMapper brandShowDetailMapper;

	@Autowired
	private ILogisticsCompanyService logicticsCompanyService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private IBrandShowScheduleService scheduleService;

	@Override
	public BrandShow getBrandShowById(int brandShowId) {
		return brandShowMapper.selectByPrimaryKey(brandShowId);
	}

	@Override
	public BrandShowDetail getBrandShowDetailById(int brandShowDetailId) {
		BrandShowDetail bsd = this.brandShowDetailMapper
				.selectByPrimaryKey(brandShowDetailId);
		int stock;
		if (null == bsd.getShowBalance() || bsd.getShowBalance() <= 0) {
			bsd.setShowBalance(0);
			stock = 0;
		} else if (null == bsd.getSaleAmount() || bsd.getSaleAmount() <= 0) {
			bsd.setSaleAmount(0);
			stock = bsd.getShowBalance();
		} else {
			stock = bsd.getShowBalance() - bsd.getSaleAmount();
		}
		try {
			String strStock = this.redisTemplate.opsForValue().get(
					ProductConstants.CACHE_PERFIX_INVENTORY + bsd.getbSDId());
			if (StringUtils.isEmpty(strStock) || "null".equals(strStock)) {
				strStock = stock + "";
				this.redisTemplate.opsForValue().set(
						ProductConstants.CACHE_PERFIX_INVENTORY
								+ bsd.getbSDId(), strStock);
			} else {
				stock = Integer.parseInt(strStock);
				int saleAmount = bsd.getShowBalance() - stock;
				if (saleAmount >= 0) {
					bsd.setSaleAmount(saleAmount);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bsd;
	}

	@Override
	public List<BrandShow> getBrandShowByIds(List<Integer> brandShowIds) {
		return brandShowMapper.getBrandShowByIds(brandShowIds);
	}

	@Override
	public List<BrandShowDetail> getBrandShowDetailsByIds(
			List<Integer> brandShowDetailIds) {
		List<BrandShowDetail> bsds = brandShowDetailMapper
				.getBrandShowDetailsByIds(brandShowDetailIds);
		if (null != bsds && bsds.size() != 0) {
			for (BrandShowDetail bsd : bsds) {
				int stock;
				if (null == bsd.getShowBalance() || bsd.getShowBalance() <= 0) {
					bsd.setShowBalance(0);
					stock = 0;
				} else if (null == bsd.getSaleAmount()
						|| bsd.getSaleAmount() <= 0) {
					bsd.setSaleAmount(0);
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
						stock = Integer.parseInt(strStock);
						int saleAmount = bsd.getShowBalance() - stock;
						if (saleAmount >= 0) {
							bsd.setSaleAmount(saleAmount);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return bsds;
	}

	@Override
	public List<BrandShow> getValidBrandShows(BrandShow record) {
		record.setStatus(BrandShow$Status.ONLINE);
		return this.brandShowMapper.getValidBrandShows(record);
	}

	@Override
	public BigDecimal getLowestPrice(int bsid) {
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
	@Override
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
	public int newBrandShow(BrandShow brandShow) {
		brandShow.setStatus(BrandShow$Status.EDITING);
		brandShow.setCreateByDate(DateUtils.currentDate());

		int result = brandShowMapper.insert(brandShow);

		return result > 0 ? brandShow.getBrandShowId() : 0;
	}

	@Override
	public int modifyBrandShow(BrandShow brandShow) {
		brandShow.setStatus(BrandShow$Status.EDITING);
		return brandShowMapper.updateByPrimaryKeySelective(brandShow);
	}

	@Override
	public int submitBrandShow(int brandShowId, BrandShowDetail[] details) {
		int result = brandShowMapper.updateStatus(brandShowId,
				BrandShow$Status.WAIT_AUDIT);

		if (result > 0) {
			BrandShow brandShow = brandShowMapper
					.selectByPrimaryKey(brandShowId);
			List<BrandShowDetail> oldBrandShowDetailList = this
					.getDetailsOfBrandShow(brandShowId);

			for (BrandShowDetail oldBrandShow : oldBrandShowDetailList) {
				boolean found = false;

				for (BrandShowDetail detail : details) {
					if (oldBrandShow.getbSDId().equals(detail.getbSDId())) {
						found = true;
						break;
					}
				}

				if (!found) {
					brandShowDetailMapper.updateStatus(oldBrandShow.getbSDId(),
							BrandShowDetail$Status.REMOVED);
				}
			}

			for (BrandShowDetail detail : details) {
				detail.setBrandName(brandShow.getBrandName());

				if (detail.getbSDId() != null && detail.getbSDId() != 0) {
					brandShowDetailMapper.updateByPrimaryKeySelective(detail);

				} else {
					detail.setStatus(BrandShowDetail$Status.VALID);
					brandShowDetailMapper.insert(detail);
				}
			}
		}

		return result;
	}

	@Override
	public Page<BrandShow> queryMyBrandShowByPage(int sellerId,
			Map<String, ?> cond, int... page) {

		Page<BrandShow> brandShowPage = new Page<BrandShow>();

		if (page != null) {
			if (page.length > 0) {
				brandShowPage.setCurrentPageNo(page[0]);
			}

			if (page.length > 1) {
				brandShowPage.setPageSize(page[1]);
			}
		}

		brandShowPage.setResult(brandShowMapper.queryBrandShowOfSellerByPage(
				sellerId, cond, brandShowPage));

		return brandShowPage;
	}

	@Override
	public Page<BrandShow> queryWaitAuditBrandShowByPage(Map<String, ?> cond,
			int... page) {

		Page<BrandShow> brandShowPage = new Page<BrandShow>();

		if (page != null) {
			if (page.length > 0) {
				brandShowPage.setCurrentPageNo(page[0]);
			}

			if (page.length > 1) {
				brandShowPage.setPageSize(page[1]);
			}
		}

		brandShowPage.setResult(brandShowMapper.queryWaitAuditBrandShowByPage(
				cond, brandShowPage));

		return brandShowPage;
	}

	@Override
	public int passAuditBrandShow(int brandShowId, Date startDate,
			Date endDate, String auditor, String opinion) {

		BrandShow brandShow = this.getBrandShowById(brandShowId);
		if (brandShow == null)
			return 0;

		if (!BrandShow$Status.WAIT_AUDIT.equals(brandShow.getStatus())
				&& !BrandShow$Status.AUDITING.equals(brandShow.getStatus())) {
			return -1;
		}

		BrandShow instance = new BrandShow();

		instance.setBrandShowId(brandShowId);
		instance.setStartDate(startDate);
		instance.setEndDate(endDate);
		instance.setAuditByName(auditor);
		instance.setAuditContent(opinion);
		instance.setAuditDate(DateUtils.currentDate());
		instance.setStatus(BrandShow$Status.WAIT_ONLINE);

		int result = brandShowMapper.updateByPrimaryKeySelective(instance);

		if (result > 0) {
			scheduleService.registerBrandShowStartJob(brandShowId, startDate);
			scheduleService.registerBrandShowEndJob(brandShowId, endDate);
		}

		return result;
	}

	@Override
	public int rejectAuditBrandShow(int brandShowId, String auditor,
			String opinion) {

		BrandShow brandShow = this.getBrandShowById(brandShowId);
		if (brandShow == null)
			return 0;

		if (!BrandShow$Status.WAIT_AUDIT.equals(brandShow.getStatus())
				&& !BrandShow$Status.AUDITING.equals(brandShow.getStatus())) {
			return -1;
		}

		BrandShow instance = new BrandShow();

		instance.setBrandShowId(brandShowId);
		instance.setAuditByName(auditor);
		instance.setAuditContent(opinion);
		instance.setAuditDate(DateUtils.currentDate());
		instance.setStatus(BrandShow$Status.REJECTED);

		return brandShowMapper.updateByPrimaryKeySelective(instance);
	}

	@Override
	public int startBrandShow(int brandShowId) {
		BrandShow brandShow = this.getBrandShowById(brandShowId);
		if (brandShow == null)
			return 0;

		if (!BrandShow$Status.WAIT_ONLINE.equals(brandShow.getStatus())) {
			return -1;
		}

		return brandShowMapper.updateStatus(brandShowId,
				BrandShow$Status.ONLINE);
	}

	@Override
	public int endBrandSow(int brandShowId) {
		BrandShow brandShow = this.getBrandShowById(brandShowId);
		if (brandShow == null)
			return 0;

		if (!BrandShow$Status.ONLINE.equals(brandShow.getStatus())) {
			return -1;
		}

		return brandShowMapper.updateStatus(brandShowId,
				BrandShow$Status.FINISHED);
	}

	@Override
	public List<BrandShowDetail> getDetailsOfBrandShow(int brandShowId) {
		return brandShowDetailMapper.getValidDetailsOfBrandShow(brandShowId);
	}

	@Override
	public List<BrandShow> getOnlinedBrandShowsOfSeller(int sellerId) {
		return brandShowMapper.getOnlinedBrandShowsOfSeller(sellerId);
	}

	@Override
	public List<LogisticsCompany> getLogisticsCompanyListOfBrandShow(
			int brandShowId) {
		BrandShow brandShow = this.getBrandShowById(brandShowId);

		if (brandShow == null
				|| StringUtils.isEmpty(brandShow.getLogisticsCompIds()))
			return null;

		String[] arr = brandShow.getLogisticsCompIds().split("\\,");
		List<Long> list = Lists.newArrayList();

		for (String id : arr) {
			if (StringUtils.isNotEmpty(id)) {
				list.add(Long.parseLong(id));
			}
		}

		return logicticsCompanyService.getLogisticsCompanyByIds(list);
	}

	@Override
	public Page<BrandShow> queryBrandShowByPage(Map<String, ?> cond,
			int... page) {
		Page<BrandShow> brandShowPage = new Page<BrandShow>();

		if (page != null) {
			if (page.length > 0) {
				brandShowPage.setCurrentPageNo(page[0]);
			}

			if (page.length > 1) {
				brandShowPage.setPageSize(page[1]);
			}
		}

		brandShowPage.setResult(brandShowMapper.queryBrandShowByPage(cond,
				brandShowPage));

		return brandShowPage;
	}

	@Override
	public BrandShowDetail getBrandShowDetailBySkuId(Integer brandShowId,
			Integer skuId) {
		BrandShowDetail ret = this.brandShowDetailMapper
				.getBrandShowDetailBySkuId(brandShowId, skuId);
		return ret;
	}

	@Override
	public int getBrandShowCountOfSellerByStatus(int sellerId, String status) {
		return brandShowMapper.getBrandShowCountOfSellerByStatus(sellerId,
				status);
	}
	
	@Override
	public List<BrandShowDetail> getBrandShowDetailsByProdId(@Param("brandShowId")Integer brandShowId,
			@Param("prodId")Integer prodId){
		return this.brandShowDetailMapper.getBrandShowDetailsByProdId(brandShowId, prodId);
	}
}
