package com.eds.cogua.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> 
{
	public Optional<Cliente> findByNombres (String nombres);
	public Optional<Cliente> findByIdentificacion (int identificacion);
	public Optional<Cliente> findById (int id);
	
	@Query(nativeQuery=true, value="SELECT * FROM cliente WHERE cliente.fechaSOAT BETWEEN :fechaActual AND :fechaPosterior")
    public  List<Cliente> obtenerClientesPendientesSOATSemana(@Param("fechaActual") String fechaActual, @Param("fechaPosterior") String fechaPosterior);
	
	@Query(nativeQuery=true, value="SELECT * FROM cliente WHERE cliente.fechaSOAT BETWEEN :fechaActual AND :fechaPosterior")
    public  List<Cliente> obtenerClientesPendientesSOATMes(@Param("fechaActual") String fechaActual, @Param("fechaPosterior") String fechaPosterior);
	
	@Query(nativeQuery=true, value="SELECT ELT(MONTH(fechaSOAT), \"Enero\", \"Febrero\", \"Marzo\", \"Abril\", \"Mayo\", \"Junio\", \"Julio\", \"Agosto\", \"Septiembre\", \"Octubre\", \"Noviembre\", \"Diciembre\") AS Mes, COUNT(id_cliente) AS num_clientes FROM cliente WHERE EXTRACT(MONTH FROM fechaSOAT) BETWEEN '01' AND '12' AND EXTRACT(YEAR FROM fechaSOAT) = :anio GROUP BY(EXTRACT(MONTH FROM fechaSOAT))")
    public  List<Object[]> cantidadClientesPorMes(@Param("anio") String anio);
	
	@Query(nativeQuery=true, value="SELECT EXTRACT(YEAR FROM fechaSOAT) AS año, COUNT(id_cliente) AS num_clientes FROM cliente GROUP BY(EXTRACT(YEAR FROM fechaSOAT))")
    public  List<Object[]> cantidadClientesPorAnio();
    
    @Query(nativeQuery=true, value="SELECT COUNT(id_cliente) FROM cliente")
    public  List<Object[]> totalClientes();
}
