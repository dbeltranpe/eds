package com.eds.cogua.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericServiceAPI<T, ID>
{

	@Override
	public T guardar(T entidad) 
	{
		return getDao().save(entidad);
	}

	@Override
	public void eliminar(ID id) 
	{
		getDao().deleteById(id);
	}

	@Override
	public T obtener(ID id) 
	{
		T toReturn = null;
		
		Optional<T> obj = getDao().findById(id);
		if(obj.isPresent())
		{
			toReturn = obj.get();
		}
		
		return toReturn;
	}

	@Override
	public List<T> listar() 
	{
		List<T> listToReturn = new ArrayList<>();
		getDao().findAll().forEach(obj -> listToReturn.add(obj));
		return listToReturn;
	}
	
	public abstract CrudRepository<T, ID> getDao();
	

}