package com.catalogmerge.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

	@Entity
	@Table(name="TBL_CATALOG_A")
	public class CatalogEntityA {
		
		public CatalogEntityA() {}
		public CatalogEntityA(String skuId, String productDesc, String catalogName) 
		{
			this.skuId = skuId;
			this.productDesc = productDesc;
			this.catalogName = catalogName;
		}
		
		@Id 
		@GeneratedValue (strategy = GenerationType.IDENTITY)
		private Long id;
		@Column(name="sku_id")
		private String skuId;
		@Column(name="product_desc")
		private String productDesc;
		@Column(name="catalog_name")
		private String catalogName;

		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSkuId() {
			return skuId;
		}

		public void setSkuId(String skuId) {
			this.skuId = skuId;
		}
		public String getProductDesc() {
			return productDesc;
		}
		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}
		public String getCatalogName() {
			return catalogName;
		}
		public void setCatalogName(String catalogName) {
			this.catalogName = catalogName;
		}
		
	}
