/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;

import com.luizfrancisco.estacionamento.dao.ClienteDAO;
import com.luizfrancisco.estacionamento.model.Cliente;
import java.util.List;
import javax.swing.JOptionPane;
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
    
    public void cadastrarCliente(Cliente cliente){
        
        try{
            if(cliente != null){
                dao.inserir(cliente);
            }else{
                System.out.println("Cliente null");
            }
        }catch(Exception e){
            System.out.println("ERRO ao inserir cliente -> " + e);
        }
    }
    
    public void deletarCliente(int id) throws Exception {
    
    if (id <= 0) {
        throw new Exception("ID do cliente inválido para exclusão.");
    }
    dao.deletar(id);
}
    
    public void editarCliente(int id, Cliente c){
        try{
            if(id > 0 && c != null){
                dao.atualizar(id, c);
            }else{
                System.out.println("cliente invalido");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
