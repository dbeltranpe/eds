package com.eds.cogua.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import com.eds.cogua.entity.Cliente;

public class LeerArchivoClientesExcel 
{

	public LeerArchivoClientesExcel()
	{

	}

	public List<Cliente> leerExcel(MultipartFile reapExcelDataFile)
	{
		List<Cliente> clientesCargados = new ArrayList<>();
		
		try
		{
			Workbook libroExcel = WorkbookFactory.create(reapExcelDataFile.getInputStream());
			Sheet hoja = libroExcel.getSheetAt(0);

			for (int i = 3; i < hoja.getPhysicalNumberOfRows(); i++)
			{
				Cliente cliente = new Cliente();

				Row fila = hoja.getRow(i);

				cliente.setIdentificacion((int) fila.getCell(0).getNumericCellValue());
				cliente.setNombres(fila.getCell(1).getStringCellValue());
				cliente.setApellidos(fila.getCell(2).getStringCellValue());
				cliente.setCorreo(fila.getCell(3).getStringCellValue());
				cliente.setFechaSOAT(fila.getCell(4).getStringCellValue());

				clientesCargados.add(cliente);
			}

			libroExcel.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return clientesCargados;
	}

}
