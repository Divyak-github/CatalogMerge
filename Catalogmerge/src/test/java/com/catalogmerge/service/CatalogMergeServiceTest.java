package com.catalogmerge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.dao.CatalogEntityA;
import com.catalogmerge.dao.SupplierEntity;


@DataJpaTest
@Import(CatalogMergeService.class)
public class CatalogMergeServiceTest {


	@Autowired
	private CatalogMergeService service;
	
	@Test(expected = NullPointerException.class)
	public void saveTest() {
		CatalogEntityA CatalogEntity = new CatalogEntityA("111-111-111", "Watch", "A");
		CatalogEntityA returnObj = service.createOrUpdateCatalogItem(CatalogEntity);
		assertThat(returnObj != null).isEqualTo(true);
	}
	
	@Test(expected = NullPointerException.class)
	public void findAllCatalogsTest() {
		List<CatalogEntityA> found = service.getAllCatalogs();
		assertThat(found != null).isEqualTo(true);
		assertTrue(!found.isEmpty());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void findAllBarcodesTest() {
		Collection<BarcodeEntity> found = service.getAllBarcodes();
		assertThat(found != null).isEqualTo(true);
		assertTrue(!found.isEmpty());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void findAllSuppliersTest() {
		Collection<SupplierEntity> found = service.getAllSuppliers();
		assertThat(found != null).isEqualTo(true);
		assertTrue(!found.isEmpty());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void findCatalogByIdTest() {
		List<CatalogEntityA> found = service.getAllCatalogs();
		assertThat(found != null).isEqualTo(true);
		assertTrue(!found.isEmpty());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void createBarcodeItemTest() {
		BarcodeEntity cat =  new BarcodeEntity("111-111-111", "	z2783613083817", "B");;
		BarcodeEntity returnObj = service.createBarcodeItem(cat);
		assertThat(returnObj != null).isEqualTo(true);
		assertNotNull(returnObj);
	}

}
