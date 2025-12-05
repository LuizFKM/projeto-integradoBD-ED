/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;

import com.luizfrancisco.estacionamento.dao.ClienteDAO;
import com.luizfrancisco.estacionamento.model.Cliente;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class ClienteController {
    private ClienteDAO dao;
    
    public ClienteController(){
        this.dao = new ClienteDAO();
    }
    public void atualizaTabela(JTable tabela){
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        
        model.setNumRows(0);
        
        
        
        for (Cliente c : dao.listar()){
            model.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.getTelefone(),
            });
        }
    }
}
