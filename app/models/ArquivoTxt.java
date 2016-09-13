package models;

import javax.swing.*;
import java.io.*;
import java.util.Formatter;


/**
 * Created by AnaGodoy on 05/08/16.
 * Implementacao da interface Arquivo para a criacao de arquivos no formato .txt
 */
public class ArquivoTxt implements Arquivo{

    public String nomeArquivo;
    private final String EXTENSAO = ".txt";
    private String conteudoFile;
    public Diretorio pastaPessoal;

    public  ArquivoTxt(){


    }

    public  ArquivoTxt(String nomeArquivo, String conteudoFile){
        this.nomeArquivo = nomeArquivo;
        this.conteudoFile = conteudoFile;
        this.pastaPessoal = new Diretorio("root");
        criarArquivo();
    }

    /**
     * Abre o arquivo pelo nome e retorna uma string com o conteudo;
     * @param nomeArquivo
     * @return
     */
    public String getConteudoArquivo(String nomeArquivo){
        String conteudoArquivo = null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
            while(br.ready()){
                String linha = br.readLine();
                conteudoArquivo+=linha;
            }
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        return conteudoArquivo;
    }
    /**
     * Metodo que cria arquivos .txt Retorna um erro casa um dos parametros ou ambos sejam null;
     */
    @Override
    public void criarArquivo() {

            File arquivo = new File(getNomeArquivo());
            try(FileWriter escrever = new FileWriter(arquivo)){
                escrever.write((String) getConteudoArquivo());
                escrever.close();

            }
            catch(Exception erro){
            erro.getCause();
            }

        }

    public void deletaArquivoSistema(String nomeArquivo){
        File arquivo = new File(nomeArquivo);
        arquivo.delete();

    }

    @Override
    public String getExtensao() {
        return null;
    }

    @Override
    public String getNomeArquivo() {return this.nomeArquivo;}

    @Override
    public String getConteudoArquivo() {return  this.conteudoFile;}

    @Override
    public String getNomeComExtensao() {
        return getNomeArquivo()+".txt";
    }
/*
public  String getNomeArquivo(){return nomeArquivo;}
    public String getExtensao(){return EXTENSAO;}
    public  String getconteudoFile(){return conteudoFile;}
*/

    @Override
    public void setConteudoArquivo(String novoConteudo) {this.conteudoFile=novoConteudo;}

}
