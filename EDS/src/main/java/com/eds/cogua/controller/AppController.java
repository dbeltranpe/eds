package com.eds.cogua.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.eds.cogua.entity.AsignacionTurnos;
import com.eds.cogua.entity.Authority;
import com.eds.cogua.entity.Cliente;
import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.entity.RegistroNomina;
import com.eds.cogua.entity.Usuario;
import com.eds.cogua.repository.AsignacionTurnosRepository;
import com.eds.cogua.repository.AuthorityRepository;
import com.eds.cogua.repository.ClienteRepository;
import com.eds.cogua.repository.TrabajadorRepository;
import com.eds.cogua.repository.UsuarioRepository;
import com.eds.cogua.repository.RegistroNominaRepository;
import com.eds.cogua.service.api.AsignacionTurnosServiceAPI;
import com.eds.cogua.service.api.ClienteServiceAPI;
import com.eds.cogua.service.api.TrabajadorServiceAPI;
import com.eds.cogua.util.CambioPassword;
import com.eds.cogua.util.PDF;
import com.eds.cogua.util.EnvioCorreos;
import com.eds.cogua.util.LeerArchivoClientesExcel;
import com.eds.cogua.util.PassGenerator;

import com.eds.cogua.entity.CalculoCombustible;
import com.eds.cogua.repository.CalculoCombustibleRepository;
import com.eds.cogua.repository.TanquesRepository;
import com.eds.cogua.service.api.CalculoCombustibleServiceAPI;

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

	@Autowired
	private ClienteRepository clienteReposiory;
	
	@Autowired
	private RegistroNominaRepository registroNominaRepository;
	
	@Autowired
	private AsignacionTurnosRepository asignacionTurnosRepository;

	@Autowired
	private ClienteServiceAPI clienteServiceAPI;
	
	@Autowired
	private AsignacionTurnosServiceAPI asignacionTurnosServiceAPI;
	
	@Autowired
	private EnvioCorreos envioCorreos;
	
	@Autowired
	private CalculoCombustibleServiceAPI calculoCombustibleServiceAPI;

	@Autowired
	private CalculoCombustibleRepository calculoCombustibleRepository;

	@Autowired
	private TanquesRepository tanquesRepository;

	private PassGenerator pass = new PassGenerator();


	@GetMapping({"/", "/login"})
	public String login() 
	{
		return "login-1";
	}
	
	@GetMapping({"/error"})
	public String error() 
	{
		return "error";
	}
	
	@GetMapping({"/privacidad"})
	public String privacidad(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "privacidad";
	}
	
	@GetMapping({"/inicio"})
	public String inicio(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "inicio";
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

		model.addAttribute("resumenNomina", registroNominaRepository.pagosNominalesMensuales());
		
		model.addAttribute("logTrabajador", trabajadorRepository.logTrabajador());
		model.addAttribute("logNomina", registroNominaRepository.logNomina());
		model.addAttribute("logClientes", clienteReposiory.logClientes());
		
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
	
	@GetMapping({"/adminEditarTrabajador/{id}"})
	public String adminEditarTrabajador(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
		Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
		Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
		Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");

		List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
		
		Trabajador nuevoTrabajador = trabajadorServiceAPI.obtener(id);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoTrabajador", nuevoTrabajador);
		model.addAttribute("roles", roles);
		model.addAttribute("cambioPassword", new CambioPassword());

		return "adminAddUser";
	}


	@PostMapping({"/crear_trabajador"})
	public String crear_trabajador(Model model, @ModelAttribute("nuevoTrabajador") Trabajador nuevoTrabajador, BindingResult result)
	{
		System.out.println(nuevoTrabajador.getNominaTrabajador().getSalarioBase());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		if(result.hasErrors())
		{
			model.addAttribute("nuevoTrabajador", new Trabajador());

			Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
			Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
			Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
			Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
			List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
			model.addAttribute("roles", roles);
			model.addAttribute("cambioPassword", new CambioPassword());

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
		nuevoTrabajador.setCdUsuCre(auth.getName());
		nuevoTrabajador.setCdUsuMo(auth.getName());

		trabajadorServiceAPI.guardar(nuevoTrabajador);

		Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
		Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
		Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
		Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");

		List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoTrabajador", new Trabajador());
		model.addAttribute("roles", roles);
		model.addAttribute("cambioPassword", new CambioPassword());

		return "redirect:/adminAdicionTrabajador";
	}

	@GetMapping({"/clientList"})
	public String listarClientes(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		model.addAttribute("clientesList", clienteServiceAPI.listar());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());

		return "/clientList";
	}

	@GetMapping({"/adicionCliente"})
	public String addClient(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoCliente", new Cliente());
		model.addAttribute("cambioPassword", new CambioPassword());

		return "addClient";
	}


	@PostMapping({"/crear_cliente"})
	public String crear_cliente(Model model, @ModelAttribute("nuevoCliente") Cliente nuevoCliente, BindingResult result)
	{
		if(result.hasErrors())
		{
			Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
			Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
			Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
			Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
			List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
			
			model.addAttribute("roles", roles);
			model.addAttribute("cambioPassword", new CambioPassword());
			
			return "addClient";
		}

		if(!clienteReposiory.findByIdentificacion(nuevoCliente.getIdentificacion()).isEmpty())
		{			
			return "addClient";
		}
		else
		{
			clienteServiceAPI.guardar(nuevoCliente);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
		Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
		Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
		Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");

		List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("nuevoCliente", new Cliente());
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("roles", roles);

		return "addClient";
	}

	@GetMapping({"/detalleClient/{id}"})
	public String detalleClient(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		Cliente detalleCliente = clienteServiceAPI.obtener(id);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("detalleCliente", detalleCliente);
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("idCliente", detalleCliente.getId());

		return "detalleClient";
	}

	@PostMapping({"/actualizar_Cliente/{id}"})
	public String actualizarCliente(Model model, @ModelAttribute("detalleCliente") Cliente clienteActulizado, @PathVariable(name="id") Long idCliente) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		Cliente clienteAntiguo = clienteReposiory.findById(idCliente).get();

		clienteAntiguo.setNombres(clienteActulizado.getNombres());
		clienteAntiguo.setApellidos(clienteActulizado.getApellidos());
		clienteAntiguo.setIdentificacion(clienteActulizado.getIdentificacion());
		clienteAntiguo.setCorreo(clienteActulizado.getCorreo());
		clienteAntiguo.setFechaSOAT(clienteActulizado.getFechaSOAT());

		clienteReposiory.save(clienteAntiguo);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());

		return "redirect:/detalleClient/" + idCliente;
	}

	@PostMapping({"/importacionDatos"})
	public String cargarClientes(Model model, @RequestParam("file") MultipartFile reapExcelDataFile)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());

		LeerArchivoClientesExcel lectorExcel = new LeerArchivoClientesExcel();

		List<Cliente> clientesCargados = lectorExcel.leerExcel(reapExcelDataFile);

		for (int i = 0; i < clientesCargados.size(); i++) 
		{
			clienteServiceAPI.guardar(clientesCargados.get(i));
		}

		return "/clientList";
	}
	
	@GetMapping({"/envioCorreo/{nombres}/{apellidos}/{correo}/{fechaSOAT}/{entrada}"})
	public String envioCorreoCliente(Model model, @PathVariable(name="nombres") String nombres, @PathVariable(name="apellidos") String apellidos, @PathVariable(name="correo") String correo, @PathVariable(name="fechaSOAT") String fechaSOAT, @PathVariable(name="entrada") int entrada) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		envioCorreos.sendEmail(correo, nombres + apellidos, fechaSOAT);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		String rta = "";
		
