package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.Tanque;
import com.eds.cogua.repository.TanquesRepository;
import com.eds.cogua.service.api.TanqueServiceAPI;

public class TanqueServiceImpl extends GenericServiceImpl<Tanque, java.lang.Integer> implements TanqueServiceAPI
{
	@Autowired
	private TanquesRepository TanqueDaoAPI;

	@Override
	public CrudRepository<Tanque, Integer> getDao()
	{
		return TanqueDaoAPI;
	}



}