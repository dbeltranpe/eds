package com.eds.cogua.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import com.eds.cogua.entity.Authority;
import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.entity.Usuario;
import com.eds.cogua.repository.AuthorityRepository;
import com.eds.cogua.repository.TrabajadorRepository;
import com.eds.cogua.repository.UsuarioRepository;
import com.eds.cogua.service.api.TrabajadorServiceAPI;
import com.eds.cogua.util.CambioPassword;
import com.eds.cogua.util.PassGenerator;

@Controller
public class AppController 
{
	@Autowired
	private UsuarioRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private TrabajadorServiceAPI trabajadorServiceAPI;
	
	@Autowired
	private TrabajadorRepository trabajadorRepository;
	
	private PassGenerator pass = new PassGenerator();
	
	
	@GetMapping({"/", "/login"})
	public String login() 
	{
		return "login-1";
	}
	
	@PostMapping({"/cambioPassword"})
	public String cambioPassword(Model model, @ModelAttribute("cambioPassword") CambioPassword cambioPassword, BindingResult result) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		String toReturn = "";
		if(pass.match(cambioPassword.getPasswordActual(), user.getPassword()))
		{
			user.setPassword(pass.cifrar(cambioPassword.getPasswordNueva()));
			trabajador.setUsuario(user);
			trabajadorRepository.save(trabajador);
			toReturn = "detalleUser?passExito";
		}
		else
		{
			toReturn = "detalleUser?passError";
		}
	
		model.addAttribute("trabajador", trabajador);
		
		return toReturn;
	}
	
	@GetMapping({"/detalleUser"})
	public String detalleUser(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "detalleUser";
	}
	
	@GetMapping({"/admin"})
	public String admin(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "admin";
	}
	
	@GetMapping({"/adminDetalleUser/{id}"})
	public String adminDetalleUser(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		Trabajador userDetalle = trabajadorServiceAPI.obtener(id);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("userDetalle", userDetalle);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "adminDetalleUser";
	}
	
	@PostMapping({"/retirarTrabajador/{id}"})
	public String retirarTrabajador(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		Trabajador userDetalle = trabajadorServiceAPI.obtener(id);
		Usuario usu = userDetalle.getUsuario();
		usu.setEnabled(false);
		userDetalle.setUsuario(usu);
		
		trabajadorRepository.save(userDetalle);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("userDetalle", userDetalle);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "adminDetalleUser";
	}
	
	@GetMapping({"/adminListUser"})
	public String adminListUser(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		model.addAttribute("trabajadorList", trabajadorServiceAPI.listar());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "adminListUser";
	}
	
	@GetMapping({"/adminAdicionTrabajador"})
	public String adminAddUser(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
		Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
		Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
		Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
		
		List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoTrabajador", new Trabajador());
		model.addAttribute("roles", roles);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "adminAddUser";
	}
	

	@PostMapping({"/crear_trabajador"})
	public String crear_trabajador(Model model, @ModelAttribute("nuevoTrabajador") Trabajador nuevoTrabajador, BindingResult result)
	{
		if(result.hasErrors())
		{
			model.addAttribute("nuevoTrabajador", new Trabajador());
			
			Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
			Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
			Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
			Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
			List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
			model.addAttribute("roles", roles);
			
			return "adminAddUser";
		}
		
		Usuario cif = nuevoTrabajador.getUsuario();
		
		if(cif.getUsername()==null || cif.getUsername().trim().equals(""))
		{
			String nombre = nuevoTrabajador.getNombres().trim();
			String[] apellidos = nuevoTrabajador.getApellidos().trim().split(" ");
			cif.setUsername( nombre.charAt(0) + apellidos[0] + apellidos[1].charAt(0));
		}
		
		String passCif = pass.cifrar("Eds2020*");
		cif.setPassword(passCif);
		nuevoTrabajador.setUsuario(cif);
		
		trabajadorServiceAPI.guardar(nuevoTrabajador);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
		Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
		Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
		Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
		
		List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoTrabajador", new Trabajador());
		model.addAttribute("roles", roles);
		
		return "adminAddUser";
	}


}
