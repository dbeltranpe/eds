package com.eds.cogua.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "REGISTRO_NOMINA")
public class RegistroNomina 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_NOMINA")
	private Long id;
	
	@Column(name="FECHA")
	private LocalDate fecha;
	
	@JoinColumn(name="ID_TRABAJADOR",unique=true)
	@OneToOne(cascade = CascadeType.ALL)
	private Trabajador trabajador;
	
	@Column(name="VR_SALARIO")
	private float vrSalario;
	
	@Column(name="VR_AUX_TRANSPORTE")
	private float vrAuxTransporte;
	
	@Column(name="NRO_DIAS_TRABAJADOS")
	private int nroDiasTrabajadores;
	
	@Column(name="VR_DEV_SALARIO")
	private float vrDevSalario;
	
	@Column(name="VR_DEV_AUX_TRANSPORTE")
	private float vrDevAuxTransporte;
	
	@Column(name="VR_HED")
	private float vrHED;
	
	@Column(name="VR_RN")
	private float vrRN;
	
	@Column(name="VR_HEN")
	private float vrHEN;
	
	@Column(name="VR_HRD")
	private float vrHRD;
	
	@Column(name="VR_HRND")
	private float vrHRND;
	
	@Column(name="VR_HEDD")
	private float vrHEDD;
	
	@Column(name="VR_HEND")
	private float vrHEND;
	
	@Column(name="VR_TOTAL_DEVENGADO")
	private float vrTotalDevengado;
	
	@Column(name="VR_BASE_APORTE")
	private float vrBaseAporte;
	
	@Column(name="VR_APORTE_SALUD")
	private float vrAporteSalud;
	
	@Column(name="VR_APORTE_PENSION")
	private float vrAportePension;

	@Column(name="VR_APORTE_COOP")
	private float vrAporteCoop;
	
	@Column(name="VR_CREDITO_AHORRO")
	private float vrCreditoAhorro;
	
	@Column(name="VR_POL_EXEQUIAL")
	private float vrPolExequial;
	
	@Column(name="VR_DESCUADRE")
	private float vrDescuadre;
	
	@Column(name="VR_INTERESES_CESANTIAS")
	private float vrInteresesCesantias;
	
	@Column(name="VR_TOTAL_DESC")
	private float vrTotalDesc;
	
	@Column(name="VR_NETO_PAGAR")
	private float vrNetoPagar;
	
	@Column(name="NUM_MES")
	private int numMes;
	
	
	public RegistroNomina()
	{
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public float getVrSalario() {
		return vrSalario;
	}

	public void setVrSalario(float vrSalario) {
		this.vrSalario = vrSalario;
	}

	public float getVrAuxTransporte() {
		return vrAuxTransporte;
	}

	public void setVrAuxTransporte(float vrAuxTransporte) {
		this.vrAuxTransporte = vrAuxTransporte;
	}

	public int getNroDiasTrabajadores() {
		return nroDiasTrabajadores;
	}

	public void setNroDiasTrabajadores(int nroDiasTrabajadores) {
		this.nroDiasTrabajadores = nroDiasTrabajadores;
	}

	public float getVrDevSalario() {
		return vrDevSalario;
	}

	public void setVrDevSalario(float vrDevSalario) {
		this.vrDevSalario = vrDevSalario;
	}

	public float getVrDevAuxTransporte() {
		return vrDevAuxTransporte;
	}

	public void setVrDevAuxTransporte(float vrDevAuxTransporte) {
		this.vrDevAuxTransporte = vrDevAuxTransporte;
	}

	public float getVrHED() {
		return vrHED;
	}

	public void setVrHED(float vrHED) {
		this.vrHED = vrHED;
	}

	public float getVrRN() {
		return vrRN;
	}

	public void setVrRN(float vrRN) {
		this.vrRN = vrRN;
	}

	public float getVrHEN() {
		return vrHEN;
	}

	public void setVrHEN(float vrHEN) {
		this.vrHEN = vrHEN;
	}

	public float getVrHRD() {
		return vrHRD;
	}

	public void setVrHRD(float vrHRD) {
		this.vrHRD = vrHRD;
	}

	public float getVrHRND() {
		return vrHRND;
	}

	public void setVrHRND(float vrHRND) {
		this.vrHRND = vrHRND;
	}

	public float getVrHEDD() {
		return vrHEDD;
	}

	public void setVrHEDD(float vrHEDD) {
		this.vrHEDD = vrHEDD;
	}

	public float getVrHEND() {
		return vrHEND;
	}

	public void setVrHEND(float vrHEND) {
		this.vrHEND = vrHEND;
	}

	public float getVrTotalDevengado() {
		return vrTotalDevengado;
	}

	public void setVrTotalDevengado(float vrTotalDevengado) {
		this.vrTotalDevengado = vrTotalDevengado;
	}

	public float getVrBaseAporte() {
		return vrBaseAporte;
	}

	public void setVrBaseAporte(float vrBaseAporte) {
		this.vrBaseAporte = vrBaseAporte;
	}

	public float getVrAporteSalud() {
		return vrAporteSalud;
	}

	public void setVrAporteSalud(float vrAporteSalud) {
		this.vrAporteSalud = vrAporteSalud;
	}

	public float getVrAportePension() {
		return vrAportePension;
	}

	public void setVrAportePension(float vrAportePension) {
		this.vrAportePension = vrAportePension;
	}

	public float getVrAporteCoop() {
		return vrAporteCoop;
	}

	public void setVrAporteCoop(float vrAporteCoop) {
		this.vrAporteCoop = vrAporteCoop;
	}

	public float getVrCreditoAhorro() {
		return vrCreditoAhorro;
	}

	public void setVrCreditoAhorro(float vrCreditoAhorro) {
		this.vrCreditoAhorro = vrCreditoAhorro;
	}

	public float getVrPolExequial() {
		return vrPolExequial;
	}

	public void setVrPolExequial(float vrPolExequial) {
		this.vrPolExequial = vrPolExequial;
	}

	public float getVrDescuadre() {
		return vrDescuadre;
	}

	public void setVrDescuadre(float vrDescuadre) {
		this.vrDescuadre = vrDescuadre;
	}

	public float getVrInteresesCesantias() {
		return vrInteresesCesantias;
	}

	public void setVrInteresesCesantias(float vrInteresesCesantias) {
		this.vrInteresesCesantias = vrInteresesCesantias;
	}

	public float getVrTotalDesc() {
		return vrTotalDesc;
	}

	public void setVrTotalDesc(float vrTotalDesc) {
		this.vrTotalDesc = vrTotalDesc;
	}

	public float getVrNetoPagar() {
		return vrNetoPagar;
	}

	public void setVrNetoPagar(float vrNetoPagar) {
		this.vrNetoPagar = vrNetoPagar;
	}
	
	public int getNumMes() {
		return numMes;
	}

	public void setNumMes(int numMes) {
		this.numMes = numMes;
	}
	

}