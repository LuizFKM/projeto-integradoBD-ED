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
 * @author User
 */
public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/Estacionamento";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    public static Connection getConnection(){
        try{
            
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(SQLException e){
            throw new RuntimeException("Não foi possível conectar ao banco de dados -> ERO " + e);
        }
    }
}
