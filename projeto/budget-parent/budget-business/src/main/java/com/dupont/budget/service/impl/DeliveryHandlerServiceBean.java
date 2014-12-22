package com.dupont.budget.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.service.DeliveryHandlerService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.event.FornecedorEvent;

@Stateless
public class DeliveryHandlerServiceBean implements DeliveryHandlerService {  

	@Inject
	private Logger logger;
	
	@Inject
	private DomainService service;
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DeliveryHandlerService#onNewCadastroFornecedor(com.dupont.budget.service.event.FornecedorEvent)
	 */
    @Override
    @Asynchronous
    public void onNewCadastroFornecedor(@Observes FornecedorEvent event) {
 
        logger.debug("Evento de cadastro de fornecedores sendo processado pelo serviço.");
        
        File file = new File(event.getFilePath());
 
        try {
        	Sheet sheet = null;
        	
        	FileInputStream fis = new FileInputStream(file);        	
    		
    		try {
    			if (file.getName().contains("xlsx")) {
    				XSSFWorkbook workbook = new XSSFWorkbook(fis);
    				sheet = workbook.getSheetAt(0);
    			} else {
    				HSSFWorkbook workbook = new HSSFWorkbook(fis);
    				sheet = workbook.getSheetAt(0);
    			}
    		} catch (Exception e) {
    			logger.error(e.getLocalizedMessage());
    		}

    		Iterator<Row> rowIterator = sheet.iterator();
    			
    		int counter = 0;
    		
    		try {
    			int blank = 0;
    			List<Fornecedor> persistent = service.findAll(Fornecedor.class);
    			while (rowIterator.hasNext()) {
    				Row row = rowIterator.next();
    				Cell cell = row.getCell(0);
    				String value = cell.getStringCellValue();
    				if (value != null && !value.isEmpty()) {
    					Fornecedor fornecedor = new Fornecedor(value, true);
    					if (service.findByName(fornecedor) != null) {
    						persistent.remove(fornecedor);
    					} else {
    						service.create(fornecedor);
    					}
    					counter++;
    				} else {
    					blank++;
    				}
    				
    				if (blank >= 3) {
    					break;
    				}
    			}
    			
    			if (!persistent.isEmpty()) {
    				for (Fornecedor o: persistent) {
    					o.setAtivo(false);
    					service.update(o);
    				}
    			}
    			
    		} catch (Exception e) {
    			logger.error(String.format("Nao foi possivel finalizar o cadastro de fornecedores: %s", e.getLocalizedMessage()));
    		}
    		
    		logger.debug(String.format("%d entradas do arquivos processadas com sucesso!", counter));
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
