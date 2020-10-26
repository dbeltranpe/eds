package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.repository.TrabajadorRepository;
import com.eds.cogua.service.api.TrabajadorServiceAPI;

@Service
public class TrabajadorServiceImpl extends GenericServiceImpl<Trabajador, Long> implements TrabajadorServiceAPI 
{
	@Autowired
	private TrabajadorRepository TrabajadorDaoAPI;

	@Override
	public CrudRepository<Trabajador, Long> getDao() 
	{
		return TrabajadorDaoAPI;
	}

}