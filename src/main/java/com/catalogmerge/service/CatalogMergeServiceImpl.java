package com.catalogmerge.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.dao.CatalogEntityA;
import com.catalogmerge.dao.SupplierEntity;
import com.catalogmerge.exception.RecordNotFoundException;
import com.catalogmerge.repository.BarcodeRepository;
import com.catalogmerge.repository.CatalogRepositoryA;
import com.catalogmerge.repository.SupplierRepository;
import com.catalogmerge.util.RandomIndexGenerator;
import com.catalogmerge.util.WriteToCsv;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
@Transactional
public class CatalogMergeServiceImpl implements CatalogMergeService{

	@Autowired
	CatalogRepositoryA catalogRepositoryA;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	BarcodeRepository scbRepository;
	
	@Value("${catA.path}") String catA;	@Value("${catB.path}") String catB;	@Value("${supA.path}") String supA;
	@Value("${supB.path}") String supB;	@Value("${barcA.path}") String barcA;	@Value("${barcB.path}") String barcB;
	@Value("${catalogItem}") String catalogItem; @Value("${modify}") String modify;
	@Value("${output.path}") String outputPath;
	
	String curr_directory = System.getProperty("user.dir");
	/**
	 * @return
	 */
	@Override
	public List<CatalogEntityA> getAllCatalogs() {
		List<CatalogEntityA> result = (List<CatalogEntityA>) catalogRepositoryA.findAll();
		return result;

	}

	/**
	 * @return
	 */
	@Override
	public List<SupplierEntity> getAllSuppliers() {
		List<SupplierEntity> result = (List<SupplierEntity>) supplierRepository.findAll();
		return result;

	}
	
	/**
	 * @return
	 */
	@Override
	public List<BarcodeEntity> getAllBarcodes() {
		List<BarcodeEntity> result = (List<BarcodeEntity>) scbRepository.findAll();
		return result;

	}
	/**
	 * @param id
	 * @return
	 * @throws RecordNotFoundException
	 */
	@Override
	public CatalogEntityA getCatalogById(Long id) throws RecordNotFoundException {
		Optional<CatalogEntityA> catalogItem = catalogRepositoryA.findById(id);
		if (catalogItem.isPresent()) {
			return catalogItem.get();
		} else {
			throw new RecordNotFoundException("No Catalog item exist for given id");
		}
	}
	
	@Override
	public BarcodeEntity getBarcodeById(Long id) throws RecordNotFoundException {
		Optional<BarcodeEntity> catalogItem = scbRepository.findById(id);
		if (catalogItem.isPresent()) {
			return catalogItem.get();
		} else {
			throw new RecordNotFoundException("No Catalog item exist for given id");
		}
	}

	private CatalogEntityA getCatalogByNameSkuPd(String skuId, String productDesc, String catalogName) {

		List<CatalogEntityA> result = (List<CatalogEntityA>) catalogRepositoryA.findAll();
		CatalogEntityA finalResult = result.stream().filter(x -> (catalogName.equals(x.getCatalogName())
				&& skuId.equals(x.getSkuId()) && productDesc.equals(x.getProductDesc()))).findAny().orElse(null);

		if (finalResult != null) {
			return finalResult;
		} else {
			return new CatalogEntityA(skuId, productDesc, catalogName);
		}
	}

	/**
	 * @param id
	 * @throws RecordNotFoundException
	 */
    @Override
	public void deleteCatalogById(Long id) throws RecordNotFoundException {
		Optional<CatalogEntityA> catalogItem = catalogRepositoryA.findById(id);
		if (catalogItem.isPresent()) 
		{
			//
			scbRepository.findAll().forEach(x-> 
			{
				if(x.getCatalog().equals(catalogItem.get())) 
				{
					scbRepository.delete(x);
				}
					
			});
		} else {
			throw new RecordNotFoundException("No catalogItem record exist for given id");
		}
	}
	

	public void loadData() {
		try {
			
			loadCatalogs(catA);
			loadCatalogs(catB);
			loadSuppliers(supA);
			loadSuppliers(supB);
			loadBarcodes(barcA);
			loadBarcodes(barcB);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}

	}

