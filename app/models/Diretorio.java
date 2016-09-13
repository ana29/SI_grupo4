package models;

import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 02/08/2016.
 */
public class Diretorio {

    @Constraints.Required
    public  String nome;
    public List<Diretorio> subDiretorios;
    public List<Arquivo> arquivos;


    public Diretorio(String nome){
        this.nome = nome;
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public Diretorio(){
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public String getNome(){
        return this.nome;
    }

    public List<Diretorio> getSubDiretorios(){
        return this.subDiretorios;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public boolean containsDiretorio(String nome){
        for (Diretorio d: this.subDiretorios){
            if (d.getNome().equals(nome)){
                return true;
            }
        }
        return false;
    }

    public boolean containsArquivo(String nome, String extensao){
        for (Arquivo a: this.getArquivos()){
            if (a.getNomeArquivo().equals(nome) && a.getExtensao().equals(extensao)){
                return true;
            }
        }
        return false;
    }
}