package com.dupont.budget.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
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
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
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
		Sheet sheet = loadFileSheet(file);
		Iterator<Row> rowIterator = sheet.iterator();

		try {
			SolicitacaoPagamento solicitacao = null;
			
			File csv = createFile();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(csv))) {
				row: 
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					List<Fornecedor> list = service.findByName(new Fornecedor(row.getCell(6).getStringCellValue()));
					if (list.isEmpty() || list.size() > 1) {
						bw.write(String.format("%s;%s;%s;%.2f;Fornecedor não encontrado", row.getCell(2).getStringCellValue(), row.getCell(8).getStringCellValue(), row.getCell(6).getStringCellValue(), row.getCell(16).getNumericCellValue()));
						continue;
					}
					
					CentroCusto centroCusto = service.findCentroCustoByCodigo(row.getCell(8).getStringCellValue());
					if (centroCusto == null) {
						bw.write(String.format("%s;%s;%s;%.2f;Centro de Custo não encontrado", row.getCell(2).getStringCellValue(), row.getCell(8).getStringCellValue(), row.getCell(6).getStringCellValue(), row.getCell(16).getNumericCellValue()));
						continue;
					}
					
					String numeroNota = row.getCell(2).getStringCellValue();
					Double valor = row.getCell(16).getNumericCellValue();
					Fornecedor fornecedor = list.get(0);
					solicitacao = service.findSolicitacaoByNumeroNota(row.getCell(2).getStringCellValue());
					
					if (solicitacao == null) {
						DespesaSolicitacaoPagamento tmp = new DespesaSolicitacaoPagamento();
						tmp.setCentroCusto(centroCusto);
						SolicitacaoPagamento o = new SolicitacaoPagamento(StatusPagamento.PENDENTE_VALIDACAO);
						o.setFornecedor(list.get(0));
						o.setNumeroNotaFiscal(row.getCell(2).getStringCellValue());
						DespesaSolicitacaoPagamento d = new DespesaSolicitacaoPagamento();
						d.setSolicitacaoPagamento(solicitacao);
						d.setCentroCusto(centroCusto);
						d.setValor(valor);
						o.getDespesas().add(d);
						service.create(o);
						bw.write(String.format("%s;%s;%s;%.2f;PENDENTE_VALIDACAO", numeroNota, centroCusto.getNome(), fornecedor.getNome(), valor));
					} else {
						for (Iterator<DespesaSolicitacaoPagamento> iterator = solicitacao.getDespesas().iterator(); iterator.hasNext();) {
							DespesaSolicitacaoPagamento d = iterator.next();
							if (d.getValor().equals(valor)) {
								if (!d.getCentroCusto().equals(centroCusto)) {
									bw.write(String.format("%s;%s;%s;%.2f;Centro de Custo divergente entre Cover Sheet e Relatório SAP", numeroNota, centroCusto.getNome(), fornecedor.getNome(), valor));
									continue row;
								} else if (solicitacao.getFornecedor().equals(list)) {
									bw.write(String.format("%s;%s;%s;%.2f;Fornecedor divergente entre Cover Sheet e Relatório SAP", numeroNota, centroCusto.getNome(), fornecedor.getNome(), valor));
									continue row;
								}
							} else {
								if (d.getCentroCusto().equals(centroCusto) && solicitacao.getFornecedor().equals(list)) {
									bw.write(String.format("%s;%s;%s;%d;Valor divergente entre Cover Sheet e Relatório SAP"));
									continue row;
								}
							}
						}
					}
				}
			} catch (IOException e) {
				logger.error(String.format("Erro durante a gravação do arquivo de log: %s", e.getLocalizedMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private File createFile() {
    	String tmpDir = System.getProperty("java.io.tmpdir");
		final String name = DeliveryHandlerService.RELATORIO_SAP_TEMP_DIR;
		tmpDir = tmpDir.endsWith(String.valueOf(File.separatorChar)) ? tmpDir.concat(name) : tmpDir.concat(String.valueOf(File.separatorChar)).concat(name);
		
		File dir = new File(tmpDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		return new File(dir.getAbsolutePath().concat(new BigInteger(130, new SecureRandom()).toString(32)));
    }
}
