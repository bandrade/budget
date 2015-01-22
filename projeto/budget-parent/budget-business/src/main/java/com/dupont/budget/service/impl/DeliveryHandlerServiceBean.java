package com.dupont.budget.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
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
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaForecastPK;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.OrigemSolicitacao;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DeliveryHandlerService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.event.UploadEvent;
import com.dupont.budget.service.event.Uploaded;

@Stateless
public class DeliveryHandlerServiceBean implements DeliveryHandlerService {

	@Inject
	private Logger logger;

	@Inject
	private DomainService service;
	
	@Inject
	private ForecastService forecastService;
	
	@Inject
	private BudgetService budgetService;

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
    public void onFornecedorUpload(@Observes @Uploaded(Fornecedor.class) UploadEvent event) {
	
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
	public void onValorComprometidoUpload(@Observes @Uploaded(ValorComprometido.class) UploadEvent event) {
		logger.debug("Evento de cadastro de valores comprometidos sendo processado pelo serviço.");
		Integer ano = Calendar.getInstance().get(Calendar.YEAR);
		File file = new File(event.getPath());

		try {
			Sheet sheet = loadFileSheet(file);
			Iterator<Row> rowIterator = sheet.iterator();
			int counter = 0;

				ValorComprometido valor = null;
				while (rowIterator.hasNext()) {
					try {
						Row row = rowIterator.next();
						String centroDeCustoRow = row.getCell(0).getStringCellValue();
						String tipoDespesaRow = row.getCell(1).getStringCellValue();
						String acaoRow = row.getCell(2).getStringCellValue();
						long mesRow= (long) row.getCell(3).getNumericCellValue();
						Double valorRow = row.getCell(4).getNumericCellValue();
						valor = service.findValorComprometidoByFiltro(
								centroDeCustoRow,
								tipoDespesaRow,
								acaoRow,
								mesRow);
						if (valor != null) {
							valor.setValor(valorRow);
							service.update(valor);
						} else {
							valor = new ValorComprometido();
							CentroCusto centroCusto = service.findCentroCustoByCodigo(centroDeCustoRow);
							if (centroCusto ==null) {
								continue;
							}
							valor.setCentroCusto(centroCusto);
							List<TipoDespesa> tiposDespesa = service.findByNameEqual(new TipoDespesa(tipoDespesaRow.toLowerCase()));
							if (tiposDespesa.isEmpty() || tiposDespesa.size() > 1) {
								continue;
							}
							TipoDespesa tipoDespesa = tiposDespesa.get(0);
							valor.setTipoDespesa(tipoDespesa);
							
							Budget budget = budgetService.findByAnoAndCentroDeCusto(ano+"", centroCusto.getId());
							boolean possuiBudget = budget !=null;
							Forecast forecast = forecastService.findForecastByCCAndAno(ano+"", centroCusto.getId());
							if(forecast ==null)
								continue;
							
							Acao acao = service.findAcaoByForecastOrBudget(possuiBudget? budget.getId():null ,forecast.getId(),acaoRow.toLowerCase()) ;
							if (acao == null) {
								acao = new Acao(acaoRow);
								if(possuiBudget)
								{
									acao.setBudget(budget);
								}
								else
								{
									acao.setForecast(forecast);
								}
								service.create(acao);
							}
							DespesaForecast despesaForecast = forecastService.obterDespesaForecast(forecast,tipoDespesa , acao);
							if(despesaForecast == null)
							{
								despesaForecast = new DespesaForecast();
								despesaForecast.setForecast(forecast);
								despesaForecast.setAcao(acao);
								despesaForecast.setAtivo(true);
								despesaForecast.setTipoDespesa(tipoDespesa);
								despesaForecast.setValor(0D);
								despesaForecast.setDespesaPK(new DespesaForecastPK(ano+"", mesRow, null));
								forecastService.incluirDespesaForecast(despesaForecast);
								
							}
							valor.setAcao(acao);
							valor.setAtivo(true);
							valor.setMes(Integer.valueOf(mesRow+""));
							valor.setValor(valorRow);
							valor.setAno(ano);
							service.create(valor);
						}
						counter++;
					} catch (Exception e) {
						logger.error(String.format("Nao foi possivel finalizar a carga de valores comprometidos: %s", e.getLocalizedMessage()));
					}
				}
			logger.debug(String.format("%d entradas do arquivos processadas com sucesso!",counter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    @Asynchronous
	public void onSolicitacaoPagamentoUpload(@Observes @Uploaded(SolicitacaoPagamento.class) UploadEvent event) {
    	logger.debug("Evento de atualização de solicitações de pagamento sendo processado pelo serviço.");

		File file = new File(event.getPath());
		Sheet sheet = loadFileSheet(file);
		Iterator<Row> rowIterator = sheet.iterator();

		try {
			SolicitacaoPagamento solicitacao = null;

			File csv = createFile();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(csv))) {
				line:
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					try {
						String numeroNota = row.getCell(7).getStringCellValue();
						if (numeroNota == null || numeroNota.trim().isEmpty()) {
							writeLine("Número da Nota vazio.", bw, row);
							continue;
						}

						List<Fornecedor> list = service.findByName(new Fornecedor(row.getCell(6).getStringCellValue()));
						if (list.isEmpty() || list.size() > 1) {
							writeLine("Fornecedor não encontrado", bw, row);
							continue;
						}

						CentroCusto centroCusto = service.findCentroCustoByCodigo(row.getCell(8).getStringCellValue());
						if (centroCusto == null) {
							writeLine("Centro de custo não encontrado", bw, row);
							continue;
						}

						Double valor = row.getCell(16).getNumericCellValue();
						solicitacao = service.findSolicitacaoByNumeroNota(row.getCell(7).getStringCellValue());

						if (solicitacao == null) {
							SolicitacaoPagamento o = new SolicitacaoPagamento(StatusPagamento.PENDENTE_VALIDACAO);
							o.setFornecedor(list.get(0));
							o.setNumeroNotaFiscal(numeroNota);
							o.setOrigem(OrigemSolicitacao.SAP);
							o.setValor(valor);
							o.setDataPagamento(new Date());
							o.setCriacao(new Date());
							service.create(o);
							DespesaSolicitacaoPagamento d = new DespesaSolicitacaoPagamento();
							d.setSolicitacaoPagamento(o);
							d.setCentroCusto(centroCusto);
							d.setValor(valor);
							service.create(d);
							o.getDespesas().add(d);
							service.update(o);
							writeLine("PENDENTE_VALIDACAO", bw, row);
							continue;
						} else {
							List<DespesaSolicitacaoPagamento> despesas = solicitacao.getDespesas();
							for (Iterator<DespesaSolicitacaoPagamento> iterator = despesas.iterator(); iterator.hasNext();) {
								DespesaSolicitacaoPagamento d = iterator.next();
								if (d.getValor().equals(valor)) {
									if (!d.getCentroCusto().equals(centroCusto)) {
										writeLine("Centro de Custo divergente entre Cover Sheet e Relatório SAP", bw, row);
										continue line;
									} else if (solicitacao.getFornecedor().equals(list.get(0))) {
										writeLine("Fornecedor divergente entre Cover Sheet e Relatório SAP", bw, row);
										continue line;
									}
								} else {
									if (d.getCentroCusto().equals(centroCusto) && solicitacao.getFornecedor().equals(list.get(0))) {
										writeLine("Não foi possível tratar o registro. Existe mais de uma despesa com o mesmo fornecedor e centro de custo.", bw, row);
										continue line;
									}
								}
							}
						}
						writeLine("Valor não processado.", bw, row);
					} catch (Exception e) {
						bw.write(String.format("%s;%s;%s;%s;Erro durante a leitura\n", row.getCell(7).getStringCellValue(), row.getCell(8).getStringCellValue(), row.getCell(6).getStringCellValue(), row.getCell(16).getStringCellValue().replace(",", ".")));
					}
				}
			} catch (IOException e) {
				logger.error(String.format("Erro durante a gravação do arquivo de log: %s", e.getLocalizedMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void writeLine(String message, BufferedWriter bw, Row row) throws IOException {
		bw.write(String.format("%s;%s;%s;%.2f;%s\n", row.getCell(7).getStringCellValue(), row.getCell(8).getStringCellValue(), row.getCell(6).getStringCellValue(), row.getCell(16).getNumericCellValue(), message));
	}

    private File createFile() {
    	String tmpDir = System.getProperty("java.io.tmpdir");
		final String name = DeliveryHandlerService.RELATORIO_SAP_TEMP_DIR;
		tmpDir = tmpDir.endsWith(String.valueOf(File.separatorChar)) ? tmpDir.concat(name) : tmpDir.concat(String.valueOf(File.separatorChar)).concat(name);

		File dir = new File(tmpDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		return new File(dir.getAbsolutePath().concat(String.valueOf(File.separatorChar)).concat(new BigInteger(130, new SecureRandom()).toString(32)));
    }
}
