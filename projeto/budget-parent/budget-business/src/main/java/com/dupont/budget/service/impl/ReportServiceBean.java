package com.dupont.budget.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.dupont.budget.exception.SystemException;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.service.ReportService;

/**
 * Implementação dos serviços que fornecem os dados de relatorio.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
@Stateless
public class ReportServiceBean implements ReportService {
	
	@PersistenceContext(unitName="budget-pu")
	private EntityManager em;

	@Override
	public byte[] exportDatabase() {
		
		List<SolicitacaoPagamento> solicitacoes = em.createNamedQuery("SolicitacaoPagamento.findAll", SolicitacaoPagamento.class).getResultList();		
		List<Budget> budgets                    = em.createNamedQuery("Budget.findAll"              , Budget.class).getResultList();		
		List<Forecast> forecast                 = em.createNamedQuery("Forecast.findAll"            , Forecast.class).getResultList();
		
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
				
		int rowNumber = 0;
		
		// Criar Cabeçalho de solicitacao de pagamento 
		Row row   = sheet.createRow(rowNumber++);
		
		row.createCell(0).setCellValue("Responsável pelo Envio da Solicitação");
		row.createCell(1).setCellValue("Fornecedor");
		row.createCell(2).setCellValue("Nr da Nota");
		row.createCell(3).setCellValue("Centro de custo");
		row.createCell(4).setCellValue("Descrição do Centro de custo");
		row.createCell(5).setCellValue("Produto");
		row.createCell(6).setCellValue("Tipo de despesa");
		row.createCell(7).setCellValue("Ação");
		row.createCell(8).setCellValue("Cultura");
		row.createCell(9).setCellValue("Distrito");
		row.createCell(10).setCellValue("ERC/CCE");
		row.createCell(11).setCellValue("Valor");
				
		
		for (SolicitacaoPagamento solicitacao : solicitacoes) {		
			
			Set<DespesaSolicitacaoPagamento> despesas = solicitacao.getDespesas();
			
			if( despesas == null || despesas.isEmpty() )
				continue;
			
			row = sheet.createRow(rowNumber++);
			
			for (DespesaSolicitacaoPagamento despesa : despesas) {
				row.createCell(0).setCellValue(solicitacao.getUsuarioCriador().getNome()); // Responsavel
				row.createCell(1).setCellValue(solicitacao.getFornecedor().getNome()); // Fornecedor
				row.createCell(2).setCellValue(solicitacao.getNumeroNotaFiscal()); // nr nota fiscal
				row.createCell(3).setCellValue(despesa.getCentroCusto().getCodigo()); // centro de custo - codigo
				row.createCell(4).setCellValue(despesa.getCentroCusto().getNome()); // centro de custo - descricao
				row.createCell(5).setCellValue(despesa.getProduto().getNome()); // produto
				row.createCell(6).setCellValue(despesa.getTipoDespesa().getNome());  // tipo de despesa
				row.createCell(7).setCellValue(despesa.getAcao().getNome()); // Ação
				row.createCell(8).setCellValue(despesa.getCultura().getNome()); // cultura
				row.createCell(9).setCellValue(despesa.getDistrito().getNome()); // distrito
				row.createCell(10).setCellValue(despesa.getVendedor()==null? "":despesa.getVendedor().getNome()); // ERC/CCE
				row.createCell(11).setCellValue(despesa.getValor()); // valor
			}
			
			
		}
		
		
		// Criar cabeçalho de Budget
		rowNumber += 2;
		row = sheet.createRow(rowNumber);
		
		row.createCell(0).setCellValue("Area");
		row.createCell(1).setCellValue("Responsável pelo dentro de custo");
		row.createCell(2).setCellValue("Centro de custo");
		row.createCell(3).setCellValue("Descrição do Centro de Custo");
		row.createCell(4).setCellValue("Tipo de Despesa");
		row.createCell(5).setCellValue("Ação");
		row.createCell(6).setCellValue("Produto");
		row.createCell(7).setCellValue("Cultura");
		row.createCell(8).setCellValue("Distrito");
		row.createCell(9).setCellValue("ERC");
		row.createCell(10).setCellValue("Cliente");
		row.createCell(11).setCellValue("Mês");
		row.createCell(12).setCellValue("Budget Proposto");
		row.createCell(13).setCellValue("Budget Aprovado");
		row.createCell(14).setCellValue("Comentário");
		
		for (Budget budget : budgets) {
			
			Set<Despesa> despesas = budget.getDespesas();
			
			if( despesas == null || despesas.isEmpty() )
				continue;
			
			for (Despesa despesa : despesas) {
				
				row.createCell(0);
			}
			
		}
		
		// Criar cabeçalho de Forecast
		rowNumber += 2;
		row = sheet.createRow(rowNumber);
		
		row.createCell(0).setCellValue("Area");
		row.createCell(1).setCellValue("Responsável pelo centro de custo");
		row.createCell(2).setCellValue("Centro de Custo");
		row.createCell(3).setCellValue("Descrição do Centro de Custo");
		row.createCell(4).setCellValue("Tipo de Despesa");
		row.createCell(5).setCellValue("Ação");
		row.createCell(6).setCellValue("Produto");
		row.createCell(7).setCellValue("Cultura");
		row.createCell(8).setCellValue("Distrito");
		row.createCell(9).setCellValue("ERC");
		row.createCell(10).setCellValue("Cliente");
		row.createCell(11).setCellValue("Mês");
		row.createCell(12).setCellValue("Valor Forecast ou Gasto Efetivo");
		row.createCell(13).setCellValue("Valor Comprometido");
		
		// Criar resposta
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			wb.write(bos);
		} catch (IOException e) {
			throw new SystemException("ERRO ao gravar o ByteArrayOutputStream.");
		}
		
		return bos.toByteArray();
	}

}
