package com.catalogmerge.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_SUPPLIER")
public class SupplierEntity {

	public SupplierEntity() {}
	public SupplierEntity(int supplierId, String supplierName, String supplierCompany) 
	{
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.supplierCompany = supplierCompany;
	}
	
	@Id 
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="supplier_id")
	private int supplierId;
	
	@Column(name="supplier_name")
	private String supplierName;
	
	@Column(name="supplier_cmpny")
	private String supplierCompany;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCompany() {
		return supplierCompany;
	}
	public void setSupplierCompany(String supplierCompany) {
		this.supplierCompany = supplierCompany;
	}
	
}
