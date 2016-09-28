package models;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 10/09/2016.
 */
public class ArquivoMd implements Arquivo {

    public String nomeArquivo;
    private String conteudoFile;
    private final String EXTENSAO = ".md";
    private Diretorio pastaPessoal;
    private List<String> compartilhadosEdicao;
    private List<String> compartilhadosLeitura;

    public ArquivoMd(){}

    public ArquivoMd(String nome, String conteudo){
        this.nomeArquivo = nome;
        this.conteudoFile = conteudo;
        this.pastaPessoal = new Diretorio("root", "/root");
        this.compartilhadosEdicao = new ArrayList<>();
        this.compartilhadosLeitura = new ArrayList<>();
        criarArquivo();
    }
    @Override
    public void criarArquivo() {

        File arquivo = new File(nomeArquivo+EXTENSAO);
        try(FileWriter escrever = new FileWriter(arquivo)){
            escrever.write((String) conteudoFile);
            escrever.close();
        }
        catch(Exception erro){
           erro.getCause();
        }
    }

    @Override
    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    @Override
    public String getConteudoArquivo() {
        return conteudoFile;
    }

    @Override
    public void deletaArquivoSistema(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        arquivo.delete();
    }

    @Override
    public String getExtensao(){return EXTENSAO;}

    @Override
    public List<String> getCompartilhadosEdicao() {
        return compartilhadosEdicao;
    }

    @Override
    public List<String> getCompartilhadosLeitura() {
        return compartilhadosLeitura;
    }

}