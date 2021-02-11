package com.catalogmerge.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catalogmerge.dao.CatalogEntityA;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

public class WriteToCsv {
	private static final Logger LOGGER = LoggerFactory.getLogger(WriteToCsv.class);

	public static void csvWriterOneByOne(String fileName, List<CatalogEntityA> mergedCatalogList) throws Exception {
		try
		{
		PrintWriter pw = null;
		File outputFile = new File(fileName);
		try {
			pw = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {}

		String column_separator = ",";
		String columnNamesList = "SKU ID,Product Desciption, Company Name";
		StringBuilder builder = new StringBuilder();
		builder.append(columnNamesList);

		mergedCatalogList.forEach(x -> {
			builder.append("\n");
			builder.append(x.getSkuId()).append(column_separator);
			builder.append(x.getProductDesc()).append(column_separator);
			builder.append(x.getCatalogName()).append(column_separator);
			builder.append('\n');
		});
		pw.write(builder.toString());
		pw.close();
		Runtime.getRuntime().exec(new String[] { "cmd.exe", "/C", fileName });

	}
		catch(Exception e) {};
	}

	public static void writeOutputToCsv(PrintWriter writer, List<CatalogEntityA> mergedCatalogList) {

		try {

			ColumnPositionMappingStrategy<CatalogEntityA> mapStrategy = new ColumnPositionMappingStrategy<>();

			mapStrategy.setType(CatalogEntityA.class);

			String[] columns = new String[] { "skuId", "productDesc", "catalogName" };
			mapStrategy.setColumnMapping(columns);

			StatefulBeanToCsv<CatalogEntityA> btcsv = new StatefulBeanToCsvBuilder<CatalogEntityA>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withMappingStrategy(mapStrategy).withSeparator(',')
					.build();

			btcsv.write(mergedCatalogList);

		} catch (CsvException ex) {

			LOGGER.error("Error mapping Bean to CSV", ex);
			ex.printStackTrace();
			ex.printStackTrace(writer);
		}
	}

	public static void writeCity(PrintWriter writer, CatalogEntityA catalog) {

		try {

			ColumnPositionMappingStrategy<CatalogEntityA> mapStrategy = new ColumnPositionMappingStrategy<>();

			mapStrategy.setType(CatalogEntityA.class);

			String[] columns = new String[] { "id", "name", "population" };
			mapStrategy.setColumnMapping(columns);

			StatefulBeanToCsv<CatalogEntityA> btcsv = new StatefulBeanToCsvBuilder<CatalogEntityA>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withMappingStrategy(mapStrategy).withSeparator(',')
					.build();

			btcsv.write(catalog);

		} catch (CsvException ex) {

			LOGGER.error("Error mapping Bean to CSV", ex);
		}
	}
}
