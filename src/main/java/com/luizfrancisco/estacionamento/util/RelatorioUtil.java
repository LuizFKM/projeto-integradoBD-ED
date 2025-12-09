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
import javax.swing.UIManager; 
import net.sf.jasperreports.view.JasperViewer; 

/**
 *
 * @author npc12
 */
public class RelatorioUtil {



public static void abrirPDF(String caminho, Map<String, Object> parametros, Connection con) {
    try {
        InputStream reportStream = RelatorioUtil.class.getResourceAsStream(caminho);
        if (reportStream == null) {
            System.out.println("ERRO: Arquivo .jasper não encontrado: " + caminho);
            return;
        }

        JasperReport relatorio = (JasperReport) JRLoader.loadObject(reportStream);
        JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, con);

        if (print.getPages().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "O relatório não retornou dados.");
            return;
        }

        // --- CORREÇÃO PARA LINUX (POP!_OS / UBUNTU) ---
        // Isso resolve o erro "GLib-GIO-CRITICAL" e o travamento visual.
        // Forçamos o Java a usar o visual padrão (Metal) em vez do GTK do Linux.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ----------------------------------------------

        // Abre a janela interna do Jasper
        // 'false' significa que fechar o relatório NÃO fecha o sistema todo
        JasperViewer view = new JasperViewer(print, false);
        view.setTitle("Relatório de Estacionamento");
        view.setVisible(true);
        view.toFront(); // Traz a janela para frente

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + e.getMessage());
    }
}

 
}