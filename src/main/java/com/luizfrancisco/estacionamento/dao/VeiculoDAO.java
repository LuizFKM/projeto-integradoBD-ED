/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;

import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Cliente;
import com.luizfrancisco.estacionamento.model.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class VeiculoDAO {
    public void inserir(Veiculo v){
        String sql = "INSERT INTO veiculo (modelo, cor, placa, id_cliente) VALUES (?, ?, ?, ?)";
        
            try(Connection con = Conexao.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);){
                ps.setString(1, v.getModelo());
                ps.setString(2, v.getCor());
                ps.setString(3, v.getPlaca());
                ps.setInt(4, v.getCliente().getId());
                ps.executeUpdate();

            }catch(SQLException e){
                System.out.println("ERRO -> " + e);
            }
    }
// Supondo que você esteja na classe VeiculoDAO
// Supondo que você esteja na classe VeiculoDAO
public List<Veiculo> listarVeiculo() {
    List<Veiculo> listaVeiculos = new ArrayList<>();
    ClienteDAO clienteDAO = new ClienteDAO();
    
    // Obtém a lista COMPLETA de todos os clientes, usando o método existente
    List<Cliente> todosClientes = clienteDAO.listar(); 
    
    // 1. Comando SQL para Veículo
    String sql = "SELECT id_veiculo, modelo, cor, placa, id_cliente FROM veiculo";

    try (Connection con = Conexao.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Veiculo v = new Veiculo();

            // Mapeamento dos atributos simples...
            v.setId(rs.getInt("id_veiculo"));
            v.setModelo(rs.getString("modelo"));
            v.setCor(rs.getString("cor"));
            v.setPlaca(rs.getString("placa"));

            // 2. Resolução do Objeto (Busca na lista já carregada)
            int idCliente = rs.getInt("id_cliente"); 
            
            // Procura o cliente na lista completa usando o ID
            for (Cliente c : todosClientes) {
                if (c.getId() == idCliente) {
                    v.setCliente(c); // Seta o objeto Cliente encontrado
                    break;           // Interrompe a busca após encontrar
                }
            }
            
            listaVeiculos.add(v);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar veículo: " + e);
    } 

    return listaVeiculos;
}
}
