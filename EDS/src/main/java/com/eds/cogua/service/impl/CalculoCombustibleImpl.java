package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.CalculoCombustible;
import com.eds.cogua.repository.CalculoCombustibleRepository;
import com.eds.cogua.service.api.CalculoCombustibleServiceAPI;

@Service
public class CalculoCombustibleImpl extends GenericServiceImpl<CalculoCombustible, Long> implements CalculoCombustibleServiceAPI
{
	@Autowired
	private CalculoCombustibleRepository CalculoCombustibleDaoAPI;

	@Override
	public CrudRepository<CalculoCombustible, Long> getDao()
	{
		return CalculoCombustibleDaoAPI;
	}

}