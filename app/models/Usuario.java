package models;

public class Usuario {

    public String nome;
    public String email;
    public String senha;
    public Diretorio pastaPessoal;

    public Usuario(){

    }

    public Usuario(String nome, String email, String senha){
        this.pastaPessoal = new Diretorio("root");
        this.nome = nome;
        this.email = email;
        this.senha = senha;

    }

    public void criaSubDiretorio(String nome){
        if (!pastaPessoal.getSubDiretorios().contains(nome)){
            pastaPessoal.getSubDiretorios().add(new Diretorio(nome));
        }
        else{
            boolean adicionado = false;
            int count = 0;
            while (adicionado == false){
                String novoNome = nome + "(" + count + ")";
                if (!pastaPessoal.getSubDiretorios().contains(novoNome)){
                    pastaPessoal.getSubDiretorios().add(new Diretorio(novoNome));
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    public void excluirSubDiretorio(String nome){
        for (Diretorio d: pastaPessoal.getSubDiretorios()) {
            if (d.getNome().equals(nome)){
                pastaPessoal.getSubDiretorios().remove(d);
            }
        }
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

    public Diretorio getPastaPessoal() {
        return pastaPessoal;
    }
}

