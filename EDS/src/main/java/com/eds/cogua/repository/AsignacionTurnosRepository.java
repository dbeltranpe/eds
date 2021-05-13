package com.eds.cogua.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.AsignacionTurnos;

@Repository
public interface AsignacionTurnosRepository extends CrudRepository<AsignacionTurnos, Long>  
{
	 @Query(nativeQuery=true, value="SELECT COUNT(*) FROM FESTIVO WHERE FECHA = :date")
	 public long festivo(@Param("date") String date);
	 
	 @Modifying
	 @Transactional
	 @Query(nativeQuery=true, value="DELETE FROM ASIGNACION_TURNOS WHERE ID_TRABAJADOR = :idTrabajador AND FECHA = :date")
	 public void eliminarTurnosPorFecha(@Param("date") String date, @Param("idTrabajador") Long idTrabajador);
	 
	 @Query(nativeQuery=true, value="SELECT title as 'title', start as 'start', end as 'end', classname as 'classname' FROM VI_EVENTOS_TURNO WHERE ID_TRABAJADOR = :idTrabajador")
	 public List<Object[]> turnosTrabajador(@Param("idTrabajador") Long idTrabajador);
}