	public List<CatalogEntityA> loadCatalogs(String path) throws FileNotFoundException, CsvException {

		String fileLoc = curr_directory + path;
		try (CSVReader reader = new CSVReader(new FileReader(fileLoc))) {
			reader.skip(1);
			List<String[]> r = reader.readAll();
			r.forEach(x -> {
				String[] y = Arrays.toString(x).split(",");
				int openIndex = y[0].indexOf("]");
				int closeIndex = y[1].indexOf("]");
				createOrUpdateCatalogItem(new CatalogEntityA(
						(y[0].toString().substring(openIndex + 2, y[0].length())).trim(),
						(y[1].toString().substring(0, closeIndex)).trim(), (path.contains("A.csv") ? "A" : "B")));
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getAllCatalogs();

	}


	public void loadSuppliers(String path) throws FileNotFoundException, CsvException {
		String fileLoc = curr_directory + path;
		try (CSVReader reader = new CSVReader(new FileReader(fileLoc))) {
			reader.skip(1);
			List<String[]> r = reader.readAll();
			r.forEach(x -> {
				String[] y = Arrays.toString(x).split(",");
				int openIndex = y[0].indexOf("]");
				int closeIndex = y[1].indexOf("]");
				String supId = y[0].toString().substring(openIndex + 2, y[0].length()).trim();

				if (supId != null)
					createSupplierItem(new SupplierEntity(Integer.parseInt(supId),
							y[1].toString().substring(0, closeIndex).trim(), (path.contains("A.csv") ? "A" : "B")));

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void loadBarcodes(String path) throws FileNotFoundException, CsvException {
		String fileLoc = curr_directory + path;
		List<CatalogEntityA> catalogList = getAllCatalogs();
		List<SupplierEntity> supplierList = (List<SupplierEntity>) supplierRepository.findAll();
		String compName = path.contains("A.csv") ? "A" : "B";
		try (CSVReader reader = new CSVReader(new FileReader(fileLoc))) {
			reader.skip(1);
			List<String[]> r = reader.readAll();
			r.forEach(x -> {
				String[] y = Arrays.toString(x).split(",");
				int openIndex = y[0].indexOf("[");
				int closeIndex = y[2].indexOf("]");
				String supId = y[0].toString().substring(openIndex + 2, y[0].length()).trim();
				String skuId = y[1].toString().trim();
				BarcodeEntity input = new BarcodeEntity(skuId,
						y[2].toString().substring(0, closeIndex).trim(), compName);
				input.setCatalog(findCatalogBySkuAndCompName(catalogList, skuId, compName));
				input.setSupplier(findBySupIdAndCompName(supplierList, Integer.parseInt(supId), compName));
				createBarcodeItem(input);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public BarcodeEntity createBarcodeItem(BarcodeEntity entity) {
		if (entity != null)
		return	scbRepository.save(entity);
		return null;
	}

	@Override
	public CatalogEntityA createOrUpdateCatalogItem(CatalogEntityA entity) {
		// TODO Auto-generated method stub

		if (entity == null)
			return null;
		CatalogEntityA cEntity = null;
		if (entity.getSkuId() != null && !entity.getSkuId().isEmpty() && entity.getProductDesc() != null
				&& !entity.getProductDesc().isEmpty() && entity.getCatalogName() != null
				&& !entity.getCatalogName().isEmpty()) {

			if (entity.getId() == null) {
				// new entry
				CatalogEntityA newEntity = new CatalogEntityA(entity.getSkuId(), entity.getProductDesc(),
						entity.getCatalogName());
				cEntity = catalogRepositoryA.save(newEntity);

			} else {
				try {
					cEntity = getCatalogById(entity.getId());
					if (cEntity == null)
						cEntity = getCatalogByNameSkuPd(entity.getSkuId(), entity.getProductDesc(),
								entity.getCatalogName());
					else {
						cEntity.setCatalogName(entity.getCatalogName());
						cEntity.setSkuId(entity.getSkuId());
						cEntity.setProductDesc(entity.getProductDesc());
					}

				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catalogRepositoryA.save(cEntity);
			}
		} else
			return null;
		return cEntity;

	}

	@Override
	public void createSupplierItem(SupplierEntity entity) {
		if (entity != null)
			supplierRepository.save(entity);
	}

	@Override
	public List<CatalogEntityA> mergeByApplyingRules() {
		
		List<CatalogEntityA> list = (List<CatalogEntityA>) catalogRepositoryA.findAll();
		List<CatalogEntityA> mergedCatalogList = new ArrayList<CatalogEntityA> ();
		List<BarcodeEntity> bList = (List<BarcodeEntity>) scbRepository.findAll();
		for (CatalogEntityA x:list)
		{
			
			if (mergedCatalogList == null || mergedCatalogList.size() == 0)
			{
				mergedCatalogList.add(x);
			} else {
				if(!mergedCatalogList.contains(x))
				{
				List<CatalogEntityA>  parseMergeList = new ArrayList<>(mergedCatalogList);
				for(CatalogEntityA y:parseMergeList)
				{
					if(x!=null && y!=null && bList !=null)
					{
					if (isUnique(bList, x, y)) 
						{
							if(!mergedCatalogList.contains(x))
								mergedCatalogList.add(x);			
						} else
						{
							System.out.println("EXCLUDING DUPLICATES FROM MERGE:: Product Description = " +x.getProductDesc()+
									"	Company Name= "+x.getCatalogName());
							
							if(mergedCatalogList.contains(x))
								mergedCatalogList.remove(x);
							break;
						}
					}}
				}
				else
					continue;

			}
		}

	try {
	WriteToCsv.csvWriterOneByOne(curr_directory + outputPath,mergedCatalogList);

	} 
	catch (FileNotFoundException e) {	e.printStackTrace();}
	catch (Exception e) {	e.printStackTrace();}

return mergedCatalogList;

	}

	private boolean isUnique(List<BarcodeEntity> bList, CatalogEntityA x, CatalogEntityA y) 
	{
			
		try {
			BarcodeEntity tempAssocX = findByCatalogSkuAndCompName(bList, x);
			int supid_x = tempAssocX.getSupplier().getSupplierId();
			String barcode_x = tempAssocX.getBarCode();
			BarcodeEntity tempAssocY = findByCatalogSkuAndCompName(bList, y);
			int supid_y = tempAssocY.getSupplier().getSupplierId();
			String barcode_y = tempAssocY.getBarCode();

			if (y.getSkuId().trim().equalsIgnoreCase(x.getSkuId().trim()) && supid_x == supid_y
					&& barcode_x.trim().equalsIgnoreCase(barcode_y.trim()))
				return false;
			else {
				if (!y.getSkuId().trim().equalsIgnoreCase(x.getSkuId().trim())) {
					if (barcode_x.trim().equalsIgnoreCase(barcode_y.trim()))
						return false;
					else
						return true;

				} else
					return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private BarcodeEntity findByCatalogSkuAndCompName(List<BarcodeEntity> bList,
			CatalogEntityA temp) {
		
		if (bList == null)
			return null;
		for (int i = 0; i < bList.size(); i++) {
			if ((bList.get(i).getCatalog().getId() == temp.getId())
					&& bList.get(i).getCompName().equalsIgnoreCase(temp.getCatalogName())
					&& bList.get(i).getSkuId().equalsIgnoreCase(temp.getSkuId())) {
				return bList.get(i);
			}
		}
		return null;
	}

	public CatalogEntityA findCatalogBySkuAndCompName(List<CatalogEntityA> list, String skuId, String compName) {
		if (list == null)
			return null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCatalogName().equalsIgnoreCase(compName)
					&& list.get(i).getSkuId().equalsIgnoreCase(skuId)) {
				return list.get(i);
			}
		}
		return null;
	}

	private SupplierEntity findBySupIdAndCompName(List<SupplierEntity> list, int supId, String compName) {
		if (list == null)
			return null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSupplierCompany().equalsIgnoreCase(compName) && list.get(i).getSupplierId() == supId) {
				return list.get(i);
			}
		}
		return null;
	}
	
	@Override
	public BarcodeEntity associateSupplierAndBarcodes(CatalogEntityA catItem, int supId, String supName, String supCompany) throws RecordNotFoundException
	{
		BarcodeEntity tempObj = null;
		try
		{
		if(supCompany == null)
			supCompany = catItem.getCatalogName();
		int supIndex = 0;
		if(supId == 0)
			supIndex = getAllSuppliers().stream()
				.filter(x -> x.getSupplierCompany().equalsIgnoreCase(catItem.getCatalogName())).collect(Collectors.toList()).size();
		else
			supIndex = supId;
		
		
		SupplierEntity newSupplier = new SupplierEntity();
		newSupplier.setSupplierCompany(supCompany);
		newSupplier.setSupplierId(supIndex+1);
		if (supName == null ||supName.isEmpty()) {
			newSupplier.setSupplierName("SUP_" + supIndex);
		} else {
			newSupplier.setSupplierName(supName);
		}
		createSupplierItem(newSupplier);
		for (int i=0;i<5;i++)
		{
			tempObj =  
					new BarcodeEntity(
							catItem.getSkuId(),
							RandomIndexGenerator.getRandomHexString(14),
							supCompany);
			tempObj.setCatalog(catItem);
			tempObj.setSupplier(newSupplier);
			return	createBarcodeItem(tempObj) ;
		}
		}
		catch(Exception e)
		{
			return null;
		}
	return tempObj;
	}
	
	@Override
	public BarcodeEntity updateSupplierOnCatalog(BarcodeEntity barCodeItem, @RequestParam("sId") int supId, @RequestParam("sName") String supName) 
		    throws RecordNotFoundException
		    {
		    	CatalogEntityA obj = getBarcodeById(barCodeItem.getId()).getCatalog();
		    	return associateSupplierAndBarcodes(obj,supId,supName,barCodeItem.getCompName());
		    }
		 
	
	@Override
	public String modifySupplierByCatalogId(Model model, @PathVariable("id") Optional<Long> id)
			throws RecordNotFoundException {
		if (id.isPresent()) {

			CatalogEntityA entity = getCatalogById(id.get());
			List<BarcodeEntity> suppliersAssoc = getAllBarcodes().stream().filter(x -> x.getCatalog().equals(entity))
					.collect(Collectors.toList());
			List<SupplierEntity> suppliers = new ArrayList<SupplierEntity>();
			suppliersAssoc.forEach(x -> {
				if (suppliers != null && x.getSupplier() != null && !suppliers.contains(x.getSupplier()))
					suppliers.add(x.getSupplier());
			});
			if (suppliers.size() > 0) {
				model.addAttribute("suppliers", suppliers);
				model.addAttribute(catalogItem, suppliersAssoc.get(0));
			}
		} else {
			model.addAttribute("catalogItem", new BarcodeEntity());
		}
		return modify;
	}

	
}
