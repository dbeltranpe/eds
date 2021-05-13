package com.eds.cogua.util;

import java.util.Map;

import com.eds.cogua.entity.CalculoCombustible;
import com.eds.cogua.entity.RegistroNomina;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class PDF 
{
	private Map<String,String> dicNomina;
	
	String[] mes = {"","ENERO","FEBRERO","MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
	
	public PDF()
	{
		
		dicNomina = new HashMap<String,String>();
	}
	
	/**
     * Convierte el número que recibe como argumento a su representación escrita con letra.
     *
     * @param s Una cadena de texto que contiene los dígitos de un número.
     * @return  Una cadena de texto que contiene la representación con letra de
     *          la parte entera del número que se recibió como argumento.
     */
    public String cantidadConLetra(String s) 
    {
        StringBuilder result = new StringBuilder();
        BigDecimal totalBigDecimal = new BigDecimal(s).setScale(2, BigDecimal.ROUND_DOWN);
        long parteEntera = totalBigDecimal.toBigInteger().longValue();
        int triUnidades      = (int)((parteEntera % 1000));
        int triMiles         = (int)((parteEntera / 1000) % 1000);
        int triMillones      = (int)((parteEntera / 1000000) % 1000);
        int triMilMillones   = (int)((parteEntera / 1000000000) % 1000);
 
        if (parteEntera == 0) {
            result.append("cero ");
            return result.toString();
        }
 
        if (triMilMillones > 0) result.append(triTexto(triMilMillones).toString() + "mil ");
        if (triMillones > 0)    result.append(triTexto(triMillones).toString());
 
        if (triMilMillones == 0 && triMillones == 1) result.append("millón ");
        else if (triMilMillones > 0 || triMillones > 0) result.append("millones ");
 
        if (triMiles > 0)       result.append(triTexto(triMiles).toString() + "mil ");
        if (triUnidades > 0)    result.append(triTexto(triUnidades).toString());
 
        return result.toString();
    }
 
    /**
     * Convierte una cantidad de tres cifras a su representación escrita con letra.
     *
     * @param n La cantidad a convertir.
     * @return  Una cadena de texto que contiene la representación con letra
     *          del número que se recibió como argumento.
     */
    private static StringBuilder triTexto(int n) {
        StringBuilder result = new StringBuilder();
        int centenas = n / 100;
        int decenas  = (n % 100) / 10;
        int unidades = (n % 10);
 
        switch (centenas) {
            case 0: break;
            case 1:
                if (decenas == 0 && unidades == 0) {
                    result.append("cien ");
                    return result;
                }
                else result.append("ciento ");
                break;
            case 2: result.append("doscientos "); break;
            case 3: result.append("trescientos "); break;
            case 4: result.append("cuatrocientos "); break;
            case 5: result.append("quinientos "); break;
            case 6: result.append("seiscientos "); break;
            case 7: result.append("setecientos "); break;
            case 8: result.append("ochocientos "); break;
            case 9: result.append("novecientos "); break;
        }
 
        switch (decenas) {
            case 0: break;
            case 1:
                if (unidades == 0) { result.append("diez "); return result; }
                else if (unidades == 1) { result.append("once "); return result; }
                else if (unidades == 2) { result.append("doce "); return result; }
                else if (unidades == 3) { result.append("trece "); return result; }
                else if (unidades == 4) { result.append("catorce "); return result; }
                else if (unidades == 5) { result.append("quince "); return result; }
                else result.append("dieci");
                break;
            case 2:
                if (unidades == 0) { result.append("Veinte "); return result; }
                else result.append("veinti");
                break;
            case 3: result.append("treinta "); break;
            case 4: result.append("cuarenta "); break;
            case 5: result.append("cincuenta "); break;
            case 6: result.append("sesenta "); break;
            case 7: result.append("setenta "); break;
            case 8: result.append("ochenta "); break;
            case 9: result.append("noventa "); break;
        }
 
        if (decenas > 2 && unidades > 0)
            result.append("y ");
 
        switch (unidades) {
            case 0: break;
            case 1: result.append("un "); break;
            case 2: result.append("dos "); break;
            case 3: result.append("tres "); break;
            case 4: result.append("cuatro "); break;
            case 5: result.append("cinco "); break;
            case 6: result.append("seis "); break;
            case 7: result.append("siete "); break;
            case 8: result.append("ocho "); break;
            case 9: result.append("nueve "); break;
        }
 
        return result;
    }

	
	public void poblarDesprendibleNomina(RegistroNomina pRegistroNomina)
	{
		/*
		 * Datos de la Cabecera
		 */
		dicNomina.put("empleado", pRegistroNomina.getTrabajador().getNombres() + " " + pRegistroNomina.getTrabajador().getApellidos());
		dicNomina.put("cc", pRegistroNomina.getTrabajador().getIdentificacion() + "");
		dicNomina.put("cargo", "Empleado");
		
		//LocalDate date = pRegistroNomina.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate date = pRegistroNomina.getFecha();
		System.out.println(date);
		LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
		int year = date.getYear();
		Month month = date.getMonth();
		String monthName = month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
		dicNomina.put("periodoPagar", "Del 1 al " + lastDay.getDayOfMonth() + " de " + monthName + " de " + year);
		
		dicNomina.put("salarioBase", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrSalario()) );
		dicNomina.put("dias", pRegistroNomina.getNroDiasTrabajadores() + "");
		
		/*
		 * Devengos
		 */
		dicNomina.put("salario", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrSalario()));
		dicNomina.put("auxTransporte", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrAuxTransporte()));
		dicNomina.put("RN", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrRN()));
		dicNomina.put("HDRD", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrHRD()));
		dicNomina.put("HNRD", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrHRND()));
		dicNomina.put("HED", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrHED()));
		dicNomina.put("HEND", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrHEND()));
		
		/*
		 * Descuentos
		 */
		dicNomina.put("aporteSalud", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrAporteSalud()));
		dicNomina.put("aportePension", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrAportePension()));
		dicNomina.put("aporteCoop", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrAporteCoop()));
		dicNomina.put("cuotaCredito", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrCreditoAhorro()));
		dicNomina.put("descuadres", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrDescuadre()));
		dicNomina.put("polExequial", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrPolExequial()));
		
		/*
		 * Datos Totales
		 */
		dicNomina.put("totalDevengos", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrTotalDevengado()));
		dicNomina.put("totalDescuentos", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrTotalDesc()));
		dicNomina.put("totalPagar", String.format(Locale.US, "%,.2f", pRegistroNomina.getVrNetoPagar()));
		
		String valor = String.format(Locale.US, "%,.2f",  pRegistroNomina.getVrNetoPagar());
		valor = valor.substring(0, valor.length() - 2);
		valor = valor.replace(",", "");
		valor = valor.replace(".", "");

		String valor2 = String.format(Locale.US, "%,.2f",  pRegistroNomina.getVrNetoPagar());
		valor2 = valor2.substring(valor2.length() - 2);

		String texto1 = cantidadConLetra(valor).trim();
		//String texto2 = axaUtil.cantidadConLetra(valor2);
		String[] t = texto1.split(" ");
		t[0] = (t[0].equalsIgnoreCase("Un")) ? "" : t[0];

		String tx = "";
		for (int i = 0; i < t.length; i++) {
			tx += t[i] + " ";
		}
		tx = tx.trim();
		
		String descPagar = tx + " pesos " + valor2;
		dicNomina.put("descPagar", descPagar.toUpperCase());
		
	}
	
	public byte[] pdfDesprendibleNomina(RegistroNomina pRegistroNomina) throws Exception
	{
		poblarDesprendibleNomina(pRegistroNomina);
		
		String oldFile = "src/main/resources/pdf/DesprendibleNomina.pdf";
		PdfReader reader = new PdfReader(oldFile);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper pdfStamper = new PdfStamper(reader, baos);
		PdfContentByte cb = pdfStamper.getOverContent(1);

		BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.CP1252,BaseFont.NOT_EMBEDDED);
		cb.setColorFill(Color.BLACK);
		cb.setFontAndSize(bf, 10);

		cb.beginText();
		
		/*
		 * Datos de la Cabecera
		 */
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("empleado"), 150, 538, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("cc"), 150, 522, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("cargo"), 150, 507, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("periodoPagar"), 170, 490, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("salarioBase"), 240, 475, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("dias"), 440, 490, 0);
		
		/*
		 * Devengos
		 */
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("salario"), 222, 427, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("auxTransporte"), 222, 412, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("RN"), 222, 398, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("HDRD"), 222, 383, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("HNRD"), 222, 368, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("HED"), 222, 352, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("HEND"), 222, 337, 0);
		
		/*
		 * Descuentos
		 */
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("aporteSalud"), 445, 427, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("aportePension"), 445, 413, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("aporteCoop"), 445, 398, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("cuotaCredito"), 445, 383, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("descuadres"), 445, 368, 0);
		//cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("polExequial"), 445, 353, 0);
		
		/*
		 * Datos Totales
		 */
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("totalDevengos"), 224, 261, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("totalDescuentos"), 447, 261, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, dicNomina.get("totalPagar"), 443, 244, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, dicNomina.get("descPagar"), 300, 229, 0);
		
		
		cb.endText();
		baos.close();
		pdfStamper.close();
		reader.close();
		
		byte[] bytes = baos.toByteArray();
		return (bytes);
	}
	
	public byte[] pdfOP(int pAnio, int pMes, int pTanque, List<CalculoCombustible> pDetalleCompustible) throws Exception
	{
		String oldFile = "src/main/resources/pdf/OPPDF.pdf";
		PdfReader reader = new PdfReader(oldFile);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper pdfStamper = new PdfStamper(reader, baos);
		PdfContentByte cb = pdfStamper.getOverContent(1);

		BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.CP1252,BaseFont.NOT_EMBEDDED);
		cb.setColorFill(Color.BLACK);
		cb.setFontAndSize(bf, 10);

		cb.beginText();
		
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, mes[pMes], 160, 718, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, String.valueOf(pAnio), 340, 718, 0);
		
		String tanque = (pTanque == 1)? "GASOLINA":"ACPM";
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, tanque, 552, 720, 0);
		
		double totalCantidadVendida = 0.0;
		double totalDiferencia = 0.0;
		double totalCompras = 0.0;
		
		for (int i = 0; i < pDetalleCompustible.size(); i++) 
		{
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getMedidaAnterior() + "", 110, 608 - (i*17), 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getCombustibleRecibido() + "", 227, 608 - (i*17), 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getCantidadVendida() + "", 302, 608 - (i*17), 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getIventarioLibros() + "", 407, 608 - (i*17), 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getMedidaFinalGalones() + "", 493, 608 - (i*17), 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pDetalleCompustible.get(i).getDiferencia() + "", 552, 608 - (i*17), 0);
			
			totalCantidadVendida += pDetalleCompustible.get(i).getCantidadVendida();
			totalDiferencia += pDetalleCompustible.get(i).getDiferencia();
			totalCompras += pDetalleCompustible.get(i).getCombustibleRecibido();
		}
		

		double totalPerdidaGanancia = (totalDiferencia*100)/totalCompras;
		
		DecimalFormat df = new DecimalFormat("##.00");
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String.format("%.2f", totalCantidadVendida)+"", 302, 78, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String.format("%.2f", totalDiferencia)+"", 552, 78, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String.format("%.2f", totalCompras)+"", 225, 45, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String.format("%.2f", totalPerdidaGanancia) +"", 407, 30, 0);
		
	
		cb.endText();
		baos.close();
		pdfStamper.close();
		reader.close();
		
		byte[] bytes = baos.toByteArray();
		return (bytes);
	}
	
	
	public static void main(String[] args) 
	{
		try
		{
			PDF p = new PDF();
			System.out.println(p.pdfDesprendibleNomina(null));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
