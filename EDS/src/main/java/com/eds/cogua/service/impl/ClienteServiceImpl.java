package com.eds.cogua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import com.eds.cogua.commons.GenericServiceImpl;
import com.eds.cogua.entity.Cliente;
import com.eds.cogua.repository.ClienteRepository;
import com.eds.cogua.service.api.ClienteServiceAPI;

@Service
public class ClienteServiceImpl extends GenericServiceImpl<Cliente, Long> implements ClienteServiceAPI
{
	@Autowired
	private ClienteRepository ClienteDaoAPI;
	
	@Override
	public CrudRepository<Cliente, Long> getDao()
	{
		return ClienteDaoAPI;
	}
}
