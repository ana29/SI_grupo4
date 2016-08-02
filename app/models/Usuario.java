package models;

public class Usuario {

    public String nome;
    public String email;
    public String senha;

    public Usuario(){

    }

    public Usuario(String nome, String email, String senha){

        this.nome = nome;
        this.email = email;
        this.senha = senha;

    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

