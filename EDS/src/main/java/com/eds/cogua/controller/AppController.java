package com.eds.cogua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.entity.Usuario;
import com.eds.cogua.repository.TrabajadorRepository;
import com.eds.cogua.repository.UsuarioRepository;

@Controller
public class AppController 
{
	@Autowired
	private UsuarioRepository userRepository;
	
	@Autowired
	private TrabajadorRepository trabajadorRepository;
	
	
	
	@GetMapping({"/", "/login"})
	public String login() 
	{
		return "login-1";
	}
	
	@GetMapping({"/admin"})
	public String admin(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		model.addAttribute("trabajador", trabajador);
		return "administrador";
	}

}
