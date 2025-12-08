/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;

import com.luizfrancisco.estacionamento.dao.VagaDAO;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.luizfrancisco.estacionamento.model.Vaga;
import java.util.List;

/**
 *
 * @author luizfkm
 */
public class VagaController {
    private VagaDAO dao;
    
    public VagaController(){
        this.dao = new VagaDAO();
    }
    
    
    public List<Vaga> filtrarVagas(String buscaId, String filtroStatus){
        String termo = (buscaId != null) ? buscaId.trim() : "";
        
        return dao.buscar(termo, filtroStatus);
    }
    
    
}
