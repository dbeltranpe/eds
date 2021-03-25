package com.eds.cogua.controller;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


import com.eds.cogua.entity.Authority;
import com.eds.cogua.entity.Cliente;
import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.entity.RegistroNomina;
import com.eds.cogua.entity.Usuario;
import com.eds.cogua.repository.AuthorityRepository;
import com.eds.cogua.repository.ClienteRepository;
import com.eds.cogua.repository.TrabajadorRepository;
import com.eds.cogua.repository.UsuarioRepository;
import com.eds.cogua.repository.RegistroNominaRepository;
import com.eds.cogua.service.api.ClienteServiceAPI;
import com.eds.cogua.service.api.TrabajadorServiceAPI;
import com.eds.cogua.util.CambioPassword;
import com.eds.cogua.util.PDF;
import com.eds.cogua.util.EnvioCorreos;
import com.eds.cogua.util.LeerArchivoClientesExcel;
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

	@Autowired
	private ClienteRepository clienteReposiory;
	
	@Autowired
	private RegistroNominaRepository registroNominaRepository;

	@Autowired
	private ClienteServiceAPI clienteServiceAPI;
	
	@Autowired
	private EnvioCorreos envioCorreos;

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
		model.addAttribute("cambioPassword", new CambioPassword());

		return "adminAddUser";
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
			model.addAttribute("nuevoCliente", new Cliente());

			Authority userRole1 = authorityRepository.findByAuthority("ROLE_ISLERO");
			Authority userRole2 = authorityRepository.findByAuthority("ROLE_GEROP");
			Authority userRole3 = authorityRepository.findByAuthority("ROLE_AUXPATIO");
			Authority userRole4 = authorityRepository.findByAuthority("ROLE_ADMIN");
			List<Authority> roles = Arrays.asList(userRole1, userRole2, userRole3, userRole4);
			model.addAttribute("roles", roles);

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

		return "admin";
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

		return "admin";
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
		
		if(entrada == 1)
		{
			rta = "admin";
		}
		else if(entrada == 2)
		{
			rta = "dashBoardClientes";
		}

		return rta;
	}
	
	@GetMapping({"/envioCorreoNominaTrabajador/{nombres}/{apellidos}/{correo}"})
	public String envioCorreoNominaTrabajador(Model model, @PathVariable(name="nombres") String nombres, @PathVariable(name="apellidos") String apellidos, @PathVariable(name="correo") String correo)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userRepository.findByUsername(auth.getName()).get();
		Trabajador trabajador = trabajadorRepository.findByUsuario(user).get();
		
		try
		{
			PDF p = new PDF();
			byte [] pdf = null;
			pdf = p.pdfDesprendibleNomina(null);
			envioCorreos.sendEmail2(correo, nombres + apellidos, pdf);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("cambioPassword", new CambioPassword());
		
		return "admin";
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
	
		Trabajador userDetalle = trabajadorServiceAPI.obtener(id);
		RegistroNomina registroNomina = registroNominaRepository.findById(id).get();
		
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
	
	@GetMapping({"/pdf"})
	public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{	
		PDF p = new PDF();
		byte[] bytes = null;
		try {
			bytes = p.pdfDesprendibleNomina(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
	}

}
