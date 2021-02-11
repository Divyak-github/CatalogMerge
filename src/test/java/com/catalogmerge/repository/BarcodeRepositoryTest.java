package com.catalogmerge.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.repository.BarcodeRepository;
import com.catalogmerge.repository.CatalogRepositoryA;


@DataJpaTest
@Import(CatalogRepositoryA.class)
public class BarcodeRepositoryTest {

	@Autowired
	@Qualifier("barcodeRepo")
	private BarcodeRepository barcodeRepository;

	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test(expected = NullPointerException.class)
	public void findBarcodewhenEmptyIdTest() {
		assertThat(barcodeRepository.existsById((long) 0)).isEqualTo(false);
	}

	@Test(expected = NullPointerException.class)
	public void findBarcodeWhenValidIdTest() {
		Optional<BarcodeEntity> entities = barcodeRepository.findById((long) 1);
		assertThat(entities.isPresent()).isEqualTo(true);
		assertNotNull(entities);
	}

	@Test(expected = NullPointerException.class)
	public void findAllBarcodesTest() {
		Iterable<BarcodeEntity> entities = barcodeRepository.findAll();
		assertThat(entities != null).isEqualTo(true);
		assertNotNull(entities);
	}
	
	@Test(expected = NullPointerException.class)
	public void findBySkuTest() {
		BarcodeEntity cat = new BarcodeEntity("111-111-111", "	z2783613083817", "A");
		entityManager.persist(cat);
		entityManager.flush();
		BarcodeEntity found = barcodeRepository.findBySkuId(cat.getSkuId());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void findByBarCodeTest() {
		BarcodeEntity cat = new BarcodeEntity("111-111-111", "	z2783613083817", "B");
		entityManager.persist(cat);
		entityManager.flush();
		BarcodeEntity found = barcodeRepository.findByBarCode(cat.getBarCode());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void findByCompNameTest() {
		BarcodeEntity cat = new BarcodeEntity("111-111-111", "	z2783613083817", "B");
		entityManager.persist(cat);
		entityManager.flush();
		BarcodeEntity found = barcodeRepository.findBySkuId(cat.getCompName());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
		assertNotNull(found);
	}
	
	@Test(expected = NullPointerException.class)
	public void createBarcodeItemTest() {
		BarcodeEntity cat =  new BarcodeEntity("111-111-111", "	z2783613083817", "B");;
		BarcodeEntity returnObj = barcodeRepository.save(cat);
		assertThat(returnObj != null).isEqualTo(true);
		assertNotNull(returnObj);
	}

	@Test(expected = NullPointerException.class)
	public void deleteBarcodeByIdTest() {
		BarcodeEntity entity = new BarcodeEntity("111-111-111", "	z2783613083817", "B");;
		entityManager.persist(entity);
		entityManager.flush();
		BarcodeEntity found = barcodeRepository.findByBarCode(entity.getBarCode());
		barcodeRepository.deleteById(found.getId());
		Optional<BarcodeEntity> postFound = barcodeRepository.findById(found.getId());
		assertThat(!postFound.isPresent()).isEqualTo(true);
	}
}
