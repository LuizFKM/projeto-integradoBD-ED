/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;
import com.luizfrancisco.estacionamento.dao.OperacaoDAO;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.luizfrancisco.estacionamento.model.Operacao;

/**
 *
 * @author luizfkm
 */
public class OperacaoController {
    private OperacaoDAO od = new OperacaoDAO();
    
    public void atualizaTabela(JTable tabela){
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        
        model.setNumRows(0);
        
        for(Operacao o : od.listarOperacao()){
            model.addRow(new Object[]{
                o.getId_operacao(),
                o.getVaga().getId(),
                o.getVeiculo().getPlaca(),
                o.getHorarioEntrada(),
                o.getHorarioSaida(),
                o.getValorTotal()
            });
        }
    
    }
    
    
}
