package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marco on 02/08/2016.
 */
@Entity
public class Diretorio extends Model{

    @Id @GeneratedValue
    private Long id;
    public String nome;
    public String caminho;
    public List<Diretorio> subDiretorios;
    private List<Arquivo> arquivos;

    public Diretorio(String nome, String caminho){

        setNome(nome);
        this.caminho = caminho;
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public Diretorio(){
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public String getCaminho() { return this.caminho; }

    public String getRaiz() {
        if (this.nome == "root"){
            return this.caminho;
        }
        else {
            return caminho.substring(0, caminho.lastIndexOf("/"));
        }
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

    //Busca o diretório pelo nome na lista de subDiretorios(apenas no "nível 1")
    public Diretorio buscaNome(String nomePasta){
        for (Diretorio d: this.getSubDiretorios()){
            if (d.getNome().equals(nomePasta)){
                return d;
            }
        }
        return null;
    }

    //Busca recursivamente o diretório pelo caminho
    public Diretorio buscaPorCaminho(String[] caminho) {
        if (caminho.length == 2) {
            return buscaNome(caminho[1]);
        } else {
            Diretorio dir = buscaNome(caminho[1]);
            return dir.buscaPorCaminho(Arrays.copyOfRange(caminho, 1, caminho.length));
        }
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
