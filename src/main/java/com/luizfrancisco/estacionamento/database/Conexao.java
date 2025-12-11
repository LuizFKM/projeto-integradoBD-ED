/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author luizfkm
 */
public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/estacionamento";
    private static String USER = "root";
    private static String PASSWORD = "root";
    
    public static void definirCredenciais(String usuario, String senha){
        USER = usuario;
        PASSWORD = senha;
    }
    
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL,USER,PASSWORD);
        }catch (SQLException e){
            throw new RuntimeException("Nao foi possivel conectar no banco de dados -> ERRO " + e);
        }
    }
}
