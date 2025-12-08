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
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Cliente> listar(){
        List<Cliente> lista = new ArrayList<>();
        
        String sql = "SELECT id_cliente, nome, email, telefone FROM cliente c";
        
        try(Connection con = Conexao.getConnection(); 
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Cliente c = new Cliente();
                
                c.setId(rs.getInt("id_cliente"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                
                lista.add(c);
                
            }
        
        }catch(SQLException e){
            System.out.println("Erro ao listar cliente" + e);
        
        }
        return lista;
    }
    public void deletar(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public List<Cliente> buscar(String nomeOuCodigo){
    List<Cliente> lista = new ArrayList<>();
    
    // Query adaptada para buscar por NOME (LIKE) ou ID (CAST)
    String sql = """
                 SELECT id_cliente, nome, email, telefone 
                 FROM cliente
                 WHERE LOWER(nome) LIKE LOWER(?) 
                 OR CAST(id_cliente AS CHAR) LIKE ?
                 ORDER BY id_cliente
                 """;
    
    try(Connection con = Conexao.getConnection(); 
        PreparedStatement ps = con.prepareStatement(sql)){
        
        // Parâmetro 1: Busca por nome (usando % para buscar parte do nome)
        ps.setString(1, "%" + nomeOuCodigo + "%");
        
        // Parâmetro 2: Busca por ID (converte o ID para string e usa LIKE)
        ps.setString(2, "%" + nomeOuCodigo + "%");
        
        try(ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Cliente c = new Cliente();
                
                c.setId(rs.getInt("id_cliente"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                
                lista.add(c);
            }
        }
    
    }catch(SQLException e){
        System.out.println("Erro ao buscar cliente: " + e);
    
    }
    return lista;
    }
}
