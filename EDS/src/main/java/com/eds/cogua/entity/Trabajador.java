package com.eds.cogua.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Trabajador
{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_trabajador")
	private Long id;
	
	@Column(name="identificacion")
	private int identificacion;

	@Column(name="nombres")
	private String nombres;
	
	@Column(name="apellidos")
	private String apellidos;
	
	@Column(name="correo")
	private String correo;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="direccion")
	private String direccion;
	
	@Column(name="ciudad")
	private String ciudad;
	
	@Column(name="telefono")
	private String telefono;
	
	@Column(name="FECHA_INGRESO")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaIngreso;
	
	@JoinColumn(name="id_usuario",unique=true)
	@OneToOne(cascade = CascadeType.ALL)
	private Usuario usuario;
	
	@JoinColumn(name="id_nmn_trabajador",unique=true)
	@OneToOne(cascade = CascadeType.ALL)
	private NominaTrabajador nominaTrabajador;
	
	@Column(name="CD_USU_CRE")
	private String cdUsuCre;
	
	@Column(name="CD_FE_CRE")
	private Date cdFeCre;
	
	@Column(name="CD_USU_MO")
	private String cdUsuMo;
	
	@Column(name="CD_FE_MO")
	private Date cdFeMo;
	
	
	public Trabajador()
	{
		usuario = new Usuario();
		usuario.setEnabled(true);
		nominaTrabajador = new NominaTrabajador();
		cdFeCre = new Date();
		cdFeMo = new Date();
	}

	public String getCdUsuCre() {
		return cdUsuCre;
	}

	public void setCdUsuCre(String cdUsuCre) {
		this.cdUsuCre = cdUsuCre;
	}

	public Date getCdFeCre() {
		return cdFeCre;
	}

	public void setCdFeCre(Date cdFeCre) {
		this.cdFeCre = cdFeCre;
	}

	public String getCdUsuMo() {
		return cdUsuMo;
	}

	public void setCdUsuMo(String cdUsuMo) {
		this.cdUsuMo = cdUsuMo;
	}

	public Date getCdFeMo() {
		return cdFeMo;
	}

	public void setCdFeMo(Date cdFeMo) {
		this.cdFeMo = cdFeMo;
	}

	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}

	public String getNombres() 
	{
		return nombres;
	}

	public void setNombres(String nombres) 
	{
		this.nombres = nombres;
	}

	public String getApellidos() 
	{
		return apellidos;
	}

	public void setApellidos(String apellidos) 
	{
		this.apellidos = apellidos;
	}

	public String getCorreo() 
	{
		return correo;
	}

	public void setCorreo(String correo) 
	{
		this.correo = correo;
	}
	
	public String getDescripcion() 
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion) 
	{
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() 
	{
		return usuario;
	}

	public void setUsuario(Usuario usuario) 
	{
		this.usuario = usuario;
	}
	
	public String getDireccion() 
	{
		return direccion;
	}

	public void setDireccion(String direccion) 
	{
		this.direccion = direccion;
	}

	public String getCiudad() 
	{
		return ciudad;
	}

	public void setCiudad(String ciudad) 
	{
		this.ciudad = ciudad;
	}

	public String getTelefono() 
	{
		return telefono;
	}

	public void setTelefono(String telefono) 
	{
		this.telefono = telefono;
	}
	
	public int getIdentificacion() 
	{
		return identificacion;
	}

	public void setIdentificacion(int identificacion) 
	{
		this.identificacion = identificacion;
	}

	public NominaTrabajador getNominaTrabajador() 
	{
		return nominaTrabajador;
	}

	public void setNominaTrabajador(NominaTrabajador nominaTrabajador) 
	{
		this.nominaTrabajador = nominaTrabajador;
	}
	
	public Date getFechaIngreso() 
	{
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) 
	{
		this.fechaIngreso = fechaIngreso;
	}



}
