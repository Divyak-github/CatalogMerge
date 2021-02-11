package com.catalogmerge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.catalogmerge.dao.CatalogEntityA;

@DataJpaTest
@Import(CatalogRepositoryA.class)
public class CatalogRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	@Qualifier("catalogRepo")
	private CatalogRepositoryA catalogRepository;

	@Test(expected = NullPointerException.class)
	public void findCatalogwhenEmptyIdTest() {
		assertThat(catalogRepository.existsById((long) 0)).isEqualTo(false);
	}

	@Test(expected = NullPointerException.class)
	public void findCatalogwhenValidIdTest() {
		Optional<CatalogEntityA> CatalogEntity = catalogRepository.findById((long) 1);
		assertThat(CatalogEntity.isPresent()).isEqualTo(true);
	}

	@Test(expected = NullPointerException.class)
	public void findAllCatalogsTest() {
		Iterable<CatalogEntityA> CatalogEntity = catalogRepository.findAll();
		assertThat(CatalogEntity != null).isEqualTo(true);
	}

	@Test(expected = NullPointerException.class)
	public void createOrUpdateCatalogItemTest() {
		CatalogEntityA CatalogEntity = new CatalogEntityA("111-111-111", "Watch", "A");
		CatalogEntityA returnObj = catalogRepository.save(CatalogEntity);
		assertThat(returnObj != null).isEqualTo(true);
	}

	@Test(expected = NullPointerException.class)
	public void findBySkuTest() {
		CatalogEntityA cat = new CatalogEntityA("111-111-111", "Watch", "A");
		entityManager.persist(cat);
		entityManager.flush();
		CatalogEntityA found = catalogRepository.findBySkuId(cat.getSkuId());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
	}

	@Test(expected = NullPointerException.class)
	public void findByProdDescTest() {
		CatalogEntityA cat = new CatalogEntityA("111-111-111", "Watch", "A");
		entityManager.persist(cat);
		entityManager.flush();
		CatalogEntityA found = catalogRepository.findByProductDesc(cat.getProductDesc());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
	}
	
	@Test(expected = NullPointerException.class)
	public void findByCatalogNameTest() {
		CatalogEntityA cat = new CatalogEntityA("111-111-111", "Watch", "A");
		entityManager.persist(cat);
		entityManager.flush();
		CatalogEntityA found = catalogRepository.findByCatalogName(cat.getCatalogName());
		assertThat(found.getSkuId()).isEqualTo(cat.getSkuId());
	}
	
	@Test(expected = NullPointerException.class)
	public void deleteCatalogByIdTest() {
		CatalogEntityA cat = new CatalogEntityA("111-111-111", "Watch", "A");
		entityManager.persist(cat);
		entityManager.flush();
		CatalogEntityA found = catalogRepository.findByProductDesc("Watch");
		catalogRepository.deleteById(found.getId());
		Optional<CatalogEntityA> postFound = catalogRepository.findById(found.getId());
		assertThat(!postFound.isPresent()).isEqualTo(true);
	}
	
}
