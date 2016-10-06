package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 02/08/2016.
 */
@Entity
public class Diretorio extends Model{

    @Id @GeneratedValue
    private Long id;


    private String nome;
    private List<Diretorio> subDiretorios;
    private List<Arquivo> arquivos;

    public Diretorio(String nome){
        setNome(nome);
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public Diretorio(){
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
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

    public String getNome(){
        return this.nome;
    }

    public List<Diretorio> getSubDiretorios(){
        return this.subDiretorios;
    }

    public void setSubDiretorios(List<Diretorio> subDiretorios) {
        this.subDiretorios = subDiretorios;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }
}
