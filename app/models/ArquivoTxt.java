package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


/**
 * Created by AnaGodoy on 05/08/16.
 * Implementacao da interface Arquivo para a criacao de arquivos no formato .txt
 */


public class ArquivoTxt extends Model implements Arquivo{

    private String nomeArquivo;
    private String conteudoFile;
    private final String EXTENSAO = ".txt";

    private Diretorio pastaPessoal;

    private List<String> compartilhadosEdicao;
    private List<String> compartilhadosLeitura;

    public  ArquivoTxt(){


    }

    public  ArquivoTxt(String nomeArquivo, String conteudoFile){
        this.nomeArquivo = nomeArquivo;
        this.conteudoFile = conteudoFile;
        this.pastaPessoal = new Diretorio("root");
        this.compartilhadosEdicao = new ArrayList<>();
        this.compartilhadosLeitura = new ArrayList<>();
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

            File arquivo = new File(getNomeArquivo()+EXTENSAO);
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
    public String getExtensao(){return EXTENSAO;}

    @Override
    public String getNomeArquivo() {return this.nomeArquivo;}

    @Override
    public String getConteudoArquivo() {return  this.conteudoFile;}

    @Override
    public List<String> getCompartilhadosEdicao() {
        return compartilhadosEdicao;
    }

    @Override
    public List<String> getCompartilhadosLeitura() {
        return compartilhadosLeitura;
    }

//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public String toString() {
//        return "ArquivoTxt{" +
//                "id=" + id +
//                ", nomeArquivo='" + nomeArquivo + '\'' +
//                ", conteudoFile='" + conteudoFile + '\'' +
//                ", EXTENSAO='" + EXTENSAO + '\'' +
//                ", pastaPessoal=" + pastaPessoal +
//                ", compartilhadosEdicao=" + compartilhadosEdicao +
//                ", compartilhadosLeitura=" + compartilhadosLeitura +
//                '}';
//    }
}
