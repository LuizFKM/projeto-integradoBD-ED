/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.model;

import java.time.LocalDateTime;

/**
 *
 * @author luizfkm
 */
public class Operacao {
    private int id_operacao;
    private LocalDateTime horarioEntrada;
    private LocalDateTime horarioSaida;
    private double valorHora;
    private double valorTotal;
    private Veiculo veiculo;
    private Vaga vaga;

    public Operacao() {
    }
    
 public Operacao(int id_operacao, LocalDateTime horarioEntrada, LocalDateTime horarioSaida, double valorHora, double valorTotal, Veiculo veiculo, Vaga vaga) {
        this.id_operacao = id_operacao;
        this.horarioEntrada = horarioEntrada;
        this.horarioSaida = horarioSaida;
        this.valorHora = valorHora;
        this.valorTotal = valorTotal;
        this.veiculo = veiculo;
        this.vaga = vaga;
    }

    

    public int getId_operacao() {
        return id_operacao;
    }

    public void setId_operacao(int id_operacao) {
        this.id_operacao = id_operacao;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(LocalDateTime horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public LocalDateTime getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(LocalDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
    }
    
    

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    @Override
    public String toString() {
        return "Operacao{" + "id_operacao=" + id_operacao + ", horarioEntrada=" + horarioEntrada + ", horarioSaida=" + horarioSaida + ", valorHora=" + valorHora + ", valorTotal=" + valorTotal + ", veiculo=" + veiculo + ", vaga=" + vaga + '}';
    }

    
}
