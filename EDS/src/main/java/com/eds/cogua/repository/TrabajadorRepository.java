package com.eds.cogua.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eds.cogua.entity.Trabajador;
import com.eds.cogua.entity.Usuario;

@Repository
public interface TrabajadorRepository extends CrudRepository<Trabajador, Long>  
{
	public Optional<Trabajador> findByUsuario(Usuario usuario);
	
	@Query(nativeQuery=true, value="SELECT USUARIO, FECHA, TX_DESC FROM VI_LOG_TRABAJADOR")
    public  List<Object[]> logTrabajador();
}