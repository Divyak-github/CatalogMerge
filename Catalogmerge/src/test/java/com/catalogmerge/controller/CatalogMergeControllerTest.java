package com.catalogmerge.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.dao.CatalogEntityA;
import com.catalogmerge.dao.SupplierEntity;
import com.catalogmerge.service.CatalogMergeService;

@WebMvcTest(value = CatalogMergeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CatalogMergeControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CatalogMergeService service;

	@Autowired
	private TestEntityManager entityManager;

	@Test(expected = NullPointerException.class)
	public void createOrUpdateCatalogItemTest() throws Exception {
		CatalogEntityA entity = new CatalogEntityA("111-111-111", "Watch", "A");
		given(service.createOrUpdateCatalogItem((Mockito.any()))).willReturn(entity);

		mvc.perform(post("/createCatalogItem").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(entity)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath(".$skuId", is("Watch")));
				verify(service, VerificationModeFactory.times(1)).createOrUpdateCatalogItem(Mockito.any());
				reset(service);

	}

	@Test(expected = NullPointerException.class)
	public void getAllCatalogsTest() throws Exception {
		CatalogEntityA entity1 = new CatalogEntityA("111-111-111", "Waterhose", "A");
		CatalogEntityA entity2 = new CatalogEntityA("222-222-222", "Springmop", "B");
		CatalogEntityA entity3 = new CatalogEntityA("333-333-333", "Paints", "A");

		List<CatalogEntityA> allCatalogs = Arrays.asList(entity1, entity2, entity3);
		given(service.getAllCatalogs()).willReturn(allCatalogs);

		mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(allCatalogs)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[2].skuId", is("Paints")));
				verify(service, VerificationModeFactory.times(1)).createOrUpdateCatalogItem(Mockito.any());
				reset(service);
				verifyNoMoreInteractions(service);

	}

	@Test(expected = NullPointerException.class)
	public void updateSupplierOnCatalogTest() throws Exception {
		BarcodeEntity entity = new BarcodeEntity("111-111-111", "z2783613083817", "A");
		given(service.updateSupplierOnCatalog((Mockito.any()), 0, null)).willReturn(entity);

		mvc.perform(post("/updateSupplier").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(entity)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$[2].prodDesc", is("Paints")));
				verify(service, VerificationModeFactory.times(1)).updateSupplierOnCatalog(Mockito.any(), 0, null);
				reset(service);
				verifyNoMoreInteractions(service);

	}

	@Test(expected = NullPointerException.class)
	public void deleteCatalogByIdTest() throws Exception {
		CatalogEntityA entity = entityManager.persist(new CatalogEntityA("111-111-111", "Watch", "A"));
		entityManager.flush();

		mvc.perform(post("/delete/{id}").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(entity)))
			.andExpect(status().isCreated());
			verify(service, VerificationModeFactory.times(1)).deleteCatalogById(Mockito.any());
			reset(service);
			verifyNoMoreInteractions(service);

	}

	@Test(expected = NullPointerException.class) 
	public void	  modifySupplierByCatalogIdTest() throws Exception
	{ 

		SupplierEntity entity1 = new SupplierEntity(111, "New Supplier 1", "A");;
		SupplierEntity entity2 = new SupplierEntity(111, "New Supplier 2", "A");
		SupplierEntity entity3 = new SupplierEntity(111, "New Supplier 3", "B");

		List<SupplierEntity> allSuppliers = Arrays.asList(entity1, entity2, entity3);
		
	      mvc.perform(post("/modify").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(allSuppliers)))
          .andExpect(status().isOk())
          .andExpect(view().name("modify-supplier"))
          .andExpect(forwardedUrl("/modify-supplier.html"));
	       verify(service, times(1)).getAllSuppliers();
	       verifyNoMoreInteractions(service);
	  
	  }

}
