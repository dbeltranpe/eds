package com.eds.cogua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Cliente
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_cliente")
	private Long id;
	
	@Column(name="identificacion")
	private int identificacion;

	@Column(name="nombres")
	private String nombres;
	
	@Column(name="apellidos")
	private String apellidos;
	
	@Column(name="correo")
	private String correo;
	
	@Column(name="fechaSOAT")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private String fechaSOAT;
	
	public Cliente()
	{
		
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
	
	public String getFechaSOAT() 
	{
		return fechaSOAT;
	}

	public void setFechaSOAT(String fechaSOAT) 
	{
		this.fechaSOAT = fechaSOAT;
	}
	
	public int getIdentificacion() 
	{
		return identificacion;
	}

	public void setIdentificacion(int identificacion) 
	{
		this.identificacion = identificacion;
	}

}
