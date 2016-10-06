package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 10/09/2016.
 */

//@Entity
public class ArquivoMd extends Model implements Arquivo{

    private String nomeArquivo;
    private String conteudoFile;
    private final String EXTENSAO = ".md";
    private Diretorio pastaPessoal;


    private List<String> compartilhadosEdicao;
    private List<String> compartilhadosLeitura;
    private File arquivo;

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

        arquivo = new File(nomeArquivo+EXTENSAO);
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
    public void deletaArquivoSistema(String nome) {

        File arquivo = new File(nome+EXTENSAO);
        arquivo.delete();

    }

    @Override
    public String getExtensao(){return EXTENSAO;}

    @Override
    public File getFile() {
        return arquivo;
    }

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
//        return "ArquivoMd{" +
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