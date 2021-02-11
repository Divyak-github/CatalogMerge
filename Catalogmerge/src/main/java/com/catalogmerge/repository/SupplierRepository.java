package com.catalogmerge.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.catalogmerge.dao.SupplierEntity;

@Repository
@Transactional
@Component("supplierRepo")
public interface SupplierRepository extends CrudRepository<SupplierEntity, Long> {
	public SupplierEntity findBySupplierId(int supplierId);
	public SupplierEntity findBySupplierName(String supplierName);
	public SupplierEntity findBySupplierCompany(String supplierCompany);
}