/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.controller;
import com.luizfrancisco.estacionamento.dao.OperacaoDAO;
import com.luizfrancisco.estacionamento.model.Operacao;
import java.time.LocalDateTime;

import java.util.List;

/**
 *
 * @author luizfkm
 */
public class OperacaoController {
    private OperacaoDAO dao = new OperacaoDAO();

    public List<Operacao> filtrarOperacoes(String busca){
        String buscaTermo = busca.trim();
        
        if(buscaTermo.isEmpty()){
            return dao.listarOperacao();
        }else {
            return dao.buscar(buscaTermo);
        }
    }

    public void checkout(int idOperacao){
        Operacao op = dao.buscarPorId(idOperacao);
        System.out.println(op.getValorHora());
        op.setHorarioSaida(LocalDateTime.now());
        op.calcularValorTotal();
        dao.inserirSaida(op);
        
    }
    
    public void registrarEntrada(Operacao novaOperacao) throws Exception {
    String placa = novaOperacao.getVeiculo().getPlaca();
    boolean jaTemCarro = dao.verificaVeiculoEstacionado(placa);
    
    if (jaTemCarro) {
        throw new Exception("ERRO: O veículo " + placa + " já está estacionado!");
    }

    dao.inserirEntrada(novaOperacao);
    }
    
    public void deletarOperacao(int idOperacao){
        Operacao op = dao.buscarPorId(idOperacao);
        dao.deletar(idOperacao);
    }
    
    public void atualizarOperacao(int id, Operacao op){
        try{
            if(id > 0 && op != null){
                dao.atualizarOperacao(id, op);
            }else{
                System.out.println("Operacao inválida");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    public Operacao buscarOp(int id) {
    if(id > 0) {
        return dao.buscarPorId(id);
    }
    return null;
}
    
    public void registrarSaida(Operacao op){
        if(op != null){
            dao.inserirSaida(op);
        }
    }
    
}
