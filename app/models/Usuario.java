package models;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.logging.Logger;

public class Usuario {

    private final String LIXEIRA = "/root/Lixeira";

    public String nome;
    public String email;
    public String senha;
    public Diretorio pastaPessoal;
    public Diretorio comprimidos;
    public Diretorio lixeira;
    public Diretorio compartilhados;
    public CaixaDeNotificacao caixaDeNotificacao;
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());
    public Timestamp horaDoLogin;

    public Usuario(){
        this.pastaPessoal = new Diretorio("root", "/root");
        this.compartilhados = new Diretorio("Compartilhados", "/root/Compartilhados");
        this.lixeira = new Diretorio("Lixeira", LIXEIRA);
this.comprimidos = new Diretorio("Comprimidos", "/root");
        this.caixaDeNotificacao = new CaixaDeNotificacao();

    }

    public Usuario(String nome, String email, String senha){
        this.caixaDeNotificacao = new CaixaDeNotificacao();
        this.pastaPessoal = new Diretorio("root", "/root");
        this.compartilhados = new Diretorio("Compartilhados", "/root/Compartilhados");
        this.lixeira= new Diretorio("Lixeira", LIXEIRA);
        this.comprimidos = new Diretorio("Comprimidos", "/root");
        this.nome = nome;
        this.email = email;
        this.senha = senha;

    }

    public void criaSubDiretorio(String nome, String caminhoDiretorio){
        LOGGER.info("ENTROU NA CRIAÇÃO DO DIRETORIO");
        Diretorio diretorio = buscaDiretorio(caminhoDiretorio);
        if (caminhoDiretorio.equals(LIXEIRA)){
            Diretorio dir = new Diretorio(nome, caminhoDiretorio);
            lixeira.subDiretorios.add(dir);
        }else if(!diretorio.containsDiretorio(nome)){
            diretorio.getSubDiretorios().add(new Diretorio(nome, diretorio.getCaminho()+"/"+nome));
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = nome + "(" + count + ")";
                if (!diretorio.containsDiretorio(novoNome)){
                    diretorio.getSubDiretorios().add(new Diretorio(novoNome, diretorio.getCaminho()+"/"+novoNome));
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    public void addArquivo(String nomeArquivo, String conteudoFile, String extensao, String caminhoDiretorio) {
        Diretorio diretorio;
        if (caminhoDiretorio.equals(LIXEIRA)){
            diretorio = getLixeira();
         }else{
            diretorio= buscaDiretorio(caminhoDiretorio);
         }
        if (!diretorio.containsArquivo(nomeArquivo, extensao)){
            auxExtensao(nomeArquivo, conteudoFile, extensao, diretorio);
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = nomeArquivo+ "(" + count + ")";
                if (!diretorio.containsArquivo(novoNome, extensao)){
                    auxExtensao(novoNome, conteudoFile, extensao, diretorio);
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    private void auxExtensao(String nomeArquivo, String conteudoFile, String extensao, Diretorio diretorio) {
        if (extensao.equals(".txt")){
            diretorio.getArquivos().add(new ArquivoTxt(nomeArquivo, conteudoFile));
        }
        else{
            diretorio.getArquivos().add(new ArquivoMd(nomeArquivo, conteudoFile));

        }
    }

    public void addComprimidos(Arquivo arquivo){
        comprimidos.getArquivos().add(arquivo);
    }

    public Diretorio getComprimidos(){
        return comprimidos;
    }

    public void excluirSubDiretorio(String nome, Diretorio dir){
        for (int i = 0; i < dir.getSubDiretorios().size(); i++) {
            if (dir.getSubDiretorios().get(i).getNome().equals(nome)){
                dir.getSubDiretorios().remove(dir.getSubDiretorios().get(i));
            }
        }
    }

    public void excluirArquivo(String nome,String extensao ,String caminhoDiretorio){
       if (caminhoDiretorio.equals(getCaminhoLixeira())){
           auxDeletaArquivosDaLista(lixeira, nome, extensao);

       }else{
        Diretorio diretorio = buscaDiretorio(caminhoDiretorio);
         auxDeletaArquivosDaLista(diretorio, nome, extensao);

        }
    }

    private void auxDeletaArquivosDaLista(Diretorio dir, String nome, String extensao){
        if (dir.containsArquivo(nome, extensao))
            for (int i = 0; i <dir.getArquivos().size() ; i++) {
                Arquivo arq = dir.getArquivos().get(i);
                if (arq.getNomeArquivo().equals(nome)&&arq.getExtensao().equals(extensao))
                    dir.getArquivos().remove(i);
            }

    }

    public Diretorio buscaDiretorio(String caminhoDiretorio){
        String[] caminho = caminhoDiretorio.split("/");
        if (caminho.length == 2){
            return getPastaPessoal();
        }
        return getPastaPessoal().buscaPorCaminho(Arrays.copyOfRange(caminho, 1, caminho.length));
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

    public Diretorio getLixeira() { return lixeira;}

    public Diretorio getCompartilhados(){ return compartilhados;}

    public CaixaDeNotificacao getCaixaDeNotificacao() {
        return caixaDeNotificacao;
    }

    public Timestamp getHoraDoLogin(){
        return horaDoLogin;
    }

    public void setHoraDoLogin(Timestamp horaDoLogin){
        this.horaDoLogin = horaDoLogin;
    }

    public String getCaminhoLixeira() {
        return "/root/Lixeira";
    }

}

