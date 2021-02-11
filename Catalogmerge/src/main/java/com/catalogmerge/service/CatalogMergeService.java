package com.catalogmerge.service;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.dao.CatalogEntityA;
import com.catalogmerge.dao.SupplierEntity;
import com.catalogmerge.exception.RecordNotFoundException;
import com.opencsv.exceptions.CsvException;

public interface CatalogMergeService {

	List<CatalogEntityA> getAllCatalogs();

	CatalogEntityA getCatalogById(Long long1) throws RecordNotFoundException;

	void loadData();

	void deleteCatalogById(Long id) throws RecordNotFoundException;

	Collection<BarcodeEntity> getAllBarcodes();

	BarcodeEntity getBarcodeById(Long id) throws RecordNotFoundException;

	List<SupplierEntity> getAllSuppliers();

	void createSupplierItem(SupplierEntity newSupplier);

	BarcodeEntity createBarcodeItem(BarcodeEntity tempObj);

	List<CatalogEntityA> loadCatalogs(String path) throws FileNotFoundException, CsvException;

	void loadSuppliers(String path) throws FileNotFoundException, CsvException;

	void loadBarcodes(String path) throws FileNotFoundException, CsvException;

	CatalogEntityA createOrUpdateCatalogItem(CatalogEntityA entity);

	BarcodeEntity associateSupplierAndBarcodes(CatalogEntityA catItem, int supId, String supName, String supCompany) throws RecordNotFoundException;
	
	BarcodeEntity updateSupplierOnCatalog(BarcodeEntity barCodeItem, int supId, String supName) throws RecordNotFoundException;
	
	String modifySupplierByCatalogId(Model model, Optional<Long> id) throws RecordNotFoundException;
	
	List<CatalogEntityA> mergeByApplyingRules();



}
