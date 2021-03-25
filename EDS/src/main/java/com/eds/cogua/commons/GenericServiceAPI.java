package com.eds.cogua.commons;

import java.io.Serializable;
import java.util.List;

public interface GenericServiceAPI<T,ID extends Serializable> 
{	
	public T guardar(T entidad);
	
	public void eliminar(ID id);
	
	public T obtener(ID id);
	
	public List<T> listar();
}