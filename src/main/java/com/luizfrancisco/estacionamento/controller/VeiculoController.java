/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;
import com.luizfrancisco.estacionamento.dao.VeiculoDAO;
import com.luizfrancisco.estacionamento.model.Veiculo;
import java.util.List;
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

    public List<Veiculo> filtrarVeiculos(String busca){
        String buscaTermo = busca.trim();
        
        if(buscaTermo.isEmpty()){
            return dao.listarVeiculo();
        }else {
            return dao.buscar(buscaTermo);
        }
    }
}
