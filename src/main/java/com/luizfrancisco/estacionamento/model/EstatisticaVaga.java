/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.model;

/**
 *
 * @author User
 */
public class EstatisticaVaga {
    private int qtd;
    private double valor;
    private String tempoFormatado;

    public EstatisticaVaga() {
    }

    
    public EstatisticaVaga(int qtd, double valor, String tempoFormatado) {
        this.qtd = qtd;
        this.valor = valor;
        this.tempoFormatado = tempoFormatado;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTempoFormatado() {
        return tempoFormatado;
    }

    public void setTempoFormatado(String tempoFormatado) {
        this.tempoFormatado = tempoFormatado;
    }

    @Override
    public String toString() {
        return "EstatisticaVaga{" + "qtd=" + qtd + ", valor=" + valor + ", tempoFormatado=" + tempoFormatado + '}';
    }
    
    
    
    
}
