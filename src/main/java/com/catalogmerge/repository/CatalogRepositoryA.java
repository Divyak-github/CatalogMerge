package com.catalogmerge.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.catalogmerge.dao.CatalogEntityA;

@Repository
@Transactional
@Component("catalogRepo")
public interface CatalogRepositoryA extends JpaRepository<CatalogEntityA, Long> {

	public CatalogEntityA findBySkuId(String skuId);
	public List<CatalogEntityA> findAll();
	public CatalogEntityA findByProductDesc(String productDesc);
	public CatalogEntityA findByCatalogName(String catalogName);

}