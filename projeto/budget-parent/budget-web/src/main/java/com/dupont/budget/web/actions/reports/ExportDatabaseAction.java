package com.dupont.budget.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.dupont.budget.service.ReportService;

/**
 * Action que retorna um arquivo excel com todos os dados de Solicitacao de pagamento, budget e forecast.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
@Model
public class ExportDatabaseAction {
	
	private StreamedContent file;

	@Inject
	private ReportService reportService;
	
	public StreamedContent getFile(){
		
		byte[] report = reportService.exportDatabase();
		file = new DefaultStreamedContent(new ByteArrayInputStream(report), "application/excel", "budget-alldata-"+new Date().getDate()+".xls");
		
		return file;
	}
}
