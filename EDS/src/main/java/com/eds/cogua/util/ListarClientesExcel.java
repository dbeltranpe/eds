package com.eds.cogua.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.eds.cogua.entity.Cliente;

@Component("/clientList")
public class ListarClientesExcel extends AbstractXlsView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		response.setHeader("Content-Disposition", "attachment; filename=\"listado-clientes.xlsx\"");
		Sheet hojaExcel = workbook.createSheet("Clientes");
		
		Row filaTitulo = hojaExcel.createRow(0);
		Cell celda = filaTitulo.createCell(0);
		celda.setCellValue("LISTADO GENERAL DE CLIENTES");
		
		Row filaRotulos = hojaExcel.createRow(2);
		String[] columnas = {"IDENTIFICACIÓN", "NOMBRES", "APELLIDOS", "CORREO", "FECHA SOAT"};
		
		for (int i = 0; i < columnas.length; i++) 
		{
			celda = filaRotulos.createCell(i);
			celda.setCellValue(columnas[i]);
		}
		
		@SuppressWarnings("unchecked")
		List<Cliente> listadoClientes = (List<Cliente>) model.get("clientesList");
		
		int numFila = 3;
		for (Cliente cliente : listadoClientes) 
		{
			filaRotulos = hojaExcel.createRow(numFila);
			
			filaRotulos.createCell(0).setCellValue(cliente.getIdentificacion());
			filaRotulos.createCell(1).setCellValue(cliente.getNombres());
			filaRotulos.createCell(2).setCellValue(cliente.getApellidos());
			filaRotulos.createCell(3).setCellValue(cliente.getCorreo());
			filaRotulos.createCell(4).setCellValue(cliente.getFechaSOAT());
			
			numFila ++;
		}
	}

}
