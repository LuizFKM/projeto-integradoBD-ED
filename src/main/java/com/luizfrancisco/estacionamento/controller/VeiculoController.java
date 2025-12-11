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
    
    public void cadastrarVeiculo(Veiculo veiculo){
        try{
            if(veiculo != null){
                dao.inserir(veiculo);
            }else{
                System.out.println("veiculo null");
            }
        }catch(Exception e){
            System.out.println("ERRO ao inserir veiculo -> " + e);
        }
    }
    
    public void atualizarVeiculo(int id, Veiculo v){
        try{
            if(id > 0 && v != null){
                dao.atualizar(id, v);
            }else{
                System.out.println("veiculo invalido");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void deletarVeiculo(int id)throws Exception {
        if (id <= 0) {
            throw new Exception("ID do cliente inválido para exclusão.");
        }
        dao.deletar(id);
    }

    public List<Veiculo> filtrarVeiculos(String busca) throws Exception{
        
        if (busca == null) {
            return dao.listarVeiculo();
        }
        String buscaTermo = busca.trim();
        
        if (buscaTermo.isEmpty()) {
            return dao.listarVeiculo();
        }else {
            return dao.buscar(buscaTermo);
        }
        
    }
}
