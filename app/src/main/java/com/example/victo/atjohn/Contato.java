package com.example.victo.atjohn;

import java.io.Serializable;

/**
 * Created by victo on 29/11/2017.
 */

public class Contato implements Serializable{

    String Nome;
    String Login;
    String Senha;
    String ConfirmarSenha;
    String Cpf;

    public Contato (String nome, String login, String senha, String confirmarSenha, String cpf){

        Nome = nome;
        Login = login;
        Senha = senha;
        ConfirmarSenha = confirmarSenha;
        Cpf = cpf;

    }

}
