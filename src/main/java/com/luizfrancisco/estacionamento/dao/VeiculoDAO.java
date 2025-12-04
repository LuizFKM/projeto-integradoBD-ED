/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;

import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
