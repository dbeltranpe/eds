package com.eds.cogua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class CalculoCombustible 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private String fecha;
	
	@Column(name = "combustible_recibido")
	private double combustibleRecibido;
	
	@Column(name = "medida_tanque_1")
	private double medidaTanque1;
	
	@Column(name = "medida_tanque_2")
	private double medidaTanque2;
	
	@Column(name = "cantidad_vendida")
	private double cantidadVendida;
	
	@Column(name = "inventario_libros")
	private double iventarioLibros;
	
	@Column(name = "medida_final_galones")
	private double medidaFinalGalones;
	
	@Column(name = "diferencia")
	private double diferencia;
	
	@Column(name = "total_vendido")
	private double totalVendido;
	
	@Column(name = "medida_anterior")
	private double medidaAnterior;
	
	public CalculoCombustible()
	{
		medidaAnterior = 0;
		iventarioLibros = 0;
		totalVendido = 0;
		combustibleRecibido = 0;
	}
	
	public Long getId() 
	{
		return id;
	}
	
	public void setId(Long id) 
	{
		this.id = id;
	}
	
	public String getFecha()
	{
		return fecha;
	}

	public double getCombustibleRecibido()
	{
		return combustibleRecibido;
	}

	public double getMedidaTanque1()
	{
		return medidaTanque1;
	}

	public double getMedidaTanque2()
	{
		return medidaTanque2;
	}

	public double getCantidadVendida() 
	{
		return cantidadVendida;
	}

	public double getIventarioLibros() 
	{
		return iventarioLibros;
	}

	public double getMedidaFinalGalones() 
	{
		return medidaFinalGalones;
	}

	public double getDiferencia() 
	{
		return diferencia;
	}

	public double getTotalVendido() 
	{
		return totalVendido;
	}
	
	public double getMedidaAnterior() 
	{
		return medidaAnterior;
	}

	public void setFecha(String fecha) 
	{
		this.fecha = fecha;
	}

	public void setCombustibleRecibido(double combustibleRecibido) 
	{
		this.combustibleRecibido = combustibleRecibido;
	}

	public void setMedidaTanque1(double medidaTanque1) 
	{
		this.medidaTanque1 = medidaTanque1;
	}

	public void setMedidaTanque2(double medidaTanque2) 
	{
		this.medidaTanque2 = medidaTanque2;
	}

	public void setCantidadVendida(double cantidadVendida)
	{
		this.cantidadVendida = cantidadVendida;
	}

	public void setIventarioLibros(double iventarioLibros)
	{
		this.iventarioLibros = iventarioLibros;
	}

	public void setMedidaFinalGalones(double medidaFinalGalones) 
	{
		this.medidaFinalGalones = medidaFinalGalones;
	}

	public void setDiferencia(double diferencia) 
	{
		this.diferencia = diferencia;
	}

	public void setTotalVendido(double totalVendido) 
	{
		this.totalVendido = totalVendido;
	}
	
	public void setMedidaAnterior(double medidaAnterior) 
	{
		this.medidaAnterior = medidaAnterior;
	}
	
}
