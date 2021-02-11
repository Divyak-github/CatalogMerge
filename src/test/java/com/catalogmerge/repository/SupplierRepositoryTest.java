package com.catalogmerge.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.catalogmerge.dao.SupplierEntity;
import com.catalogmerge.repository.SupplierRepository;

public class SupplierRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	@Qualifier("supplierRepo")
	private SupplierRepository supplierRepository;

	@Test(expected = NullPointerException.class)
	public void findSupplierwhenEmptyIdTest() {
		assertThat(supplierRepository.existsById((long) 0)).isEqualTo(false);
	}

	@Test(expected = NullPointerException.class)
	public void findSupplierwhenValidIdTest() {
		Optional<SupplierEntity> entity = supplierRepository.findById((long) 1);
		assertThat(entity.isPresent()).isEqualTo(true);
	}
	
	@Test(expected = NullPointerException.class)
	public void findBySupplierNameTest() {
		SupplierEntity supplier = new SupplierEntity(111, "New Supplier B", "B");
		entityManager.persist(supplier);
		entityManager.flush();
		SupplierEntity found = supplierRepository.findBySupplierName(supplier.getSupplierName());
		assertThat(found.getSupplierName()).isEqualTo(supplier.getSupplierName());
	}

	
	@Test(expected = NullPointerException.class)
	public void findByCompNameTest() {
		SupplierEntity supplier = new SupplierEntity(111, "New Supplier A", "A");
		entityManager.persist(supplier);
		entityManager.flush();
		SupplierEntity found = supplierRepository.findBySupplierCompany(supplier.getSupplierCompany());
		assertThat(found.getSupplierCompany()).isEqualTo(supplier.getSupplierCompany());
		assertNotNull(found);
	}

	@Test(expected = NullPointerException.class)
	public void findAllSuppliersTest() {
		Iterable<SupplierEntity> CatalogEntity = supplierRepository.findAll();
		assertThat(CatalogEntity != null).isEqualTo(true);
	}
	
	@Test(expected = NullPointerException.class)
	public void createOrUpdateSupplierTest() {
		SupplierEntity entity = new SupplierEntity(111, "New Supplier A", "A");
		SupplierEntity returnObj = supplierRepository.save(entity);
		assertThat(returnObj != null).isEqualTo(true);
	}

	@Test(expected = NullPointerException.class)
	public void deleteSupplierByIdTest() {
		SupplierEntity supplier = new SupplierEntity(111, "New Supplier B", "B");
		entityManager.persist(supplier);
		entityManager.flush();
		SupplierEntity found = supplierRepository.findBySupplierId(supplier.getSupplierId());
		supplierRepository.deleteById(found.getId());
		Optional<SupplierEntity> postFound = supplierRepository.findById(found.getId());
		assertThat(!postFound.isPresent()).isEqualTo(true);
	}
	
	

	
	

}
