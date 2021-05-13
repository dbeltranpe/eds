package com.eds.cogua.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.CalculoCombustible;

	@Repository
	public interface CalculoCombustibleRepository extends CrudRepository<CalculoCombustible, Long>
	{
	public Optional<CalculoCombustible> findById(int id);
		
	@Query(nativeQuery = true, value = "SELECT medida_final_galones FROM calculo_combustible WHERE medida_tanque_1 > 0 ORDER BY fecha DESC LIMIT 1")
	public List<Object[]> ultimoRegistroCorriente();
	
	@Query(nativeQuery = true, value = "SELECT medida_final_galones FROM calculo_combustible WHERE medida_tanque_2 > 0 ORDER BY fecha DESC LIMIT 1")
	public List<Object[]> ultimoRegistroACPM();
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_1 > 0 ORDER BY fecha")	
	public List<CalculoCombustible> combustibleCorriente();
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_2 > 0 ORDER BY fecha")
	public List<CalculoCombustible> combustibleACPM();
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM calculo_combustible WHERE fecha >= :fechaAEliminar AND medida_tanque_1 > 0")
	public void eliminarDatosCorriente(@Param("fechaAEliminar") String fecha);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM calculo_combustible WHERE fecha >= :fechaAEliminar AND medida_tanque_2 > 0")
	public void eliminarDatosACPM(@Param("fechaAEliminar") String fecha);
	
	public Optional<CalculoCombustible> findById(Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_1 > 0 AND fecha > :fechaMayor AND id != :idActual ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleCorrienteDown(@Param("fechaMayor") String fecha, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_2 > 0 AND fecha > :fechaMayor AND id != :idActual ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleACPMDown(@Param("fechaMayor") String fecha, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_1 > 0 AND fecha > :fechaMayor ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleCorrienteDownNuevo(@Param("fechaMayor") String fecha);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_2 > 0 AND fecha > :fechaMayor ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleACPMDownNuevo(@Param("fechaMayor") String fecha);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_1 > 0 AND fecha > :fechaMenor AND fecha < :fechaMayor AND id != :idActual ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleCorrienteUp(@Param("fechaMenor") String fechaMenor, @Param("fechaMayor") String fechaMayor, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible WHERE medida_tanque_2 > 0 AND fecha > :fechaMenor AND fecha < :fechaMayor AND id != :idActual ORDER BY fecha")
	public List<CalculoCombustible> listaCombustibleACPMUp(@Param("fechaMenor") String fechaMenor, @Param("fechaMayor") String fechaMayor, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible cc WHERE fecha < :fechaCalculo AND medida_tanque_1 > 0 AND id != :idActual ORDER BY fecha DESC LIMIT 1")
	public List<CalculoCombustible> calculoAnteriorAFechaCorriente(@Param("fechaCalculo") String fecha, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible cc WHERE fecha < :fechaCalculo and medida_tanque_2 > 0 AND id != :idActual ORDER BY fecha DESC LIMIT 1")
	public List<CalculoCombustible> calculoAnteriorAFechaACPM(@Param("fechaCalculo") String fecha, @Param("idActual") Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible cc WHERE fecha < :fechaCalculo AND medida_tanque_1 > 0 ORDER BY fecha DESC LIMIT 1")
	public List<CalculoCombustible> calculoAnteriorAFechaCorrienteNuevo(@Param("fechaCalculo") String fecha);
	
	@Query(nativeQuery = true, value = "SELECT * FROM calculo_combustible cc WHERE fecha < :fechaCalculo AND medida_tanque_2 > 0 ORDER BY fecha DESC LIMIT 1")
	public List<CalculoCombustible> calculoAnteriorAFechaACPMNuevo(@Param("fechaCalculo") String fecha);
	
	@Query(nativeQuery = true, value = "select * from calculo_combustible where YEAR(fecha) = ?1 and month(fecha) = ?2  and medida_tanque_2 > 0 order by fecha asc")
	public List<CalculoCombustible> listarACPMxMesxAnio(int anio, int mes);
	
	@Query(nativeQuery = true, value = "select * from calculo_combustible where YEAR(fecha) = ?1 and month(fecha) = ?2  and medida_tanque_1 > 0 order by fecha asc")
	public List<CalculoCombustible> listarGasolinaxMesxAnio(int anio, int mes);
	
	
}