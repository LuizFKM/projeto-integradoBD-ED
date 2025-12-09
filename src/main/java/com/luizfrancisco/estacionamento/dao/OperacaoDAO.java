/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.dao;
import com.luizfrancisco.estacionamento.database.Conexao;
import com.luizfrancisco.estacionamento.model.Operacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.luizfrancisco.estacionamento.model.Cliente;
import com.luizfrancisco.estacionamento.model.Veiculo;
import com.luizfrancisco.estacionamento.model.Vaga;
import javax.swing.SpringLayout;
/**
 *
 * @author luizfkm
 */
public class OperacaoDAO {
    
    public void inserirEntrada(Operacao o){
        String sql = "INSERT INTO operacao (horario_entrada, id_veiculo, id_vaga, valor_hora)"
                + "VALUES (?, ?, ?, ?)";
        
        try(Connection con = Conexao.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ){
                    ps.setTimestamp(1, Timestamp.valueOf(o.getHorarioEntrada()));
                    ps.setInt(2, o.getVeiculo().getId());
                    ps.setInt(3, o.getVaga().getId());
                    ps.setDouble(4, o.getValorHora());
                    ps.executeUpdate();
            
        
        }catch(SQLException e){
            System.out.println("ERRO ao registrar entrada ->" + e);
        }
    }
    
    public void inserirSaida(Operacao o){
        String sql = "UPDATE operacao SET horario_saida = ?, valor_total = ? WHERE id_operacao = ?";
        
        try(Connection con = Conexao.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ){
                    ps.setTimestamp(1, Timestamp.valueOf(o.getHorarioSaida()));
                    ps.setDouble(2, o.getValorTotal());
                    ps.setInt(3, o.getId_operacao());
                    ps.executeUpdate();
            
                    
        }catch(SQLException e){
            System.out.println("ERRO ao registrar saída ->" + e);
        }
    }
    
