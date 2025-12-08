/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;
import com.luizfrancisco.estacionamento.dao.OperacaoDAO;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.luizfrancisco.estacionamento.model.Operacao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author luizfkm
 */
public class OperacaoController {
    private OperacaoDAO dao = new OperacaoDAO();
    
    public void atualizaTabela(JTable tabela){
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        
        model.setNumRows(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(Operacao o : dao.listarOperacao()){
        
           String entradaFormatada = "";
           String saidaFormatada = "";
           if(o.getHorarioEntrada() != null){
               entradaFormatada = o.getHorarioEntrada().format(dtf);
           }
           
           if(o.getHorarioSaida() == null){
               saidaFormatada = "Em andamento.";
           }else{
               saidaFormatada = o.getHorarioSaida().format(dtf);
           }

            model.addRow(new Object[]{
                o.getId_operacao(),
                o.getVaga().getId(),
                o.getVeiculo().getPlaca(),
                entradaFormatada,
                saidaFormatada,
                o.getValorTotal(),
                o.getValorHora()
            });
        }
    
    }
    
    public void checkout(int idOperacao){
        Operacao op = dao.buscarPorId(idOperacao);
        System.out.println(op.getValorHora());
        op.setHorarioSaida(LocalDateTime.now());
        op.calcularValorTotal();
        dao.inserirSaida(op);
        
    }
    
    
}
