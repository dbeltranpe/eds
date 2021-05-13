package com.eds.cogua.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eds.cogua.entity.Tanque;

@Repository
public interface TanquesRepository extends CrudRepository<Tanque, java.lang.Integer>
{
	public Optional<Tanque> findByAltura(int altura);
	public Optional<Tanque> findById(int id);

	@Query(nativeQuery = true, value = "SELECT galon FROM val_tanque_1 WHERE altura = :alturaTanque")
    public List<Object[]> galonAltutaTanque1(@Param("alturaTanque") int alturaTanque);

    @Query(nativeQuery = true, value = "SELECT galon FROM val_tanque_2 WHERE altura = :alturaTanque")
    public List<Object[]> galonAltutaTanque2(@Param("alturaTanque") int alturaTanque);

    @Query(nativeQuery = true, value = "SELECT galon FROM val_tanque_3 WHERE altura = :alturaTanque")
    public List<Object[]> galonAltutaTanque3(@Param("alturaTanque") int alturaTanque);

}