//		if(entrada == 1)
//		{
//			rta = "admin";
//		}
//		else if(entrada == 2)
//		{
//			rta = "dashBoardClientes";
//		}

		return "/clientList";
	}
	
	@GetMapping({"/envioCorreoNominaTrabajador/{nombres}/{apellidos}/{correo}/{id}"})
	public String envioCorreoNominaTrabajador(Model model, @PathVariable(name="nombres") String nombres, @PathVariable(name="apellidos") String apellidos, @PathVariable(name="correo") String correo, @PathVariable(name="id") Long id)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		try
		{
			PDF p = new PDF();
			byte [] pdf = null;
			pdf = p.pdfDesprendibleNomina(registroNominaRepository.findById(id).get());
			envioCorreos.sendEmail2(correo, nombres + apellidos, pdf);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "adminConsultaNomina";
	}
	
	@GetMapping({"/eliminarClient/{id}"})
	public String Eliminar(Model model, @PathVariable(name="id") Long idCliente)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		clienteServiceAPI.eliminar(idCliente);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "admin";
	}
	
	@GetMapping({"/dashBoardClientes"})
	public String dashBoardClientes(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		LocalDate date =  LocalDate.now();
		LocalDate semana = date.plusDays(5);
		LocalDate mes =  date.plusMonths(1);
		
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("clientesSemana", clienteReposiory.obtenerClientesPendientesSOATSemana(date.toString(), semana.toString()));
		model.addAttribute("clientesMes", clienteReposiory.obtenerClientesPendientesSOATMes(date.toString(), mes.toString()));
		model.addAttribute("listaTotalClientesMes", clienteReposiory.cantidadClientesPorMes("2021"));
		model.addAttribute("clientesTotalAnio", clienteReposiory.cantidadClientesPorAnio());
		model.addAttribute("totalClientes", clienteReposiory.totalClientes());
		
		return "dashBoardClientes";
	}
	
	@GetMapping({"/adminCalculoNomina"})
	public String adminCalculoNomina(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		 
		model.addAttribute("registroNomina", new RegistroNomina());
		
		model.addAttribute("trabajadorList", trabajadorServiceAPI.listar());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "adminCalculoNomina";
	}
	
	@PostMapping({"/generarNomina"})
	public String generarNomina(Model model, @ModelAttribute("registroNomina") RegistroNomina registroNomina, BindingResult result)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		int pDiasTrabajados = registroNomina.getNroDiasTrabajadores();
		LocalDate date = LocalDate.parse(LocalDateTime.now().getYear() + "-" + String.format("%02d", registroNomina.getNumMes()) + "-01");
		Date pFechas = java.sql.Date.valueOf(date);
		float pAporteCop = registroNomina.getVrAporteCoop();
		float pCreditoCop = registroNomina.getVrCreditoAhorro();
		float pPolExequial = registroNomina.getVrPolExequial();
		float pDescuadres = registroNomina.getVrDescuadre();
		int pIdTrabajador = Math.toIntExact(registroNomina.getTrabajador().getId());
		
		registroNominaRepository.spNominaTrabajador(pDiasTrabajados, pFechas, pAporteCop, pCreditoCop, pPolExequial, pDescuadres, pIdTrabajador, user.getUsername());
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		Format formatter = new SimpleDateFormat("ddMMyyyy");
		String fec = formatter.format(pFechas);
		
		//return "nominaTrabajador";
		return "redirect:/nominaTrabajador/" + pIdTrabajador + "/" + fec;
	}
	
	@GetMapping({"/nominaTrabajador/{id}/{fecha}"})
	public String nominaTrabajador(Model model, @PathVariable(name="id") Long id, @PathVariable("fecha")@DateTimeFormat(pattern = "ddMMyyyy") Date fecha) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		Trabajador userDetalle = trabajadorServiceAPI.obtener(id);
		RegistroNomina registroNomina = registroNominaRepository.obtenerNominaMes(id, fecha);
		
		model.addAttribute("registroNomina", registroNomina);
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("userDetalle", userDetalle);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "nominaTrabajador";
	}
	
	@GetMapping({"/nominaTrabajadorId/{id}"})
	public String nominaTrabajadorId(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		RegistroNomina registroNomina = registroNominaRepository.findById(id).get();
		Trabajador userDetalle = registroNomina.getTrabajador();
		
		model.addAttribute("registroNomina", registroNomina);
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("userDetalle", userDetalle);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "nominaTrabajador";
	}
	
	@GetMapping({"/adminConsultaNomina"})
	public String adminConsultaNomina(Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "adminConsultaNomina";
	}
	
	@GetMapping({"/horarioTrabajador/{id}"})
	public String horarioTrabajador(Model model, @PathVariable(name="id") Long id) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
	
		AsignacionTurnos asignacionTurno = new AsignacionTurnos();
		model.addAttribute("asignacionTurno",asignacionTurno);
		
		Trabajador trabajadorActual = trabajadorServiceAPI.obtener(id);
		
		model.addAttribute("trabajadorActual", trabajadorActual);
		model.addAttribute("trabajadorTurno", id);
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "horarioTrabajador";
	}
	
	@PostMapping({"/adicionTurno/{id}"})
	public String adicionTurno(Model model, @ModelAttribute("asignacionTurno") AsignacionTurnos asignacionTurno, @PathVariable(name="id") Long pIdTrabajador)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		asignacionTurno.setTrabajador(trabajadorRepository.findById(pIdTrabajador).get());
		Date fecha = asignacionTurno.getFecha();
		Calendar c = Calendar.getInstance();
	    c.setTime(fecha);
	    
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    
	    boolean festivo = asignacionTurnosRepository.festivo(dateFormat.format(fecha)) > 0? true: false;
	    Date newDate = DateUtils.addDays(fecha, 1);
	    boolean festivoNextDay = asignacionTurnosRepository.festivo(dateFormat.format(newDate)) > 0? true: false;
		
		if(asignacionTurno.getIdTurno() == 1)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				asignacionTurno.setIdTurno(2);
			}
			else if(festivo)
			{
				asignacionTurno.setIdTurno(3);
			}
			else
			{
				asignacionTurno.setIdTurno(1);
			}
		}
		else if(asignacionTurno.getIdTurno() == 2)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				asignacionTurno.setIdTurno(5);
			}
			else if(festivo)
			{
				asignacionTurno.setIdTurno(6);
			}
			else
			{
				asignacionTurno.setIdTurno(4);
			}
			
		}
		else if(asignacionTurno.getIdTurno() == 3)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				asignacionTurno.setIdTurno(8);
			}
			else if(festivo)
			{
				asignacionTurno.setIdTurno(11);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == true)
			{
				asignacionTurno.setIdTurno(10);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == false)
			{
				asignacionTurno.setIdTurno(9);
			}
			else
			{
				asignacionTurno.setIdTurno(7);
			}
			
		}
		else if(asignacionTurno.getIdTurno() == 12)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				asignacionTurno.setIdTurno(13);
			}
			else if(festivo)
			{
				asignacionTurno.setIdTurno(14);
			}
			else
			{
				asignacionTurno.setIdTurno(12);
			}
		}
		else if(asignacionTurno.getIdTurno() == 13)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				asignacionTurno.setIdTurno(16);
			}
			else if(festivo)
			{
				asignacionTurno.setIdTurno(19);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == true)
			{
				asignacionTurno.setIdTurno(18);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == false)
			{
				asignacionTurno.setIdTurno(17);
			}
			else
			{
				asignacionTurno.setIdTurno(15);
			}
		}
		else if(asignacionTurno.getIdTurno() == 16)
		{
			asignacionTurno.setIdTurno(20);
		}
		else if(asignacionTurno.getIdTurno() == 120)
		{
			if((c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || festivo)
			{
				asignacionTurno.setIdTurno(23);
			}
			else
			{
				asignacionTurno.setIdTurno(22);
			}
		}
		else if(asignacionTurno.getIdTurno() == 130)
		{
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				asignacionTurno.setIdTurno(25);
			}
			else if(festivo == true && festivoNextDay == false )
			{
				asignacionTurno.setIdTurno(28);
			}
			else if(festivo == true && festivoNextDay == true )
			{
				asignacionTurno.setIdTurno(29);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == true)
			{
				asignacionTurno.setIdTurno(27);
			}
			else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && festivoNextDay == false)
			{
				asignacionTurno.setIdTurno(26);
			}
			else
			{
				asignacionTurno.setIdTurno(24);
			}
		}
		else if(asignacionTurno.getIdTurno() == 0)
		{
			asignacionTurno.setIdTurno(21);
		}
		
		asignacionTurno.setCdUsuCre(auth.getName());
		asignacionTurno.setCdUsuMo(auth.getName());
		asignacionTurnosServiceAPI.guardar(asignacionTurno);
		
		Trabajador trabajadorActual = trabajadorServiceAPI.obtener(pIdTrabajador);
		model.addAttribute("trabajadorActual", trabajadorActual);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "redirect:/horarioTrabajador/" + pIdTrabajador ;
	}
	
	@PostMapping({"/limpiarTurno/{id}"})
	public String limpiarTurno(Model model, @ModelAttribute("asignacionTurno") AsignacionTurnos asignacionTurno, @PathVariable(name="id") Long pIdTrabajador)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		asignacionTurno.setTrabajador(trabajadorRepository.findById(pIdTrabajador).get());
		Date fecha = asignacionTurno.getFecha();
		Calendar c = Calendar.getInstance();
	    c.setTime(fecha);
	    
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    asignacionTurnosRepository.eliminarTurnosPorFecha(dateFormat.format(fecha), pIdTrabajador);
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "redirect:/horarioTrabajador/" + pIdTrabajador ;
	}
	
	@GetMapping({"/pdf/{id}"})
	public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response, @PathVariable(name="id") Long id) throws IOException 
	{	
		PDF p = new PDF();
		byte[] bytes = null;
		try {
			bytes = p.pdfDesprendibleNomina(registroNominaRepository.findById(id).get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
	}
	
	
	@GetMapping({"/reporteOP"})
    public String reporteOP(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userRepository.findByUsername(auth.getName()).get();
        Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

        int[] reporte = new int[3];
        
        model.addAttribute("reporte", reporte);
        
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("cambioPassword", new CambioPassword());

        return "reporteOP";
    }
	
	@GetMapping({"/reporteOPPDF/{anio}/{mes}/{tanque}"})
	public ResponseEntity<?> reporteOPPDF(HttpServletRequest request, HttpServletResponse response, @PathVariable(name="anio") int anio, 
			@PathVariable(name="mes") int mes, @PathVariable(name="tanque") int tanque) throws IOException 
	{	
		PDF p = new PDF();
		List<CalculoCombustible> list = null;
		
		if(tanque == 1)
		{
			list = calculoCombustibleRepository.listarGasolinaxMesxAnio(anio, mes);
		}
		else
		{
			list = calculoCombustibleRepository.listarACPMxMesxAnio(anio, mes);
		}
		
		byte[] bytes = null;
		try 
		{
			bytes = p.pdfOP(anio,mes,tanque,list);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
	}
	
	@GetMapping({"/calculoOP"})
	public String gestionCombustible(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		model.addAttribute("nuevoCalculoCombustible", new CalculoCombustible());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "calculoOP";
	}
	
	public double calcularVentaTotal(double combutibleRecibido, double mediaAnterior, double medidaActual) 
	{
		double rta = combutibleRecibido + mediaAnterior - medidaActual;
		rta = Math.round(rta * Math.pow(10, 2)) / Math.pow(10, 2);
		
		return rta;
	}
	
	public double calcularInventarioLibros(double medidaAnteriorGalones, double combustibleActualRecibido , double cantidadActualVendida)
	{
		double rta = medidaAnteriorGalones + combustibleActualRecibido - cantidadActualVendida;
		rta = Math.round(rta * Math.pow(10, 2)) / Math.pow(10, 2);
		
		return rta;
	}
	
	public double calcularDiferencia(double medidaActualGalones, double calculoInventarioLibros)
	{
		double rta = medidaActualGalones - calculoInventarioLibros;
		rta = Math.round(rta * Math.pow(10, 2)) / Math.pow(10, 2);
		
		return rta;
	}
	
	public int numeroConsultaBaseDatos(int numeroCompleto, int numTanque) 
	{
		int modulo = 0;
		int numeroConsultaDB = 0;
		
		if(numTanque == 1) 
		{
			modulo = numeroCompleto % 10;
			numeroConsultaDB = 0;
			
			if(numeroCompleto % 10 != 0)
			{
				int faltante = 10 - modulo;
				numeroConsultaDB = numeroCompleto + faltante;
			}
			else
			{
				numeroConsultaDB = numeroCompleto;
			}
			
			if(numeroConsultaDB > 2577)
			{
				numeroConsultaDB = 2577;
			}
		}
		else if(numTanque == 2) 
		{
			modulo = numeroCompleto % 10;
			numeroConsultaDB = 0;
			
			if(numeroCompleto % 10 != 0)
			{
				int faltante = 10 - modulo;
				numeroConsultaDB = numeroCompleto + faltante;
			}
			else
			{
				numeroConsultaDB = numeroCompleto;
			}
			
			if(numeroConsultaDB > 2575)
			{
				numeroConsultaDB = 2575;
			}
		}
		
		return numeroConsultaDB;
	}
	
	public Double[] calculoCombustible(double medida1, double medida2, double medida3) 
	{
		String medidaTanque1 = medida1 + "";
		medidaTanque1.trim();
		String medidaTanque2 = medida2 + "";
		medidaTanque2.trim();
		String medidaTanque3 = medida3 + "";
		medidaTanque2.trim();
		
		Double[] rta = new Double[2];
		
		char [] medidaTanque1BD = medidaTanque1.toCharArray();
		char [] medidaTanque2BD = medidaTanque2.toCharArray();
		char [] medidaTanque3BD = medidaTanque3.toCharArray();
		
		String punto = ".";
		char charPunto = punto.charAt(0);
		
		List<Object[]> galon = null;
		List<Object[]> galonT3 = null;
		double combustibleActual = 0;
		
		if(!medidaTanque1.isEmpty() && Double.parseDouble(medidaTanque1) != 0) 
		{
			String sNumCompleto = "";
			for (int i = 0; i < medidaTanque1BD.length; i++) 
			{
				if(medidaTanque1BD[i] != charPunto)
				{
					sNumCompleto += medidaTanque1BD[i];
				}
			}
			
			int numeroCompleto = Integer.parseInt(sNumCompleto);
			int numeroConsultaDB = numeroConsultaBaseDatos(numeroCompleto, 1);
			
			galon = tanquesRepository.galonAltutaTanque1(numeroConsultaDB);
			
			combustibleActual = ((int) galon.get(0)[0] * medida1) / (numeroConsultaDB/10);
			
			combustibleActual = Math.round(combustibleActual * Math.pow(10, 2)) / Math.pow(10, 2);
			
			rta[0] = combustibleActual;
			rta[1] = 1.0;
		}
		else
		{
			String sNumCompleto1 = "";
			String sNumCompleto2 = "";
			for (int i = 0; i < medidaTanque2BD.length; i++) 
			{
				if(medidaTanque2BD[i] != charPunto)
				{
					sNumCompleto1 += medidaTanque2BD[i];
				}
			}
			
			for (int i = 0; i < medidaTanque3BD.length; i++) 
			{
				if(medidaTanque3BD[i] != charPunto)
				{
					sNumCompleto2 += medidaTanque3BD[i];
				}
			}
			
			int numeroCompleto1 = Integer.parseInt(sNumCompleto1);
			int numeroCompleto2 = Integer.parseInt(sNumCompleto2);
			
			int numeroConsultaDB1 = numeroConsultaBaseDatos(numeroCompleto1, 2);
			int numeroConsultaDB2 = numeroConsultaBaseDatos(numeroCompleto2, 2);
			
			galon = tanquesRepository.galonAltutaTanque2(numeroConsultaDB1);
			galonT3 = tanquesRepository.galonAltutaTanque3(numeroConsultaDB2);
			
			combustibleActual = ((int) galon.get(0)[0] * medida2) / (numeroConsultaDB1/10);
			combustibleActual += ((int) galonT3.get(0)[0] * medida3) / (numeroConsultaDB2/10);
			
			combustibleActual = Math.round(combustibleActual * Math.pow(10, 2)) / Math.pow(10, 2);
			
			rta[0] = combustibleActual;
			rta[1] = 2.0;
		}
		
		return rta;
	}
	
	@PostMapping({"/calcularCombustible"})
	public String calcularCombustible(Model model, @ModelAttribute("nuevoCalculoCombustible") CalculoCombustible nuevocalculoCombustible)
	{

		List<CalculoCombustible> registroAnterior = null;
		
		double combustibleActual = 0;
		double inventarioLibros = 0;
		double diferencia = 0;
		double totalVenta = 0;
		List<CalculoCombustible> listaCalculos = null;
		
		Double[] rta = calculoCombustible(nuevocalculoCombustible.getMedidaTanque1(), nuevocalculoCombustible.getMedidaTanque2(), nuevocalculoCombustible.getMedidaTanque3());
		
		if(nuevocalculoCombustible.getFecha().isEmpty()) 
		{
			nuevocalculoCombustible.setFecha(LocalDate.now().toString());
		}
		
		if(rta[1] == 1.0)
		{
			combustibleActual = rta[0];
			nuevocalculoCombustible.setMedidaFinalGalones(combustibleActual);			
			registroAnterior = calculoCombustibleRepository.calculoAnteriorAFechaCorrienteNuevo(nuevocalculoCombustible.getFecha());
			listaCalculos = calculoCombustibleRepository.listaCombustibleCorrienteDownNuevo(nuevocalculoCombustible.getFecha());
		}
		else
		{
			combustibleActual = rta[0];
			nuevocalculoCombustible.setMedidaFinalGalones(combustibleActual);			
			registroAnterior = calculoCombustibleRepository.calculoAnteriorAFechaACPMNuevo(nuevocalculoCombustible.getFecha());
			listaCalculos = calculoCombustibleRepository.listaCombustibleACPMDownNuevo(nuevocalculoCombustible.getFecha());
		}
		
		if(!registroAnterior.isEmpty())
		{
			inventarioLibros = calcularInventarioLibros(registroAnterior.get(0).getMedidaFinalGalones(), nuevocalculoCombustible.getCombustibleRecibido(), nuevocalculoCombustible.getCantidadVendida());
			nuevocalculoCombustible.setIventarioLibros(inventarioLibros);
			
			diferencia = calcularDiferencia(combustibleActual, inventarioLibros);
			nuevocalculoCombustible.setDiferencia(diferencia);
			
			nuevocalculoCombustible.setMedidaAnterior(registroAnterior.get(0).getMedidaFinalGalones());
			
			totalVenta = calcularVentaTotal(nuevocalculoCombustible.getCombustibleRecibido(), registroAnterior.get(0).getMedidaFinalGalones(), combustibleActual);
			nuevocalculoCombustible.setTotalVendido(totalVenta);
		}
		else
		{
			inventarioLibros = nuevocalculoCombustible.getCantidadVendida();
			inventarioLibros = Math.round(inventarioLibros * Math.pow(10, 2)) / Math.pow(10, 2);
			nuevocalculoCombustible.setIventarioLibros(inventarioLibros);
			
			diferencia = calcularDiferencia(combustibleActual, inventarioLibros);
			nuevocalculoCombustible.setDiferencia(diferencia);
			
			nuevocalculoCombustible.setMedidaAnterior(0);
			
			totalVenta = calcularVentaTotal(nuevocalculoCombustible.getCombustibleRecibido(), 0, nuevocalculoCombustible.getMedidaFinalGalones());
			
			nuevocalculoCombustible.setTotalVendido(0);
		}
		
		if(!listaCalculos.isEmpty())
		{
			actualizarOPDown(nuevocalculoCombustible.getMedidaFinalGalones(), listaCalculos);
		}
		
		calculoCombustibleServiceAPI.guardar(nuevocalculoCombustible);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("nuevoCalculoCombustible", new CalculoCombustible());
		
		return "/calculoOP";
	}
	
	@GetMapping({"/eliminarOP/{id}"})
	public String eliminarOP(Model model, @PathVariable(name="id") Long id)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		CalculoCombustible cc = calculoCombustibleRepository.findById(id).get();
		
		if(cc.getMedidaTanque1() > 0)
		{
			calculoCombustibleRepository.eliminarDatosCorriente(cc.getFecha());
		}
		else
		{
			calculoCombustibleRepository.eliminarDatosACPM(cc.getFecha());
		}
		
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("nuevoCalculoCombustible", new CalculoCombustible());

		
		return "/calculoOP";
	}
	
	@GetMapping({"/detalleOP/{id}"})
	public String detalleOP(Model model, @PathVariable(name="id") Long id)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		CalculoCombustible detalleOP = calculoCombustibleServiceAPI.obtener(id);

		model.addAttribute("trabajador", trabajador);
		model.addAttribute("detalleOP", detalleOP);
		model.addAttribute("cambioPassword", new CambioPassword());
		model.addAttribute("idOP", detalleOP.getId());
		
		return "/detalleOP";
	}
	
	public void actualizarOPDown(double medidaAnteriorGalones, List<CalculoCombustible> lista)
	{
		double galonAnterior = medidaAnteriorGalones;
		
		for (CalculoCombustible calculoCombustible : lista) 
		{
			double totalVenta = calcularVentaTotal(calculoCombustible.getCombustibleRecibido(), galonAnterior, calculoCombustible.getMedidaFinalGalones());
			double inventarioLibros = calcularInventarioLibros(galonAnterior, calculoCombustible.getCombustibleRecibido(), calculoCombustible.getCantidadVendida());		
			double diferencia = calcularDiferencia(calculoCombustible.getMedidaFinalGalones(), inventarioLibros);
			
			calculoCombustible.setIventarioLibros(inventarioLibros);
			calculoCombustible.setDiferencia(diferencia);
			calculoCombustible.setTotalVendido(totalVenta);
			calculoCombustible.setMedidaAnterior(galonAnterior);
			
			galonAnterior = calculoCombustible.getMedidaFinalGalones();
			calculoCombustibleRepository.save(calculoCombustible);
		}
	}
	
	public void actualizarOPUp(double medidaAnteriorGalonesAnterior, List<CalculoCombustible> lista)
	{
		double galonAnterior = medidaAnteriorGalonesAnterior;
		
		for (CalculoCombustible calculoCombustible : lista) 
		{			
			double totalVenta = calcularVentaTotal(calculoCombustible.getCombustibleRecibido(), galonAnterior, calculoCombustible.getMedidaFinalGalones());
			double inventarioLibros = calcularInventarioLibros(galonAnterior, calculoCombustible.getCombustibleRecibido(), calculoCombustible.getCantidadVendida());		
			double diferencia = calcularDiferencia(calculoCombustible.getMedidaFinalGalones(), inventarioLibros);
			
			calculoCombustible.setIventarioLibros(inventarioLibros);
			calculoCombustible.setDiferencia(diferencia);
			calculoCombustible.setMedidaAnterior(galonAnterior);
			calculoCombustible.setTotalVendido(totalVenta);
			
			galonAnterior = calculoCombustible.getMedidaFinalGalones();
			calculoCombustibleRepository.save(calculoCombustible);
		}
	}
	
	@PostMapping({"/actualizar_OP/{id}"})
	public String actualizarOP(Model model, @ModelAttribute("detalleOP") CalculoCombustible opActulizado, @PathVariable(name="id") Long idOP)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();

		CalculoCombustible calculoOPAntiguo = calculoCombustibleRepository.findById(idOP).get();
		
		double totalVenta = 0;
		double inventarioLibros = 0;
		double diferencia = 0;
		Double[] rta = null;
		List<CalculoCombustible> listaCombustibleDown = null;
		List<CalculoCombustible> listaCombustibleUp = null;
		
		if(!calculoOPAntiguo.getFecha().equals(opActulizado.getFecha()))
		{	
			if(opActulizado.getMedidaTanque1() != 0)
			{
				List<CalculoCombustible> calculoAFecha = calculoCombustibleRepository.calculoAnteriorAFechaCorriente(opActulizado.getFecha(), calculoOPAntiguo.getId());
				
				if(!calculoAFecha.isEmpty())
				{
					if(calculoOPAntiguo.getMedidaTanque1() != opActulizado.getMedidaTanque1() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido() || calculoOPAntiguo.getCantidadVendida() != opActulizado.getCantidadVendida())
					{
						rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
						inventarioLibros = calcularInventarioLibros(calculoAFecha.get(0).getMedidaFinalGalones(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(rta[0], inventarioLibros);
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoAFecha.get(0).getMedidaFinalGalones(), rta[0]);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
						calculoOPAntiguo.setMedidaAnterior(calculoAFecha.get(0).getMedidaFinalGalones());
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
					else
					{
						calculoOPAntiguo.setMedidaAnterior(calculoAFecha.get(0).getMedidaFinalGalones());
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoAFecha.get(0).getMedidaFinalGalones(), calculoOPAntiguo.getMedidaFinalGalones());
						inventarioLibros = calcularInventarioLibros(calculoAFecha.get(0).getMedidaFinalGalones(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(calculoOPAntiguo.getMedidaFinalGalones(), inventarioLibros);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}

				}
				else
				{
					if(calculoOPAntiguo.getMedidaTanque1() != opActulizado.getMedidaTanque1() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido() || calculoOPAntiguo.getCantidadVendida() != opActulizado.getCantidadVendida())
					{
						rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
						
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), 0, rta[0]);
						inventarioLibros = calcularInventarioLibros(0, opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(rta[0], inventarioLibros);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
						calculoOPAntiguo.setMedidaAnterior(0.0);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
					else
					{
						calculoOPAntiguo.setMedidaAnterior(0.0);
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), 0, calculoOPAntiguo.getMedidaFinalGalones());
						inventarioLibros = calcularInventarioLibros(0, opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(calculoOPAntiguo.getMedidaFinalGalones(), inventarioLibros);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
				}
				
				listaCombustibleDown = calculoCombustibleRepository.listaCombustibleCorrienteDown(opActulizado.getFecha(), calculoOPAntiguo.getId());
				listaCombustibleUp = calculoCombustibleRepository.listaCombustibleCorrienteUp(calculoOPAntiguo.getFecha(), opActulizado.getFecha(), calculoOPAntiguo.getId());
				
				if(!listaCombustibleDown.isEmpty() && rta != null)
				{
					actualizarOPDown(rta[0], listaCombustibleDown);
				}
				else 
				{
					actualizarOPDown(calculoOPAntiguo.getMedidaFinalGalones(), listaCombustibleDown);
				}
				
				if(!listaCombustibleUp.isEmpty() && rta != null)
				{
					List<CalculoCombustible> auc = calculoCombustibleRepository.calculoAnteriorAFechaCorriente(calculoOPAntiguo.getFecha(), calculoOPAntiguo.getId());
					actualizarOPUp(auc.get(0).getMedidaFinalGalones(), listaCombustibleUp);
				}
			}
			else if(opActulizado.getMedidaTanque2() != 0 && opActulizado.getMedidaTanque3() != 0)
			{
				List<CalculoCombustible> calculoAFecha = calculoCombustibleRepository.calculoAnteriorAFechaACPM(opActulizado.getFecha(), calculoOPAntiguo.getId());
				
				if(!calculoAFecha.isEmpty())
				{
					if(calculoOPAntiguo.getMedidaTanque2() != opActulizado.getMedidaTanque2() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido() || calculoOPAntiguo.getMedidaTanque3() != opActulizado.getMedidaTanque3())
					{
						rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
						inventarioLibros = calcularInventarioLibros(calculoAFecha.get(0).getMedidaFinalGalones(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(rta[0], inventarioLibros);
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoAFecha.get(0).getMedidaFinalGalones(), rta[0]);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setMedidaAnterior(calculoAFecha.get(0).getMedidaFinalGalones());
						calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
					else
					{
						inventarioLibros = calcularInventarioLibros(calculoAFecha.get(0).getMedidaFinalGalones(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(calculoOPAntiguo.getMedidaFinalGalones(), inventarioLibros);
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoAFecha.get(0).getMedidaFinalGalones(), calculoOPAntiguo.getMedidaFinalGalones());
						
						calculoOPAntiguo.setTotalVendido(totalVenta);
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setMedidaAnterior(calculoAFecha.get(0).getMedidaFinalGalones());
					}

				}
				else
				{
					if(calculoOPAntiguo.getMedidaTanque1() != opActulizado.getMedidaTanque1() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido())
					{
						rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
						inventarioLibros = calcularInventarioLibros(0, opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(rta[0], inventarioLibros);
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), 0.0, rta[0]);
						
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setMedidaAnterior(0.0);
						calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
					else
					{
						totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), 0.0, calculoOPAntiguo.getMedidaFinalGalones());
						inventarioLibros = calcularInventarioLibros(0, opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
						diferencia = calcularDiferencia(calculoOPAntiguo.getMedidaFinalGalones(), inventarioLibros);
						
						calculoOPAntiguo.setMedidaAnterior(0.0);
						calculoOPAntiguo.setIventarioLibros(inventarioLibros);
						calculoOPAntiguo.setDiferencia(diferencia);
						calculoOPAntiguo.setTotalVendido(totalVenta);
					}
				}
				
				listaCombustibleDown = calculoCombustibleRepository.listaCombustibleACPMDown(opActulizado.getFecha(), calculoOPAntiguo.getId());
				listaCombustibleUp = calculoCombustibleRepository.listaCombustibleACPMUp(calculoOPAntiguo.getFecha(), opActulizado.getFecha(), calculoOPAntiguo.getId());
				
				if(!listaCombustibleDown.isEmpty() && rta != null)
				{
					actualizarOPDown(rta[0], listaCombustibleDown);
				}
				else 
				{
					actualizarOPDown(calculoOPAntiguo.getMedidaFinalGalones(), listaCombustibleDown);
				}
				
				if(!listaCombustibleUp.isEmpty() && rta != null)
				{
					List<CalculoCombustible> auc = calculoCombustibleRepository.calculoAnteriorAFechaACPM(calculoOPAntiguo.getFecha(), calculoOPAntiguo.getId());
					actualizarOPUp(auc.get(0).getMedidaFinalGalones(), listaCombustibleUp);
				}
			}
		}
		else
		{
			// List<CalculoCombustible> anterior = calculoCombustibleRepository.calculoAnteriorAFechaCorriente(calculoOPAntiguo.getFecha(), calculoOPAntiguo.getId());
			
			if(opActulizado.getMedidaTanque1() != 0)
			{
				if(calculoOPAntiguo.getMedidaTanque1() != opActulizado.getMedidaTanque1() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido() || calculoOPAntiguo.getCantidadVendida() != opActulizado.getCantidadVendida())
				{
					rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
					calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
				
					totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoOPAntiguo.getMedidaAnterior(), rta[0]);
					calculoOPAntiguo.setTotalVendido(totalVenta);
					
					inventarioLibros = calcularInventarioLibros(calculoOPAntiguo.getMedidaAnterior(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());
					calculoOPAntiguo.setIventarioLibros(inventarioLibros);
					
					diferencia = calcularDiferencia(rta[0], inventarioLibros);					
					calculoOPAntiguo.setDiferencia(diferencia);
					
					listaCombustibleDown = calculoCombustibleRepository.listaCombustibleCorrienteDown(opActulizado.getFecha(), calculoOPAntiguo.getId());
					
					if(!listaCombustibleDown.isEmpty())
					{
						actualizarOPDown(rta[0], listaCombustibleDown);
					}
				}
			}
			else if(opActulizado.getMedidaTanque2() != 0 && opActulizado.getMedidaTanque3() != 0)
			{
				if(calculoOPAntiguo.getMedidaTanque2() != opActulizado.getMedidaTanque2() || calculoOPAntiguo.getCombustibleRecibido() != opActulizado.getCombustibleRecibido() || calculoOPAntiguo.getMedidaTanque3() != opActulizado.getMedidaTanque3() || calculoOPAntiguo.getCantidadVendida() != opActulizado.getCantidadVendida())
				{
					rta = calculoCombustible(opActulizado.getMedidaTanque1(), opActulizado.getMedidaTanque2(), opActulizado.getMedidaTanque3());
					calculoOPAntiguo.setMedidaFinalGalones(rta[0]);
				
					totalVenta = calcularVentaTotal(opActulizado.getCombustibleRecibido(), calculoOPAntiguo.getMedidaAnterior(), rta[0]);
					calculoOPAntiguo.setTotalVendido(totalVenta);
					
					inventarioLibros = calcularInventarioLibros(calculoOPAntiguo.getMedidaAnterior(), opActulizado.getCombustibleRecibido(), opActulizado.getCantidadVendida());			
					calculoOPAntiguo.setIventarioLibros(inventarioLibros);
					
					diferencia = calcularDiferencia(rta[0], inventarioLibros);					
					calculoOPAntiguo.setDiferencia(diferencia);
					
					listaCombustibleDown = calculoCombustibleRepository.listaCombustibleACPMDown(opActulizado.getFecha(), calculoOPAntiguo.getId());
					
					if(!listaCombustibleDown.isEmpty())
					{
						actualizarOPDown(rta[0], listaCombustibleDown);
					}
				}
			}
		}
		
		calculoOPAntiguo.setCombustibleRecibido(opActulizado.getCombustibleRecibido());
		calculoOPAntiguo.setCantidadVendida(opActulizado.getCantidadVendida());
		calculoOPAntiguo.setMedidaTanque1(opActulizado.getMedidaTanque1());
		calculoOPAntiguo.setMedidaTanque2(opActulizado.getMedidaTanque2());
		calculoOPAntiguo.setMedidaTanque3(opActulizado.getMedidaTanque3());
		calculoOPAntiguo.setFecha(opActulizado.getFecha());
		
		calculoCombustibleRepository.save(calculoOPAntiguo);
		model.addAttribute("nuevoCalculoCombustible", new CalculoCombustible());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		return "/calculoOP";
	}
	
	
	

}
