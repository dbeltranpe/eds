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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "ASIGNACION_TURNOS")
public class AsignacionTurnos 
{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_asignacion")
	private Long id;
	
	@JoinColumn(name="ID_TRABAJADOR",unique=true)
	@OneToOne(cascade = CascadeType.ALL)
	private Trabajador trabajador;
	
	@Column(name="ID_TURNO")
	private int idTurno;
	
	@Column(name="FECHA")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;

	@Column(name="CD_USU_CRE")
	private String cdUsuCre;
	
	@Column(name="CD_FE_CRE")
	private Date cdFeCre;
	
	@Column(name="CD_USU_MO")
	private String cdUsuMo;
	
	@Column(name="CD_FE_MO")
	private Date cdFeMo;
	
	public AsignacionTurnos()
	{
		this.cdFeCre = new Date();
		this.cdFeMo = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public int getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(int idTurno) {
		this.idTurno = idTurno;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	
	
	

}
