package com.eds.cogua.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eds.cogua.entity.RegistroNomina;
import com.eds.cogua.repository.AsignacionTurnosRepository;
import com.eds.cogua.repository.CalculoCombustibleRepository;
import com.eds.cogua.repository.RegistroNominaRepository;
import com.eds.cogua.service.api.RegistroNominaServiceAPI;

import com.eds.cogua.entity.CalculoCombustible;
import com.eds.cogua.service.api.CalculoCombustibleServiceAPI;

@org.springframework.web.bind.annotation.RestController
public class RestController  
{
	@Autowired
	private RegistroNominaServiceAPI registroNominaService;
	
	@Autowired
	private RegistroNominaRepository registroNominaRepository;
	
	@Autowired
	private AsignacionTurnosRepository asignacionTurnosRepository;
	
	@Autowired
	private CalculoCombustibleServiceAPI calculoCombustibleService;
	
	@Autowired
	private CalculoCombustibleRepository calculoCombustibleRepository;
	
	@RequestMapping(path="/registrosNominas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<RegistroNomina> registrosNominas()
	{
		return registroNominaService.listar();
	}
	
	@RequestMapping(path="/registrosTurnos/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> registrosTurnos(@PathVariable(name="id") Long pIdTrabajador)
	{
		List<Map<String, Object>> listToReturn = new ArrayList<>();
		
		List<Object[]> list = asignacionTurnosRepository.turnosTrabajador(pIdTrabajador);
		
		for (int i = 0; i < list.size() ; i++) 
		{
			Map<String, Object> rtn = new LinkedHashMap<>();
			rtn.put("title", list.get(i)[0]);
		    rtn.put("start", list.get(i)[1]);
		    rtn.put("end", list.get(i)[2]);
		    rtn.put("classname", list.get(i)[3]);
		    listToReturn.add(rtn);
		}

	    return listToReturn;
	  
	}
	
	@RequestMapping(path="/pagosNominalesMensuales", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Object[]> pagosNominalesMensuales()
	{
		return registroNominaRepository.pagosNominalesMensuales();
	}
	
	@RequestMapping(path = "/calculosOPACPM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalculoCombustible> calculosOPACPM()
	{
		return calculoCombustibleRepository.combustibleACPM();		
	}
	
	@RequestMapping(path = "/calculosOPGasolina", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalculoCombustible> calculosOPGasolina()
	{
		return calculoCombustibleRepository.combustibleCorriente();		
	}

}