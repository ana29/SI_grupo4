package models;

import java.io.File;
import java.io.FileWriter;

/**
   * Created by marco on 10/09/2016.
  */

public class ArquivoMd implements Arquivo {
    public String nomeArquivo;
    public String conteudoFile;
    private Diretorio pastaPessoal;

    public ArquivoMd(){

    }
    public ArquivoMd(String nome, String conteudo){
                    this.nomeArquivo = nome;
                    this.conteudoFile = conteudo;
                    this.pastaPessoal = new Diretorio("root");
                    criarArquivo();
    }
    @Override
    public void criarArquivo() {

     File arquivo = new File(getNomeArquivo());
     try(FileWriter escrever = new FileWriter(arquivo)){
         escrever.write((String) getConteudoArquivo());
         escrever.close();

     } catch(Exception erro){
            erro.getCause();
     }
     }
     @Override
     public void deletaArquivoSistema(String nomeArquivo){
         File arquivo = new File(nomeArquivo);
         arquivo.delete();

     }
    @Override
    public String getNomeArquivo() {return this.nomeArquivo;}

    @Override
    public String getConteudoArquivo() {return  this.conteudoFile;}

    @Override
    public String getNomeComExtensao() {
        return getNomeArquivo()+".md";
    }

    @Override
    public void setConteudoArquivo(String novoConteudo) {this.conteudoFile=novoConteudo;}

}

