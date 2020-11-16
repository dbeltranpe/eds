package com.eds.cogua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "NMN_TRABAJADOR")
public class NominaTrabajador 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_nmn_trabajador")
	private Long id;
	
	@Column(name="salarioBase")
	private float salarioBase;
	
	@Column(name="auxilioTransporte")
	private float auxilioTransporte;
	
	@Column(name="descuentosSalario")
	private float descuentosSalario;
	
	@Column(name="gananciasExtra")
	private float gananciasExtra;
	
	
	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}

	public float getSalarioBase() 
	{
		return salarioBase;
	}

	public void setSalarioBase(float salarioBase)
	{
		this.salarioBase = salarioBase;
	}

	public float getAuxilioTransporte() 
	{
		return auxilioTransporte;
	}

	public void setAuxilioTransporte(float auxilioTransporte) 
	{
		this.auxilioTransporte = auxilioTransporte;
	}

	public float getDescuentosSalario() 
	{
		return descuentosSalario;
	}

	public void setDescuentosSalario(float descuentosSalario) 
	{
		this.descuentosSalario = descuentosSalario;
	}

	public float getGananciasExtra() 
	{
		return gananciasExtra;
	}

	public void setGananciasExtra(float gananciasExtra) 
	{
		this.gananciasExtra = gananciasExtra;
	}
}
