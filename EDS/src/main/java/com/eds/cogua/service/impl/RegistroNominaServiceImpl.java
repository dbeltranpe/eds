package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.RegistroNomina;
import com.eds.cogua.repository.RegistroNominaRepository;
import com.eds.cogua.service.api.RegistroNominaServiceAPI;

@Service
public class RegistroNominaServiceImpl extends GenericServiceImpl<RegistroNomina, Long> implements RegistroNominaServiceAPI 
{
	@Autowired
	private RegistroNominaRepository registroNominaDaoAPI;

	@Override
	public CrudRepository<RegistroNomina, Long> getDao() 
	{
		return registroNominaDaoAPI;
	}

<<<<<<< HEAD
}
=======
}
>>>>>>> bbb78430451d4da34b7c5e240cf8c6730d393deb
