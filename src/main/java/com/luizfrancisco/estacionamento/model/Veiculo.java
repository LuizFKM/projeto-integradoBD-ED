/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.model;

/**
 *
 * @author User
 */
public class Veiculo {
    private int id;
    private Cliente idCliente;
    private String modelo;
    private String cor;
    private String placa;
    
    public Veiculo(){}

    public Veiculo(int id, Cliente idCliente, String modelo, String cor, String placa) {
        this.id = id;
        this.idCliente = idCliente;
        this.modelo = modelo;
        this.cor = cor;
        this.placa = placa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return idCliente;
    }

    public void setCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "Veiculo{" + "id=" + id + ", idCliente=" + idCliente + ", modelo=" + modelo + ", cor=" + cor + ", placa=" + placa + '}';
    }
    
    
}
