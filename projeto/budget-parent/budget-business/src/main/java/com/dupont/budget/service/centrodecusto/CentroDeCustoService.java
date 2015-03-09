package com.dupont.budget.service.centrodecusto;

import java.util.List;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;

public interface CentroDeCustoService {
	CentroDeCustoDTO[] obterCentrosDeCusto();
	List<CentroCusto> findByArea(Long areaId) throws Exception;
	public CentroDeCustoDTO parseCentroCusto(CentroCusto cc);
	public Area findAreaByCC(Long ccId) throws Exception;
	public List<CentroCusto> obterCCList();

}
