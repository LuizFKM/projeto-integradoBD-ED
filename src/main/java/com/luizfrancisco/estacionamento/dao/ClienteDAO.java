/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;
import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author User
 */
public class ClienteDAO {
    public void inserir(Cliente c){
        String sql = "INSERT INTO cliente (nome, email, telefone) VALUES (?,?,?)";
        
        try(Connection con = Conexao.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);){
                ps.setString(1, c.getNome());
                ps.setString(2, c.getEmail());
                ps.setString(3, c.getTelefone());
                ps.executeUpdate();
   
        }catch(SQLException e){
                System.out.println("ERRO -> " + e);
            }
    }
    
    
}
