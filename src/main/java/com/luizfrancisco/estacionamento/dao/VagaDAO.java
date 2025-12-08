/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;

import com.luizfrancisco.estacionamento.database.Conexao;

import com.luizfrancisco.estacionamento.model.Vaga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luizfkm
 */
public class VagaDAO {
    
    public List<Vaga> listarVagas() {
    List<Vaga> listaVagas = new ArrayList<>();
    
    String sql = "SELECT id_vaga, status FROM vaga ORDER BY id_vaga";

    try (Connection con = Conexao.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Vaga v = new Vaga();

            v.setId(rs.getInt("id_vaga"));
                
            v.setStatus(rs.getBoolean("status"));
            listaVagas.add(v);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar veículo: " + e);
    } 
    return listaVagas;
}
    public List<Vaga> buscar(String termoBusca) {
        List<Vaga> listaVagas = new ArrayList<>();
        
        // Busca apenas pelo ID da vaga
        String sql = "SELECT id_vaga, status FROM vaga WHERE CAST(id_vaga AS CHAR) LIKE ? ORDER BY id_vaga";

        try (java.sql.Connection con = com.luizfrancisco.estacionamento.database.Conexao.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            // 1. ID da Vaga
            ps.setString(1, "%" + termoBusca + "%");

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vaga v = new Vaga();
                    v.setId(rs.getInt("id_vaga"));
                    v.setStatus(rs.getBoolean("status"));
                    listaVagas.add(v);
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao buscar vaga: " + e);
        }
        return listaVagas;
    }
    
}
