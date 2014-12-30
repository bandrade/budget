package com.dupont.budget.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.service.DeliveryHandlerService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.event.FileUploadEvent;
import com.dupont.budget.service.event.Uploaded;

@Stateless
public class DeliveryHandlerServiceBean implements DeliveryHandlerService {  

	@Inject
	private Logger logger;
	
	@Inject
	private DomainService service;
    
    private Sheet loadFileSheet(File file) {
    	Sheet sheet = null;
    	
    	try (FileInputStream fis = new FileInputStream(file)) {
    		
			if (file.getName().contains("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(fis);
				sheet = workbook.getSheetAt(0);
			} else {
				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				sheet = workbook.getSheetAt(0);
			}
    	} catch (IOException e) {
    		logger.debug(e.getLocalizedMessage());
    	}
    	return sheet;
    }
    
    /*
     * (non-Javadoc)
     * @see com.dupont.budget.service.DeliveryHandlerService#onFornecedorUpload(com.dupont.budget.service.event.FileUploadEvent)
     */
    @Override
    @Asynchronous
    public void onFornecedorUpload(@Observes @Uploaded(Fornecedor.class) FileUploadEvent event) {
		logger.debug("Evento de cadastro de fornecedores sendo processado pelo serviço.");

		File file = new File(event.getPath());

		try {
			Sheet sheet = loadFileSheet(file);
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
						if (!service.findByName(fornecedor).isEmpty()) {
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
					for (Fornecedor o : persistent) {
						o.setAtivo(false);
						service.update(o);
					}
				}

			} catch (Exception e) {
				logger.error(String.format("Nao foi possivel finalizar o cadastro de fornecedores: %s", e.getLocalizedMessage()));
			}
			logger.debug(String.format("%d entradas do arquivos processadas com sucesso!",counter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @Override
    @Asynchronous
	public void onValorComprometidoUpload(@Observes @Uploaded(ValorComprometido.class) FileUploadEvent event) {
		logger.debug("Evento de cadastro de valores comprometidos sendo processado pelo serviço.");

		File file = new File(event.getPath());

		try {
			Sheet sheet = loadFileSheet(file);
			Iterator<Row> rowIterator = sheet.iterator();
			int counter = 0;

			try {
				ValorComprometido valor = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					valor = service.findValorComprometidoByFiltro(
							row.getCell(0).getStringCellValue(), 
							row.getCell(1).getStringCellValue(), 
							row.getCell(2).getStringCellValue(), 
							(int) row.getCell(4).getNumericCellValue());
					if (valor != null) {
						valor.setValor(row.getCell(5).getNumericCellValue());
						service.update(valor);
					} else {
						valor = new ValorComprometido();
						List<CentroCusto> centrosCusto = service.findByName(new CentroCusto(row.getCell(0).getStringCellValue()));
						if (centrosCusto.isEmpty() || centrosCusto.size() > 1) {
							continue;
						}
						valor.setCentroCusto(centrosCusto.get(0));
						List<TipoDespesa> tiposDespesa = service.findByName(new TipoDespesa(row.getCell(1).getStringCellValue()));
						if (tiposDespesa.isEmpty() || tiposDespesa.size() > 1) {
							continue;
						}
						valor.setTipoDespesa(tiposDespesa.get(0));
						List<Acao> acoes = service.findByName(new Acao(row.getCell(2).getStringCellValue()));
						if (acoes.isEmpty() || acoes.size() > 1) {
							continue;
						}
						valor.setAcao(acoes.get(0));
						valor.setAtivo(true);
						valor.setMes((int) row.getCell(4).getNumericCellValue());
						valor.setValor(row.getCell(5).getNumericCellValue());
						service.create(valor);
					}
					counter++;
				}
			} catch (Exception e) {
				logger.error(String.format("Nao foi possivel finalizar a carga de valores comprometidos: %s", e.getLocalizedMessage()));
			}
			logger.debug(String.format("%d entradas do arquivos processadas com sucesso!",counter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
    @Asynchronous
	public void onSolicitacaoPagamentoUpload(@Observes @Uploaded(SolicitacaoPagamento.class) FileUploadEvent event) {
    	logger.debug("Evento de atualização de solicitações de pagamento sendo processado pelo serviço.");

		File file = new File(event.getPath());

		try {
			Sheet sheet = loadFileSheet(file);
			Iterator<Row> rowIterator = sheet.iterator();
			int counter = 0;

			try {
				List<Fornecedor> persistent = service.findAll(Fornecedor.class);
				SolicitacaoPagamento solicitacao = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					solicitacao = service.findSolicitacaoPagamentoByFiltro(
							row.getCell(2).getStringCellValue(), 
							row.getCell(6).getStringCellValue(), 
							row.getCell(8).getStringCellValue());
					if (solicitacao != null) {
						Double d = row.getCell(8).getNumericCellValue();
						solicitacao.setValor(d);
						service.update(solicitacao);
						counter++;
					}
				}

				if (!persistent.isEmpty()) {
					for (Fornecedor o : persistent) {
						o.setAtivo(false);
						service.update(o);
					}
				}

			} catch (Exception e) {
				logger.error(String.format("Nao foi possivel finalizar o cadastro de fornecedores: %s", e.getLocalizedMessage()));
			}
			logger.debug(String.format("%d entradas do arquivos processadas com sucesso!",counter));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
