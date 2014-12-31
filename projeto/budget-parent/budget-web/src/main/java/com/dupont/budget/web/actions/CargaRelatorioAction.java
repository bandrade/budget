package com.dupont.budget.web.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import com.dupont.budget.dto.ResultadoCargaDTO;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.service.DeliveryHandlerService;
import com.dupont.budget.service.EventDispatcherService;
import com.dupont.budget.service.event.FileUploadEvent;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de visualização do resultado da utlima carga do relatório 
 * 
 * @author <a href="joel.santos@surittec.com.br">Joel Santos</a>
 * @since 2014
 *
 */
@Named
@ViewScoped
@RolesAllowed(value = "ADMINISTRADOR")
public class CargaRelatorioAction implements Serializable {

	private static final long serialVersionUID = -906126463852854590L;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Inject
	private EventDispatcherService eventDispatcher;
	
	private List<ResultadoCargaDTO> list = new LinkedList<ResultadoCargaDTO>();
	
	private UploadedFile file;
	
	private long readedChars;
	
	public String upload() {
		logger.debug("Arquivo recebido, iniciando processamento!");

		FileUploadEvent event = new FileUploadEvent(SolicitacaoPagamento.class);
		try {
			String name = file.getFileName();
			File tmp = File.createTempFile("dupont_", name.substring(name.lastIndexOf(".")));
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(file.getContents());
			fos.close();
			event.setPath(tmp.getAbsolutePath());
		} catch (IOException e) {
			facesUtils.addErrorMessage("Ocorreu um erro durante o upload do arquivo, por favor tente novamente.");
			return null;
		}
		
		facesUtils.addInfoMessage("Arquivo recebido e enviado para o processamento. Por favor, aguarde a pagina será atualizada automaticamente.");
		eventDispatcher.publish(event);
		
		return "list";
	}
	
	public void updateList() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		final String name = DeliveryHandlerService.RELATORIO_SAP_TEMP_DIR;
		tmpDir = tmpDir.endsWith(String.valueOf(File.separatorChar)) ? tmpDir.concat(name) : tmpDir.concat(String.valueOf(File.separatorChar)).concat(name);
		
		File dir = new File(tmpDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File log = null;
		for (File file: dir.listFiles()) {
			if (log == null || file.lastModified() > log.lastModified()) {
				log = file;
			}
		}
		
		if (log != null) {
			try (BufferedReader br = new BufferedReader(new FileReader(log))) {
				String line = null;
				br.skip(readedChars);
				while ((line = br.readLine()) != null) {
					readedChars += line.length();
					String[] nota = line.split(",");
					ResultadoCargaDTO dto = new ResultadoCargaDTO();
					dto.setNumeroNota(nota[0]);
					dto.setCentroCusto(nota[1]);
					dto.setFornecedor(nota[2]);
					dto.setValor(Double.parseDouble(nota[3]));
					dto.setStatus(nota[4]);
					list.add(dto);
				}
			} catch (IOException e) {
				facesUtils.addErrorMessage("Ocorreu um erro durante a leitura do arquivo de log.");
			}
		} else {
			facesUtils.addWarnMessage("Ainda não foi realizado nenhum upload.");
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<ResultadoCargaDTO> getList() {
		return list;
	}

	public void setList(List<ResultadoCargaDTO> list) {
		this.list = list;
	}
	
	public static void main(String[] args) {
		
	}
}
