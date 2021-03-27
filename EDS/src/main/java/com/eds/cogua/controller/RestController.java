package com.eds.cogua.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eds.cogua.entity.RegistroNomina;
import com.eds.cogua.service.api.RegistroNominaServiceAPI;

@org.springframework.web.bind.annotation.RestController
public class RestController  
{
	@Autowired
	private RegistroNominaServiceAPI registroNominaService;
	
	@RequestMapping(path="/registrosNominas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<RegistroNomina> registrosNominas()
	{
		return registroNominaService.listar();
	}

<<<<<<< HEAD
}
=======
}
>>>>>>> bbb78430451d4da34b7c5e240cf8c6730d393deb
