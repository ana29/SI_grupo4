package models;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import akka.stream.javadsl.Zip;


/**
 * Created by MateusMorais on 02/10/16.
 * Implementação da interface Arquivo para a criação de arquivos no formato .zip
 */
public class ArquivoZip implements Arquivo {

    public String nomeArquivo;
    private final String EXTENSAO = ".zip";
    public Diretorio pastaPessoal;
    private List<String> compartilhadosEdicao;
    private List<String> compartilhadosLeitura;
    private File arquivo;

    public ArquivoZip() {

    }

    public ArquivoZip(File arquivo) {
        this.nomeArquivo = arquivo.getName();
        this.pastaPessoal = new Diretorio("root");
        this.compartilhadosEdicao = new ArrayList<>();
        this.compartilhadosLeitura = new ArrayList<>();
        this.arquivo = arquivo;
        criarArquivo();
    }

    @Override
    public void criarArquivo(){
        int cont;
        int BUFFER = 4096;
        byte[] dados = new byte[BUFFER];

        BufferedInputStream origem = null;
        FileInputStream streamDeEntrada = null;
        FileOutputStream destino = null;
        ZipOutputStream saida = null;
        ZipEntry entry = null;

        try {
            destino = new FileOutputStream(new File(arquivo.getAbsolutePath()+EXTENSAO));
            saida = new ZipOutputStream(new BufferedOutputStream(destino));
            streamDeEntrada = new FileInputStream(arquivo);
            origem = new BufferedInputStream(streamDeEntrada, BUFFER);
            entry = new ZipEntry(arquivo.getName());
            saida.putNextEntry(entry);

            while((cont = origem.read(dados, 0, BUFFER)) != -1) {
                saida.write(dados, 0, cont);
            }

            origem.close();
            saida.close();
        } catch(IOException e) {
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