package com.catalogmerge.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.catalogmerge.dao.BarcodeEntity;

@Repository
@Transactional
@Component("barcodeRepo")
public interface BarcodeRepository extends JpaRepository<BarcodeEntity, Long> 
{
	public BarcodeEntity findBySkuId(String skuId);
	public BarcodeEntity findByBarCode(String barCode);
	public BarcodeEntity findByCompName(String compName);
	
}