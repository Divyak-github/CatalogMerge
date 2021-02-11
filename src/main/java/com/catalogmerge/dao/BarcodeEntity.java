package com.catalogmerge.dao;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Lazy;

/**
 * @author divya
 */

@Entity
@Table(name="TBL_SUP_CAT_BARCODES")
public class BarcodeEntity {
	public BarcodeEntity() {}
	public BarcodeEntity(String skuId, String barCode, String compName) 
	{
		
		 
		  this.skuId = skuId;
		  this.barCode = barCode;
		  this.compName = compName;
	}
	
	@Id 
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="sku_id")
	private String skuId;
	@Column(name="bar_code")
	private String barCode;
	@Column(name="comp_name")
	private String compName;
	
	@ManyToOne(cascade = {CascadeType.ALL}) 
	private CatalogEntityA catalog;
	
	@OneToOne() 
	@Lazy
	private SupplierEntity supplier;

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
	 
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public CatalogEntityA getCatalog() {
		return catalog;
	}
	public void setCatalog(CatalogEntityA catalog) {
		this.catalog = catalog;
	}
	public SupplierEntity getSupplier() {
		return supplier;
	}
	public void setSupplier(SupplierEntity supplier) {
		this.supplier = supplier;
	}
		 
}
