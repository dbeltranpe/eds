package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.AsignacionTurnos;
import com.eds.cogua.repository.AsignacionTurnosRepository;
import com.eds.cogua.repository.RegistroNominaRepository;
import com.eds.cogua.service.api.AsignacionTurnosServiceAPI;

@Service
public class AsignacionTurnosServiceImpl extends GenericServiceImpl<AsignacionTurnos, Long> implements AsignacionTurnosServiceAPI 
{
	@Autowired
	private AsignacionTurnosRepository asignacionTurnosDaoAPI;
	
	@Override
	public CrudRepository<AsignacionTurnos, Long> getDao() 
	{
		return asignacionTurnosDaoAPI;
	}


}
