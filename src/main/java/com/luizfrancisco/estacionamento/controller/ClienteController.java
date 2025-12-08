/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;

import com.luizfrancisco.estacionamento.dao.ClienteDAO;
import com.luizfrancisco.estacionamento.model.Cliente;
import java.util.List;
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
    
    
    public List<Cliente> filtrarClientes(String busca){
        String buscaTermo = busca.trim();
        
        if(buscaTermo.isEmpty()){
            return dao.listar();
        }else {
            return dao.buscar(buscaTermo);
        }
    }
}
