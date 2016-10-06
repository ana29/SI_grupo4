package models;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import akka.stream.javadsl.Zip;


/**
 * Created by MateusMorais on 02/10/16.
 * Implementação da interface Arquivo para a criação de arquivos no formato .gz
 */
public class ArquivoGzip implements Arquivo {

    public String nomeArquivo;
    private final String EXTENSAO = ".gz";
    public Diretorio pastaPessoal;
    private List<String> compartilhadosEdicao;
    private List<String> compartilhadosLeitura;
    private File arquivo;

    public ArquivoGzip() {

    }

    public ArquivoGzip(File arquivo) {
        this.nomeArquivo = arquivo.getName();
        this.pastaPessoal = new Diretorio("root","/root");
        this.compartilhadosEdicao = new ArrayList<>();
        this.compartilhadosLeitura = new ArrayList<>();
        this.arquivo = arquivo;
        criarArquivo();
    }

    @Override
    public void criarArquivo(){
        try {
            GZIPOutputStream saidaZip = new GZIPOutputStream(new FileOutputStream(arquivo.getAbsolutePath()+EXTENSAO));
            byte[] out = arquivo.getAbsolutePath().getBytes("ISO8859_1"); //Codifica o arquivo em utf-8
            saidaZip.write(out, 0, out.length);
            saidaZip.finish();
            saidaZip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNomeArquivo(){
        return nomeArquivo;
    }

    @Override
    public String getConteudoArquivo(){
        return "null";
    }

    @Override
    public void deletaArquivoSistema(String nomeArquivo){
        arquivo.delete();
    }

    @Override
    public String getExtensao(){
        return EXTENSAO;
    }

    @Override
    public File getFile(){
        return arquivo;
    }

    @Override
    public List<String> getCompartilhadosEdicao(){
        return compartilhadosEdicao;
    }

    @Override
    public List<String> getCompartilhadosLeitura(){
        return compartilhadosLeitura;
    }
}