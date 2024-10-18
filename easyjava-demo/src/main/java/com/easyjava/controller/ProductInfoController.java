package com.easyjava.controller;

import com.easyjava.entity.vo.ResponseVO;
import com.easyjava.service.ProductInfoService;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author:花海
 * @description:ProductInfoController
 * @date:2024/10/18
*/
	@RestController
	@RequestMapping("productInfo")
	public class ProductInfoController extends ABaseController{

	@Resource
	private ProductInfoService productInfoService;
	/**
	 * 查询列表
	*/
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query) {
		return getSuccessResponseVO(productInfoService.findListByPage(query));
	}

	/**
	 * 新增
	*/
	@RequestMapping("add")
	public ResponseVO add(ProductInfo bean){
		this.productInfoService.add(bean);
		return getSuccessResponseVO( null );
	}

	/**
	 * 批量新增
	*/
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<ProductInfo> listBean){
		this.productInfoService.addBatch(listBean);
		return getSuccessResponseVO( null );
	}

	/**
	 * 批量新增或修改
	*/
	@RequestMapping("addBatchOrUpdate")
	public ResponseVO addBatchOrUpdate(@RequestBody List<ProductInfo> listBean){
		this.productInfoService.addBatchOrUpdateBatch(listBean);
		return getSuccessResponseVO( null );
	}

	/**
	 * 根据Id查询
	*/
	@RequestMapping("getProductInfoById")
	public ResponseVO getProductInfoById(Integer id){
		return getSuccessResponseVO(this.productInfoService.getProductInfoById(id));
	}

	/**
	 * 根据Id更新
	*/
	@RequestMapping("updateProductInfoById")
	public ResponseVO updateProductInfoById(ProductInfo bean, Integer id){
		this.productInfoService.updateProductInfoById(bean, id);
		return getSuccessResponseVO( null );
	}

	/**
	 * 根据Id删除
	*/
	@RequestMapping("deleteProductInfoById")
	public ResponseVO deleteProductInfoById(Integer id){
		this.productInfoService.deleteProductInfoById(id);
		return getSuccessResponseVO( null );
	}

}
