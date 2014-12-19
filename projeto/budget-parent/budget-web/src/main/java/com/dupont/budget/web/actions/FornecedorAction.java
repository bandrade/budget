package com.dupont.budget.web.actions;

import java.util.Iterator;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade {@link Fornecedor}
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
@RolesAllowed(value = "ADMINISTRADOR")
public class FornecedorAction extends GenericAction<Fornecedor> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;
	
	private UploadedFile file;

	@Named
	@Produces
	public Fornecedor getFornecedor() {
		return getEntidade();
	}
	
	@Override
	public String persist() {
		logger.debug("Arquivo recebido, iniciando processamento!");

		Sheet sheet = null;
		
		try {
			if (file.getFileName().endsWith("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputstream());
				sheet = workbook.getSheetAt(0);
			} else {
				HSSFWorkbook workbook = new HSSFWorkbook(file.getInputstream());
				sheet = workbook.getSheetAt(0);
			}
		} catch (Exception e) {
			facesUtils.addErrorMessage(String.format("Não foi possível ler o arquivo: %s", e.getLocalizedMessage()));
			return null;
		}

		Iterator<Row> rowIterator = sheet.iterator();
			
		int counter = 0;
		
		try {
			int blank = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Cell cell = row.getCell(0);
				String value = cell.getStringCellValue();
				if (value != null && !value.isEmpty()) {
					service.create(new Fornecedor(value, false));
					counter++;
				} else {
					blank++;
				}
				
				if (blank >= 3) {
					break;
				}
			}
		} catch (Exception e) {
			facesUtils.addErrorMessage(String.format("Erro durante o processamento do arquivo: %s", e.getLocalizedMessage()));
			return null;
		}
		
		facesUtils.addInfoMessage(String.format("%d entradas do arquivos processadas com sucesso!", counter));
		
		return "list";
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			logger.debug("Arquivo recebido, iniciando processamento!");

			Sheet sheet = null;

			if (event.getFile().getFileName().endsWith("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(event.getFile().getInputstream());
				sheet = workbook.getSheetAt(0);
			} else {
				HSSFWorkbook workbook = new HSSFWorkbook(event.getFile().getInputstream());
				sheet = workbook.getSheetAt(0);
			}

			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t\t");
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t");
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByName(entidade);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected DomainService getService() {
		return service;
	}

	@Override
	protected FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
