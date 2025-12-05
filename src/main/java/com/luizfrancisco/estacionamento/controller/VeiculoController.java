/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;
import com.luizfrancisco.estacionamento.dao.VeiculoDAO;
import com.luizfrancisco.estacionamento.model.Veiculo;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author npc12
 */


public class VeiculoController {
    private VeiculoDAO dao;
    
    public VeiculoController(){
        this.dao = new VeiculoDAO();
    }
    
    public void atualizaTabela(JTable tabelaVeiculos){
        DefaultTableModel model = (DefaultTableModel) tabelaVeiculos.getModel();
        
        model.setNumRows(0);
        
        for (Veiculo v : dao.listarVeiculo()) {
            model.addRow(new Object[]{
                v.getCliente(),
                v.getId(),
                v.getCor(),
                v.getModelo(),
                v.getPlaca(),
            });
        }
    }
}
