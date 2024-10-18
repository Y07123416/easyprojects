package com.easyjava.service.impl;

import com.easyjava.entity.query.SimplePage;
import com.easyjava.enums.PageSize;
import com.easyjava.service.ProductInfoService;
import com.easyjava.entity.vo.PaginationResultVO;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.mappers.ProductInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author:花海
 * @description:ProductInfoServiceImpl
 * @date:2024/10/18
*/
	@Service("productInfoService")
	public class ProductInfoServiceImpl implements ProductInfoService{

	@Resource
	private ProductInfoMapper<ProductInfo,ProductInfoQuery> productInfoMapper;
	/**
	 * 根据条件查询列表
	*/
	@Override
	public List<ProductInfo> findListByParam(ProductInfoQuery query){
		return this.productInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	*/
	@Override
	public Integer findCountByParam(ProductInfoQuery query){
		return this.productInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	*/
	@Override
	public PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query){
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<ProductInfo> list = this.findListByParam(query);
		return new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
	}

	/**
	 * 新增
	*/
	@Override
	public Integer add(ProductInfo bean){
		return this.productInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	*/
	@Override
	public Integer addBatch(List<ProductInfo> listBean){
		if(listBean == null || listBean.isEmpty()) {
			return 0;
}
		return this.productInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	*/
	@Override
	public Integer addBatchOrUpdateBatch(List<ProductInfo> listBean){
		if(listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.productInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据Id查询
	*/
	@Override
	public ProductInfo getProductInfoById(Integer id){
		return this.productInfoMapper.selectById(id);
	}

	/**
	 * 根据Id更新
	*/
	@Override
	public Integer updateProductInfoById(ProductInfo bean, Integer id){
		return this.productInfoMapper.updateById(bean, id);
	}

	/**
	 * 根据Id删除
	*/
	@Override
	public Integer deleteProductInfoById(Integer id){
		return this.productInfoMapper.deleteById(id);
	}

}
