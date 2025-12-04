/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;
import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Operacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author luizfkm
 */
public class OperacaoDAO {
    public void inserir(Operacao o){
        String sql = "INSERT INTO operacao (horario_entrada, horario_saida, valor_total, id_veiculo, id_vaga)"
                + "VALUES (?, ?, ?, ?, ?)";
        
        try(Connection con = Conexao.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ){
            
            
        
        }catch(SQLException e){
            System.out.println("ERRO ao cadastrar operacao ->" + e);
        }
    }
}
