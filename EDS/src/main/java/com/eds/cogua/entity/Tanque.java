package com.eds.cogua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class Tanque 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;

	@Column(name="altura")
	private int altura;

	@Column(name="galon")
	private int galon;

	@Column(name="litro")
	private int litro;

	public Tanque()
	{

	}

	public int getId() 
	{
		return id;
	}

	public int getAltura()
	{
		return altura;
	}

	public int getGalon()
	{
		return galon;
	}

	public int getLitro()
	{
		return litro;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setAltura(int altura) 
	{
		this.altura = altura;
	}

	public void setGalon(int galon)
	{
		this.galon = galon;
	}

	public void setLitro(int litro) 
	{
		this.litro = litro;
	}
}