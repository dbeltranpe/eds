package com.eds.cogua.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.CalculoCombustible;

@Repository
public interface CalculoCombustibleRepository extends CrudRepository<CalculoCombustible, Long>
{
	public Optional<CalculoCombustible> findById(int id);
	
	@Query(nativeQuery = true, value = "SELECT medida_final_galones FROM calculo_combustible ORDER BY id DESC")
	public List<Object[]> ultimoRegistro();
}