    public void deletar(int id){
        String sql = "DELETE FROM operacao WHERE id_operacao = ?";
                
        try (Connection con = Conexao.getConnection(); 
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Operacao> listarOperacao(){
        List<Operacao> lista = new ArrayList<>();
        
        String sql = "SELECT "
           + " o.id_operacao, "
           + " o.horario_entrada, "
           + " o.horario_saida, "
           + " o.valor_total, "

           + " v.placa, "
           + " v.modelo, "

           + " vg.id_vaga, "
           + " vg.status as status_vaga "
           + "FROM operacao o "
           + "INNER JOIN veiculo v ON o.id_veiculo = v.id_veiculo "
           + "INNER JOIN vaga vg ON o.id_vaga = vg.id_vaga";
        
        try(Connection con = Conexao.getConnection(); 
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
            

            while (rs.next()) {
                Operacao operacao = new Operacao();
                
                operacao.setId_operacao(rs.getInt("id_operacao"));
                operacao.setHorarioEntrada(rs.getTimestamp("horario_entrada").toLocalDateTime());
    
                java.sql.Timestamp tsSaida = rs.getTimestamp("horario_saida");
                if (tsSaida != null) {
                    operacao.setHorarioSaida(tsSaida.toLocalDateTime());
                    operacao.setValorTotal(rs.getDouble("valor_total"));
                } else {
                    operacao.setHorarioSaida(null);
                    operacao.setValorTotal(0.0);
                }

    
                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setModelo(rs.getString("modelo"));
                operacao.setVeiculo(veiculo);

   
                Vaga vaga = new Vaga();
                vaga.setId(rs.getInt("id_vaga"));
                vaga.setStatus(rs.getBoolean("status_vaga"));
    
    
                operacao.setVaga(vaga);
                lista.add(operacao);
            }
        
        }catch(SQLException e){
            System.out.println("Erro ao listar cliente" + e);
        
        }
        
        return lista;
    } 
    
    public Operacao buscarPorId(int id) {
    String sql = "SELECT o.*, v.placa, v.modelo, c.id_cliente, v.cor, c.nome, vg.status as status_vaga " +
                 "FROM operacao o " +
                 "INNER JOIN veiculo v ON o.id_veiculo = v.id_veiculo " +
                 "INNER JOIN cliente c ON v.id_cliente = c.id_cliente " + 
                 "INNER JOIN vaga vg ON o.id_vaga = vg.id_vaga " +
                 "WHERE o.id_operacao = ?";
                 
    Operacao op = new Operacao();

    try (Connection con = Conexao.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            op.setId_operacao(rs.getInt("id_operacao"));
            
            // --- 1. Monta o CLIENTE ---
            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("id_cliente"));
            cliente.setNome(rs.getString("nome"));
            // Se tiver telefone ou cpf, adicione aqui: cliente.setCpf(rs.getString("cpf"));

            // --- 2. Monta o VEÍCULO e coloca o CLIENTE dentro ---
            Veiculo veiculo = new Veiculo();
            veiculo.setId(rs.getInt("id_veiculo")); 
            veiculo.setPlaca(rs.getString("placa")); 
            veiculo.setModelo(rs.getString("modelo")); 
            veiculo.setCor(rs.getString("cor"));
            
            veiculo.setCliente(cliente); // <--- AQUI ESTAVA FALTANDO!
            
            op.setVeiculo(veiculo); 
            // ----------------------------------------------

            // --- 3. Monta a VAGA ---
            Vaga v = new Vaga();
            v.setId(rs.getInt("id_vaga"));
            v.setStatus(rs.getBoolean("status_vaga"));
            op.setVaga(v);

            // --- 4. Monta os HORÁRIOS ---
            Timestamp tsEntrada = rs.getTimestamp("horario_entrada");
            if (tsEntrada != null) {
                op.setHorarioEntrada(tsEntrada.toLocalDateTime());
            }

            Timestamp tsSaida = rs.getTimestamp("horario_saida");
            if (tsSaida != null) {
                op.setHorarioSaida(tsSaida.toLocalDateTime());
                op.setValorTotal(rs.getDouble("valor_total"));
            } else {
                op.setHorarioSaida(null);
                op.setValorTotal(0.0);
            }

            op.setValorHora(rs.getDouble("valor_hora"));
        }
    } catch (SQLException e) {
        System.out.println("Erro ao buscar operacao completa: " + e);
    }
    return op;
}

public List<Operacao> buscar(String termoBusca) {
        List<Operacao> lista = new ArrayList<>();
        
      
        String sql = """
                     SELECT o.id_operacao, o.horario_entrada, o.horario_saida, o.valor_total, v.placa, v.modelo, vg.id_vaga, vg.status AS status_vaga 
                     FROM operacao AS o 
                     INNER JOIN veiculo AS v ON o.id_veiculo = v.id_veiculo 
                     INNER JOIN vaga AS vg ON o.id_vaga = vg.id_vaga
                     WHERE CAST(o.id_operacao AS CHAR) LIKE ?
                     OR LOWER(v.placa) LIKE LOWER(?)
                     OR CAST(vg.id_vaga AS CHAR) LIKE ?
                     ORDER BY o.id_operacao DESC
                     """;

        try (java.sql.Connection con = com.luizfrancisco.estacionamento.database.Conexao.getConnection(); 
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

      
            ps.setString(1, "%" + termoBusca + "%"); 
            ps.setString(2, "%" + termoBusca + "%"); 
            ps.setString(3, "%" + termoBusca + "%"); 

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Operacao operacao = new Operacao();
                    operacao.setId_operacao(rs.getInt("id_operacao"));
                    operacao.setHorarioEntrada(rs.getTimestamp("horario_entrada").toLocalDateTime());

                    java.sql.Timestamp tsSaida = rs.getTimestamp("horario_saida");
                    if (tsSaida != null) {
                        operacao.setHorarioSaida(tsSaida.toLocalDateTime());
                        operacao.setValorTotal(rs.getDouble("valor_total"));
                    } else {
                        operacao.setHorarioSaida(null);
                        operacao.setValorTotal(0.0);
                    }

                    Veiculo veiculo = new Veiculo();
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setModelo(rs.getString("modelo"));
                    operacao.setVeiculo(veiculo);

                    Vaga vaga = new Vaga();
                    vaga.setId(rs.getInt("id_vaga"));
                    vaga.setStatus(rs.getBoolean("status_vaga"));
                    operacao.setVaga(vaga);

                    lista.add(operacao);
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao buscar operação: " + e);
        }
        return lista;
    }

    public boolean verificaVeiculoEstacionado(String placaDoVeiculo){
        String sql = "SELECT COUNT(*) " +
                 "FROM operacao o " +
                 "INNER JOIN veiculo v ON o.id_veiculo = v.id_veiculo " +
                 "WHERE v.placa = ? AND o.horario_saida IS NULL";
        
        boolean isEstacionado = false;
        try(Connection con = Conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);){
          
            ps.setString(1, placaDoVeiculo);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    isEstacionado = rs.getInt(1) > 0;
                }
            }
        }catch(SQLException e){
            System.out.println("ERRO ao verificar veículo estacionado -> " + e);
        }
        
        return isEstacionado;
    }

}
