package com.catalogmerge.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.catalogmerge.dao.BarcodeEntity;
import com.catalogmerge.dao.CatalogEntityA;
import com.catalogmerge.exception.RecordNotFoundException;
import com.catalogmerge.service.CatalogMergeService;

/**
 * @author divya
 *
 */
@Controller
@RequestMapping("/")
public class CatalogMergeController {

	@Autowired
	private CatalogMergeService service;
	
	@Value("${catalogItem}") String catalogItem;
	@Value("${catalogs}") String catalogs;
	@Value("${edit}") String edit;
	/**
	 * @param model
	 * @return
	 */
	@RequestMapping
	public String getCatalogs(Model model) {
		List<CatalogEntityA> list = service.getAllCatalogs();
		model.addAttribute(catalogs, list);
		CatalogEntityA entity = new CatalogEntityA();
		model.addAttribute(catalogItem, entity);
		return catalogs;
	}

	/**
	 * @param model
	 * @param id
	 * @return
	 * @throws RecordNotFoundException
	 */
	@RequestMapping(path = { "/edit", "/edit/{id}" })
	public String editCatalogById(Model model, @PathVariable("id") Optional<Long> id) throws RecordNotFoundException {
		if (id.isPresent()) {
			CatalogEntityA entity = service.getCatalogById(id.get());
			model.addAttribute("catalogItem", entity);
		} else {
			model.addAttribute("catalogItem", new CatalogEntityA());
		}

		return edit;
	}

	
	@RequestMapping(path = { "/showall", "/showall" })
	public String editCatalogById(Model model) throws RecordNotFoundException {
		return getCatalogs(model);
	}
	/**
	 * @param model
	 * @param id
	 * @return
	 * @throws RecordNotFoundException
	 */
	@RequestMapping(path = { "/merge", "/merge" })
	public String mergeCatalogs(Model model) throws RecordNotFoundException {
		// Load/replace Catalog Table
		if (service.getAllCatalogs().isEmpty())
			service.loadData();
		List<CatalogEntityA> list  = service.mergeByApplyingRules();
		//getCatalogs(model);
		model.addAttribute(catalogs, list);
		CatalogEntityA entity = new CatalogEntityA();
		model.addAttribute(catalogItem, entity);
		return catalogs;
	}

	/**
	 * @param model
	 * @param id
	 * @return
	 * @throws RecordNotFoundException
	 */
	@RequestMapping(path = "/delete/{id}")
	public String deleteCatalogById(Model model, @PathVariable("id") Long id) throws RecordNotFoundException {

		try {
			service.deleteCatalogById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		return "redirect:/";
	}

	/**
	 * @param catalogItem
	 * @return
	 * @throws RecordNotFoundException
	 */
	@RequestMapping(path = "/createCatalogItem", method = RequestMethod.POST)
	public String createOrUpdateCatalogItem(CatalogEntityA catalogItem) throws RecordNotFoundException {
		try {
			service.associateSupplierAndBarcodes(catalogItem, 0, null, catalogItem.getCatalogName());
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		return "redirect:/";

	}

	/**
	 * @param model
	 * @param id
	 * @return
	 * @throws RecordNotFoundException Pre-Tag
	 */
	@RequestMapping(path = { "/modify", "/modify/{id}" })
	public String modifySupplierByCatalogId(Model model, @PathVariable("id") Optional<Long> id)
			throws RecordNotFoundException {

		return service.modifySupplierByCatalogId(model, id);
		}

	/**
	 * @param catalogItem
	 * @return
	 * @throws RecordNotFoundException Post Tag
	 */
	
	 @RequestMapping(path = "/updateSupplier", method = RequestMethod.POST) 
	 public String updateSupplierOnCatalog(BarcodeEntity barCodeItem, @RequestParam("sId") int supId, @RequestParam("sName") String supName) throws RecordNotFoundException 
	 { 
		 if(barCodeItem!= null) 
		  service.updateSupplierOnCatalog(barCodeItem, supId, supName);
		 else
			 return "error";
		 return "redirect:/"; 
		 
	 }
	
}
