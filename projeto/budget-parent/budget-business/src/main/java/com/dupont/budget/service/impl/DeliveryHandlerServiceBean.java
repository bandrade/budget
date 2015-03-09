package com.dupont.budget.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.NonUniqueResultException;

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
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.OrigemSolicitacao;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.TipoSolicitacao;
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
		Integer mes= Calendar.getInstance().get(Calendar.MONTH)+1;
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
							valor.setValor(new BigDecimal(valorRow));
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
								despesaForecast.setValor(new BigDecimal(0D));
								forecastService.incluirDespesaForecast(despesaForecast,mes);

							}
							valor.setAcao(acao);
							valor.setAtivo(true);
							valor.setMes(Integer.valueOf(mesRow+""));
							valor.setValor(new BigDecimal(valorRow));
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
		Calendar c = Calendar.getInstance();
		Iterator<Row> rowIterator = sheet.iterator();
		List<SolicitacaoPagamento> rateiosPendentes = new ArrayList<>();

		try {
			SolicitacaoPagamento solicitacao = null;
			File csv = createFile();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(csv))) {
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					try {
						logger.info("Leitura da linha : " +row.getRowNum()  + " Quantidade de celulas: "+ row.getPhysicalNumberOfCells() + " " );
						row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						Integer per = Integer.valueOf(row.getCell(0).getStringCellValue());


						c.setTime(new Date());
						c.set(Calendar.DAY_OF_MONTH, 1);
						c.set(Calendar.MONTH, per-1);

						String numeroNota = null;
						row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
						numeroNota= row.getCell(7).getStringCellValue();
						if (numeroNota == null || numeroNota.trim().isEmpty()) {
							writeLine("Número da Nota vazio.", bw, row);
							continue;
						}


						List<Fornecedor> list = service.findByName(new Fornecedor(row.getCell(6).getStringCellValue()));
						if (list.isEmpty() || list.size() > 1) {
							writeLine("Fornecedor não encontrado", bw, row);
							continue;
						}
						Fornecedor fornecedor = list.get(0);

						CentroCusto centroCusto = null;

						try
						{

							centroCusto = service.findCentroCustoByCodigo(row.getCell(8).getStringCellValue());
							if (centroCusto == null) {
								writeLine("Centro de custo não encontrado", bw, row);
								continue;
							}
						}
						catch (NonUniqueResultException e) {
							writeLine("Centro de custo duplicado", bw, row);
							continue;
						}

						Double _valor = row.getCell(16).getNumericCellValue();
						BigDecimal valor =  new BigDecimal(_valor);
						solicitacao = service.findSolicitacaoByNumeroNotaEFornecedor(row.getCell(7).getStringCellValue(),list.get(0).getId());

						if (solicitacao == null) {
							if(valor.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal(0.0d).setScale(2, RoundingMode.HALF_UP))<0)
							{
								writeLine("NÃO É POSSÍVEL CRIAR UMA RECLASSIFICAÇÃO PARA UMA NOTA NÃO EXISTENTE", bw, row);
								continue;
							}
							criarCoverSheet(fornecedor,numeroNota,valor,centroCusto,c.getTime());
							writeLine("PENDENTE_VALIDACAO", bw, row);
							continue;
						}
						else if(solicitacao.getStatus().equals(StatusPagamento.PAGO) && valor.compareTo(new BigDecimal(0d)) >=0)
							continue;
						else {
								DespesaSolicitacaoPagamento d =  solicitacao.getDespesaByCC(centroCusto);
								if(d !=null)
								{
									//tratamento de reclassificacao
								    if (valor.compareTo(new BigDecimal(0d)) <0) {
										tratarReClassificacao(solicitacao,d,valor,bw,row);
										continue;
									}
									else if (!(d.getValor().equals(valor))) {
										writeLine("Valor divergente entre Cover Sheet e Relatório SAP", bw, row);
										continue ;
									}
									//tratamento de rateio
									else  if(solicitacao.getTipoSolicitacao().equals(TipoSolicitacao.RATEIO))
									{
											int index = rateiosPendentes.indexOf(solicitacao);

											if(index >=0)
											{
												solicitacao = rateiosPendentes.get(index);
												solicitacao.setDataPagamentoRealizado(c.getTime());
												solicitacao.addDespesasContabilizada(d);
												solicitacao.addRow(row);
												rateiosPendentes.set(index,solicitacao);
											}
											else
											{
												solicitacao.setDataPagamentoRealizado(c.getTime());
												solicitacao.addDespesasContabilizada(d);
												solicitacao.addRow(row);
												rateiosPendentes.add(solicitacao);
											}
											continue;
									}
								solicitacao.setDataPagamentoRealizado(c.getTime());
								solicitacao.setStatus(StatusPagamento.PAGO);
								service.update(solicitacao);
								writeLine("SUCESSO", bw, row);

								}
								//tratamento de rateio pendente de validacao
								else if(solicitacao.getStatus().equals(StatusPagamento.PENDENTE_VALIDACAO))
								{
											DespesaSolicitacaoPagamento _despesa = new DespesaSolicitacaoPagamento();
											_despesa.setSolicitacaoPagamento(solicitacao);
											_despesa.setCentroCusto(centroCusto);
											_despesa.setValor(valor);
											service.create(_despesa);
											solicitacao.getDespesas().add(_despesa);
											solicitacao.setTipoSolicitacao(TipoSolicitacao.RATEIO);
											solicitacao.setValor(solicitacao.getValor().add(valor));
											solicitacao.addDespesaSolicitacaoPagamento(_despesa);
											service.update(solicitacao);
											writeLine("PENDENTE_VALIDACAO", bw, row);
											continue;
								}
							}

					} catch (Exception e) {
						if(row.getCell(7)!=null && row.getCell(8) !=null && row.getCell(6)!=null && row.getCell(16)!=null)

							row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
							row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
							row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
							row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
							bw.write(String.format("%s;%s;%s;%s;Erro durante a leitura\n", row.getCell(7).getStringCellValue(),
									row.getCell(8).getStringCellValue(), row.getCell(6).getStringCellValue(), row.getCell(16).getStringCellValue().replace(",", ".")));
					}
				}
				//validando rateios
				for(SolicitacaoPagamento s : rateiosPendentes)
				{
					if(s.isDespesaTotalmenteContabilizada())
					{
						s.setStatus(StatusPagamento.PAGO);
						service.update(s);
						for(Row row: s.getRows())
						{
							writeLine("SUCESSO", bw, row);
						}
					}
					else
					{
						for(Row row: s.getRows())
						{
							writeLine("SOMA DO RATEIO DIVERGENTE", bw, row);
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

    private void tratarReClassificacao(SolicitacaoPagamento solicitacao, DespesaSolicitacaoPagamento d,BigDecimal valor,BufferedWriter bw,Row row) throws IOException
    {
    	if(d.getValor().subtract(valor).compareTo(new BigDecimal(0d)) <0)
		{
			writeLine("Valor de reclassificação é maior que o valor do cover sheet", bw, row);
		}
		else
		{
			solicitacao.setValor( solicitacao.getValor().subtract(valor));
			service.update(solicitacao);
			writeLine("SUCESSO", bw, row);
		}
    }

    private void criarCoverSheet(Fornecedor fornecedor,String numeroNota,BigDecimal valor,CentroCusto centroCusto, Date dataPagamentoRealizado)
    {
    	SolicitacaoPagamento o = new SolicitacaoPagamento(StatusPagamento.PENDENTE_VALIDACAO);
		o.setFornecedor(fornecedor);
		o.setNumeroNotaFiscal(numeroNota);
		o.setOrigem(OrigemSolicitacao.SAP);
		o.setValor(valor);
		o.setDataPagamento(new Date());
		o.setDataPagamentoRealizado(dataPagamentoRealizado);
		o.setCriacao(new Date());
		service.create(o);
		DespesaSolicitacaoPagamento d = new DespesaSolicitacaoPagamento();
		d.setSolicitacaoPagamento(o);
		d.setCentroCusto(centroCusto);
		d.setValor(valor);
		service.create(d);
		o.getDespesas().add(d);
		service.update(o);
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
