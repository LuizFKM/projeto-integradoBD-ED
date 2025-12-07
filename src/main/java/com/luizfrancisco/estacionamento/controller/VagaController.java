/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;

import com.luizfrancisco.estacionamento.dao.VagaDAO;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.luizfrancisco.estacionamento.model.Vaga;

/**
 *
 * @author luizfkm
 */
public class VagaController {
    private VagaDAO dao = new VagaDAO();
    
    public void atualizaTabela(JTable tabela){
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        
        model.setNumRows(0);
        
        for(Vaga v : dao.listarVagas()){
            v.getId();
            v.isStatus();
        }
    }
    
    
}
