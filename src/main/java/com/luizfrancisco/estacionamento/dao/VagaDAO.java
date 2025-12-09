/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;

import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.EstatisticaVaga;

import com.luizfrancisco.estacionamento.model.Vaga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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
    public List<Vaga> buscar(String termoId, String filtroStatus) {
        List<Vaga> listaVagas = new ArrayList<>();
        
        String sql = "SELECT * FROM vaga WHERE CAST(id_vaga AS CHAR) LIKE ?";
        
        if(!filtroStatus.equals("TODAS")){
         sql += " AND status = ? ";
        }
        
        sql += " ORDER BY id_vaga ASC";

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + termoId + "%");
            
            if(!filtroStatus.equals("TODAS")){
                boolean isOcupada = filtroStatus.equals("OCUPADA");
                ps.setBoolean(2, isOcupada);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vaga v = new Vaga();
                    v.setId(rs.getInt("id_vaga"));
                    v.setStatus(rs.getBoolean("status"));
                    listaVagas.add(v);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar vaga: " + e);
        }
        return listaVagas;
    }

   public EstatisticaVaga vwInfoVaga(int idVaga){
       String sql = "SELECT * FROM vw_informacoes_vaga WHERE id_vaga = ?";
       
       EstatisticaVaga ev = new EstatisticaVaga();
       
       ev.setQtd(0);
       ev.setValor(0.0);
       ev.setTempoFormatado("00:00:00");
       
       try(Connection con = Conexao.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setInt(1, idVaga);

           try(ResultSet rs = ps.executeQuery()){
               if(rs.next()){
                   ev.setQtd(rs.getInt("quantidade_operacoes"));
                   ev.setValor(rs.getDouble("valor_total_acumulado"));
                   String tempo = rs.getString("tempo_total_acumulado");
                   
                   if(tempo != null){
                       ev.setTempoFormatado(tempo);
                   }else{
                       ev.setTempoFormatado("00:00:00");
                   }  
               }
           }
        
       }catch(SQLException e){
           System.out.println("Erro ao buscar estatísticas da vaga: " + e);
       }
       return ev;
       
   }
    
}
