package models;

import java.util.logging.Logger;

public class Usuario {

    public String nome;
    public String email;
    public String senha;
    public Diretorio pastaPessoal;
    public CaixaDeNotificacao caixaDeNotificacao;
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    public Usuario(){
        this.pastaPessoal = new Diretorio("root");
        this.caixaDeNotificacao = new CaixaDeNotificacao();
    }

    public Usuario(String nome, String email, String senha){
        this.caixaDeNotificacao = new CaixaDeNotificacao();
        this.pastaPessoal = new Diretorio("root");
        this.nome = nome;
        this.email = email;
        this.senha = senha;

    }

    public void criaSubDiretorio(String nome){
        LOGGER.info("ENTROU NA CRIAÇÃO DO DIRETORIO");
        if (!pastaPessoal.contains(nome)){
            pastaPessoal.getSubDiretorios().add(new Diretorio(nome));
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (adicionado == false){
                String novoNome = nome + "(" + count + ")";
                if (!pastaPessoal.contains(novoNome)){
                    pastaPessoal.getSubDiretorios().add(new Diretorio(novoNome));
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    public  void addArquivo(String nomeArquivo,String conteudoFile){
        if (!pastaPessoal.getArquivos().contains(new ArquivoTxt(nomeArquivo, conteudoFile))){
            pastaPessoal.getArquivos().add(new ArquivoTxt(nomeArquivo, conteudoFile));
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (adicionado == false){
                String novoNome = nomeArquivo + "(" + count + ")";
                if (!pastaPessoal.getArquivos().contains(new ArquivoTxt(novoNome, conteudoFile))){
                    pastaPessoal.getArquivos().add(new ArquivoTxt(novoNome, conteudoFile));
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

    public CaixaDeNotificacao getCaixaDeNotificacao() {
        return caixaDeNotificacao;
    }
}

