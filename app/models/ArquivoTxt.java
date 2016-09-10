package models;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by AnaGodoy on 05/08/16.
 * Implementacao da interface Arquivo para a criacao de arquivos no formato .txt
 */
public class ArquivoTxt implements Arquivo{

    public String nomeArquivo;
    public String conteudoFile;
    public Diretorio pastaPessoal;
    public Diretorio compartilhados;
    private List<String> compartilhamentoEdicao;
    private List<String> compartilhamentoLeitura;

    public  ArquivoTxt(){


    }

    public  ArquivoTxt(String nomeArquivo, String conteudoFile){
        this.nomeArquivo = nomeArquivo;
        this.conteudoFile = conteudoFile;
        this.pastaPessoal = new Diretorio("root");
        this.compartilhamentoEdicao = new ArrayList<>();
        this.compartilhamentoLeitura= new ArrayList<>();
        this.compartilhados = new Diretorio();
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

    @Override
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public  String getconteudoFile(){return conteudoFile;}

    @Override
    public void compartilharEdicao(String emailUsuario){
        compartilhamentoEdicao.add(emailUsuario);
    }

    @Override
    public void compartilharLeitura(String emailUsuario){
        compartilhamentoEdicao.add(emailUsuario);
    }

    @Override
    public List<String> getCompartilhadosEdicao() {
        return compartilhamentoEdicao;
    }

    @Override
    public List<String> getCompartilhadosLeitura() {
        return compartilhamentoLeitura;
    }

    @Override
    public String getNomeDono() {
        //Pegar usuario cadastrado atualmente
        return "teste";
    }
}
