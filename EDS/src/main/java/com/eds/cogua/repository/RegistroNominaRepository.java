package com.eds.cogua.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.RegistroNomina;

@Repository
public interface RegistroNominaRepository extends CrudRepository<RegistroNomina, Long>  
{
	@Query(nativeQuery=true, value="SELECT ID_NOMINA, FECHA, ID_TRABAJADOR, VR_SALARIO, VR_AUX_TRANSPORTE, NRO_DIAS_TRABAJADOS, VR_DEV_SALARIO, VR_DEV_AUX_TRANSPORTE, VR_HED, VR_RN, VR_HEN, VR_HRD, VR_HRND, VR_HEDD, VR_HEND, VR_TOTAL_DEVENGADO, VR_BASE_APORTE, VR_APORTE_SALUD, VR_APORTE_PENSION, VR_APORTE_COOP, VR_CREDITO_AHORRO, VR_POL_EXEQUIAL, VR_DESCUADRE, VR_INTERESES_CESANTIAS, VR_TOTAL_DESC, VR_NETO_PAGAR, NUM_MES "
			+ " FROM REGISTRO_NOMINA WHERE MONTH(FECHA) = MONTH(?2) AND YEAR(FECHA) = YEAR(?2) AND ID_TRABAJADOR = ?1")
	public RegistroNomina obtenerNominaMes(Long idTrabajador, Date fecha);
	
	@Query(value = "{call SP_NOMINA_TRABAJADOR(:P_DIAS_TRABAJADOS, :P_FECHA, :P_APORTE_COP, :P_CREDITO_COP, :P_POL_EXEQUIAL, :P_DESCUADRES, :P_ID_TRABAJADOR, :P_USUARIO)}", nativeQuery = true)
    public Double spNominaTrabajador(
            @Param("P_DIAS_TRABAJADOS")int pDiasTrabajados,
            @Param("P_FECHA")Date pFechas,
            @Param("P_APORTE_COP")float pAporteCop,
            @Param("P_CREDITO_COP")float pCreditoCop,
            @Param("P_POL_EXEQUIAL")float pPolExequial,
            @Param("P_DESCUADRES")float pDescuadres,
            @Param("P_ID_TRABAJADOR")int pIdTrabajador,
            @Param("P_USUARIO")String pUsuario
    );
	
	@Query(nativeQuery=true, value="SELECT USUARIO, FECHA, TX_DESC FROM VI_LOG_NOMINA")
    public  List<Object[]> logNomina();
    
	@Query(nativeQuery=true, value="SELECT MES, ANIO, VALOR_NETO_PAGAR FROM vi_pagos_nominales_mensuales")
    public  List<Object[]> pagosNominalesMensuales();
	
}