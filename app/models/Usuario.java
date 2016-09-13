package models;

import java.util.logging.Logger;

public class Usuario {

    public String nome;
    public String email;
    public String senha;
    public Diretorio pastaPessoal;
    public Diretorio compartilhados;
    public CaixaDeNotificacao caixaDeNotificacao;
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    public Usuario(){
        this.pastaPessoal = new Diretorio("root");
        this.compartilhados = new Diretorio("Compartilhados");
        this.caixaDeNotificacao = new CaixaDeNotificacao();
    }

    public Usuario(String nome, String email, String senha){
        this.caixaDeNotificacao = new CaixaDeNotificacao();
        this.pastaPessoal = new Diretorio("root");
        this.compartilhados = new Diretorio("Compartilhados");
        this.nome = nome;
        this.email = email;
        this.senha = senha;

    }

    public void criaSubDiretorio(String nome){
        LOGGER.info("ENTROU NA CRIAÇÃO DO DIRETORIO");
        if (!pastaPessoal.containsDiretorio(nome)){
            pastaPessoal.getSubDiretorios().add(new Diretorio(nome));
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = nome + "(" + count + ")";
                if (!pastaPessoal.containsDiretorio(novoNome)){
                    pastaPessoal.getSubDiretorios().add(new Diretorio(novoNome));
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    public  void addArquivo(String nomeArquivo, String conteudoFile, String extensao){
        if (!pastaPessoal.containsArquivo(nomeArquivo, extensao)){
            auxExtensao(nomeArquivo, conteudoFile, extensao);
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = nomeArquivo+ "(" + count + ")";
                if (!pastaPessoal.containsArquivo(novoNome, extensao)){
                    auxExtensao(novoNome, conteudoFile, extensao);
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    private void auxExtensao(String nomeArquivo, String conteudoFile, String extensao) {
        if (extensao.equals(".txt")){
            pastaPessoal.getArquivos().add(new ArquivoTxt(nomeArquivo, conteudoFile));
        }
        else{
            pastaPessoal.getArquivos().add(new ArquivoMd(nomeArquivo, conteudoFile));

        }
    }

    public void excluirSubDiretorio(String nome){
        for (Diretorio d: pastaPessoal.getSubDiretorios()) {
            if (d.getNome().equals(nome)){
                pastaPessoal.getSubDiretorios().remove(d);
            }
        }
    }

    public void excluirArquivo(String nome){
        if (pastaPessoal.containsArquivo(nome, ""))
                pastaPessoal.getArquivos().remove(nome);
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

    public Diretorio getCompartilhados(){ return compartilhados;}

    public CaixaDeNotificacao getCaixaDeNotificacao() {
        return caixaDeNotificacao;
    }
}

