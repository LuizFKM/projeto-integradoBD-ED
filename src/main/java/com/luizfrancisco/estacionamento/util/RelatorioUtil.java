/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.util;
import java.sql.Connection; 
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import java.awt.Desktop;
import java.io.File;
import net.sf.jasperreports.engine.JasperExportManager;

/**
 *
 * @author npc12
 */
public class RelatorioUtil {
    

    public static void abrirPDF(String caminho, Map<String, Object> parametros, Connection con){
        try {
            //Carregar o relatório .jasper
            JasperReport relatorio = (JasperReport) JRLoader.loadObjectFromFile(caminho);
            
            // Preenche os dados
            JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, con);
            
            File pdfTemp = File.createTempFile("relatorio_",".pdf");
            pdfTemp.deleteOnExit();
            
            JasperExportManager.exportReportToPdfFile(print, pdfTemp.getAbsolutePath());
            
            Desktop.getDesktop().open(pdfTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}