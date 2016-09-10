package models;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by marco on 10/09/2016.
 */
public class ArquivoMd implements Arquivo {
    public String nomeArquivo;
    private String conteudoFile;
    private Diretorio pastaPessoal;

    public ArquivoMd(){}

    public ArquivoMd(String nome, String conteudo){
        this.nomeArquivo = nome;
        this.conteudoFile = conteudo;
        this.pastaPessoal = new Diretorio("root");
        criarArquivo();
    }
    @Override
    public void criarArquivo() {

        File arquivo = new File(nomeArquivo+".md");
        try(FileWriter escrever = new FileWriter(arquivo)){
            escrever.write((String) conteudoFile);
            escrever.close();
            //JOptionPane.showMessageDialog(null,"Arquivo '"+nomeArquivo+"' criado!","Arquivo",1);
        }
        catch(Exception erro){
            //JOptionPane.showMessageDialog(null,"Arquivo nao pode ser gerado!","Erro",0);
        }
    }

    @Override
    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    @Override
    public String getconteudoFile() {
        return this.conteudoFile;
    }
}
