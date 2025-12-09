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
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperExportManager;

/**
 *
 * @author npc12
 */
public class RelatorioUtil {
    

    public static void abrirPDF(String caminho, Map<String, Object> parametros, Connection con) {
    try {
      
        InputStream reportStream = RelatorioUtil.class.getResourceAsStream(caminho);

        if (reportStream == null) {
            System.out.println("ERRO CRÍTICO: Não achei o arquivo .jasper em: " + caminho);
            return;
        }

        JasperReport relatorio = (JasperReport) JRLoader.loadObject(reportStream);

        JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, con);

        if (print.getPages().isEmpty()) {
            System.out.println("ALERTA: O relatório foi gerado com 0 páginas (Dados vazios?).");
        }
  
        File pdfTemp = File.createTempFile("relatorio_final", ".pdf");
        JasperExportManager.exportReportToPdfFile(print, pdfTemp.getAbsolutePath());
        Desktop.getDesktop().open(pdfTemp);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}