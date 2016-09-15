package models;

import java.io.File;
import java.io.FileWriter;


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
     * Metodo que cria arquivos .txt Retorna um erro casa um dos parametros ou ambos sejam null;
     */
    @Override
    public void criarArquivo() {

            File arquivo = new File(nomeArquivo+".txt");
            try(FileWriter escrever = new FileWriter(arquivo)){
                escrever.write((String) conteudoFile);
                escrever.close();
                //JOptionPane.showMessageDialog(null,"Arquivo '"+nomeArquivo+"' criado!","Arquivo",1);
            }
            catch(Exception erro){
                //JOptionPane.showMessageDialog(null,"Arquivo nao pode ser gerado!","Erro",0);
            }
        }

    public  String getNomeArquivo(){return nomeArquivo;}
    public String getExtensao(){return EXTENSAO;}
    public  String getconteudoFile(){return conteudoFile;}

}
