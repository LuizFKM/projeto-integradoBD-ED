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
public List<Veiculo> listarVeiculo() {
    List<Veiculo> listaVeiculos = new ArrayList<>();
    
    String sql = "SELECT v.id_veiculo, v.modelo, v.cor, v.placa,c.id_cliente, c.nome, c.email"
            + " FROM veiculo AS v INNER JOIN cliente AS c ON "
            + "v.id_cliente = c.id_cliente";

    try (Connection con = Conexao.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Veiculo v = new Veiculo();

            v.setId(rs.getInt("id_veiculo"));
            v.setModelo(rs.getString("modelo"));
            v.setCor(rs.getString("cor"));
            v.setPlaca(rs.getString("placa"));

 
            
            Cliente c = new Cliente();
            c.setId(rs.getInt("id_cliente"));
            c.setNome(rs.getString("nome"));
            c.setEmail(rs.getString("email"));
            
            v.setCliente(c);
            
            listaVeiculos.add(v);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar veículo: " + e);
        e.printStackTrace();
    } 
    return listaVeiculos;
}
public void deletar(int id) {
// Comando SQL adaptado: usamos id_veiculo
    String sql = "DELETE FROM veiculo WHERE id_veiculo = ?";
    
    // Usando try-with-resources
    try(Connection con = Conexao.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        
        // 1. Define o ID do veículo a ser deletado
        ps.setInt(1, id);
        
        // 2. Executa o comando DELETE
        ps.executeUpdate();
        
    }catch(SQLException e){
        // Imprime a stack trace completa para ajudar no debug
        e.printStackTrace();
        // Sugestão: Relançar a exceção para que o Controller possa notificar a View
        throw new RuntimeException("Erro ao deletar veículo. Verifique se existem operações vinculadas.", e);
    }
}
}
