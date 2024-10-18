package com.easyjava.entity.query;

import java.util.Date;
/**
 * @author:花海
 * @description:商品信息查询对象
 * @date:2024/10/18
*/
public class ProductInfoQuery extends BaseQuery {
	/**
	 * 自增ID
	*/
	private Integer id;
	/**
	 * 商品名称
	*/
	private String productName;
	private String productNameFuzzy;

	/**
	 * 颜色类型
	*/
	private Integer colorType;
	/**
	 * 创建时间
	*/
	private Date createtime;
	private String createtimeStart;
	private String createtimeEnd;

	public void setId(Integer id) {
		this.id= id;
	}
	public Integer getId() {
		return id;
	}
	public void setProductName(String productName) {
		this.productName= productName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductNameFuzzy(String productNameFuzzy) {
		this.productNameFuzzy= productNameFuzzy;
	}
	public String getProductNameFuzzy() {
		return productNameFuzzy;
	}
	public void setColorType(Integer colorType) {
		this.colorType= colorType;
	}
	public Integer getColorType() {
		return colorType;
	}
	public void setCreatetime(Date createtime) {
		this.createtime= createtime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetimeStart(String createtimeStart) {
		this.createtimeStart= createtimeStart;
	}
	public String getCreatetimeStart() {
		return createtimeStart;
	}
	public void setCreatetimeEnd(String createtimeEnd) {
		this.createtimeEnd= createtimeEnd;
	}
	public String getCreatetimeEnd() {
		return createtimeEnd;
	}
}