/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.luizfrancisco.estacionamento.util;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTextField;


/**
 *
 * @author User
 */
public class ValidadorDeCampos {
    public static boolean temCampoVazio(JTextField campo, String nomeCampo){
        if(campo.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "O campo " + nomeCampo + " é obrigatório");
            campo.setForeground(Color.red);
            campo.requestFocus();
            return true;
        }
        
        campo.setBackground(Color.WHITE);
        return false;
    }
    
    public static boolean isEmailInvalido(JTextField campoEmail){
        String email = campoEmail.getText().trim();
        if(!email.contains("@") || !email.contains(".")){
            JOptionPane.showMessageDialog(null, "Digite um e-mail válido");
            campoEmail.setBackground(new Color(255, 200, 200));
            campoEmail.requestFocus();
            return true;
        }
        campoEmail.setBackground(Color.WHITE);
        return false;
    }
}
