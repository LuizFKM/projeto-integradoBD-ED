/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;
import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author User
 */
public class UsuarioDAO {
    private Connection conn;
    
    public Usuario autenticar(String login, String senha) throws SQLException {
    String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
    
    try(Connection con = Conexao.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
    
        ps.setString(1, login);
        ps.setString(2, senha);
        
        try(ResultSet rs = ps.executeQuery()){
            if(rs.next()){
                Usuario u = new Usuario();
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setRole(rs.getString("role"));
                return u;
            }
        }
    }
    return null;
}
}
