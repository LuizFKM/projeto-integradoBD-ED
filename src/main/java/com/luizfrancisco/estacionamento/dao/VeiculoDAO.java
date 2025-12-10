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

    String sql = "DELETE FROM veiculo WHERE id_veiculo = ?";
    
  
    try(Connection con = Conexao.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        
        
        ps.setInt(1, id);
        

        ps.executeUpdate();
        
    }catch(SQLException e){
      
        e.printStackTrace();
     
        throw new RuntimeException("Erro ao deletar veículo. Verifique se existem operações vinculadas.", e);
    }
}

public void atualizar(int id, Veiculo v)throws SQLException {
    String sql = "UPDATE veiculo SET modelo = ?, cor = ?, placa = ?, id_cliente = ? WHERE id_veiculo = ?";
    
    try (Connection con = Conexao.getConnection(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, v.getModelo());
            ps.setString(2, v.getCor());
            ps.setString(3, v.getPlaca());
            ps.setInt(4, v.getCliente().getId());
            ps.setInt(5, id);
            
            int linhasAfetadas = ps.executeUpdate();
            
            if(linhasAfetadas == 0){
                throw new SQLException("Nenhum veículo encontrado com o ID: " + id);
            }
        }
    
    
}
        
public List<Veiculo> buscar(String termoBusca) {
        List<Veiculo> listaVeiculos = new ArrayList<>();
        
    
        String sql = """
                     SELECT v.id_veiculo, v.modelo, v.cor, v.placa, c.id_cliente, c.nome, c.email
                     FROM veiculo AS v INNER JOIN cliente AS c ON v.id_cliente = c.id_cliente
                     WHERE CAST(v.id_veiculo AS CHAR) LIKE ?         
                     OR LOWER(v.placa) LIKE LOWER(?)         
                     OR LOWER(v.modelo) LIKE LOWER(?)        
                     OR LOWER(v.cor) LIKE LOWER(?)           
                     OR LOWER(c.nome) LIKE LOWER(?)
                     """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String param = "%" + termoBusca + "%";
          
            ps.setString(1, param); 
            ps.setString(2, param); 
            ps.setString(3, param); 
            ps.setString(4, param);
            ps.setString(5, param);

            try (ResultSet rs = ps.executeQuery()) {
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
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículo: " + e);
        }
        return listaVeiculos;
    }
}
