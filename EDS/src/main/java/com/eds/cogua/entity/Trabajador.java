package com.eds.cogua.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Trabajador
{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_trabajador")
	private Long id;
	
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
	
	@JoinColumn(name="id_usuario",unique=true)
	@OneToOne(cascade = CascadeType.ALL)
	private Usuario usuario;
	
	public Trabajador()
	{
		usuario = new Usuario();
		usuario.setEnabled(true);
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


}
