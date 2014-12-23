package com.dupont.budget.web.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.primefaces.model.UploadedFile;

import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.service.EventDispatcherService;
import com.dupont.budget.service.event.FileUploadEvent;

/**
 * Ação genérica para o upload e tratamento de arquivos de forma assíncrona.
 * 
 * @author joel
 *
 * @param <T> tipo da entidade que será feito o upload.
 */
public abstract class AsyncFileUploadAction<T extends AbstractEntity<?>> extends GenericAction<T> {

	private static final long serialVersionUID = 4522080601483254313L;
	
	private UploadedFile file;
	
	/**
	 * Efetua a publicação do evento de upload de forma assíncrona e envia as devidas mensagems para o usuário.
	 * @return
	 */
	public String upload() {
		getLogger().debug("Arquivo recebido, iniciando processamento!");

		FileUploadEvent event = new FileUploadEvent(getType());
		try {
			String name = file.getFileName();
			File tmp = File.createTempFile("dupont_", name.substring(name.lastIndexOf(".")));
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(file.getContents());
			fos.close();
			event.setPath(tmp.getAbsolutePath());
		} catch (IOException e) {
			getFacesUtils().addErrorMessage("Ocorreu um erro durante o upload do arquivo, por favor tente novamente.");
			return null;
		}
		
		getFacesUtils().addInfoMessage("Arquivo recebido e enviado para o processamento. Por favor, aguarde e atualize a página em alguns instantes.");
		getEventDispatcher().publish(event);
		
		return "list";
	}
	
	@SuppressWarnings("unchecked")
	protected Class<T> getType() {
		return ((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	protected abstract EventDispatcherService getEventDispatcher();

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
