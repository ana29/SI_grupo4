package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 02/08/2016.
 */
public class Diretorio {
    private String nome;
    private List<Diretorio> subDiretorios;
    private List<Diretorio> arquivos;

    public Diretorio(String nome){
        this.nome = nome;
        this.subDiretorios = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }



    public String getNome(){
        return this.nome;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Diretorio){
            Diretorio obj = (Diretorio) o;
            return this.nome.equals(obj.getNome());
        }
        return false;
    }

    public List<Diretorio> getSubDiretorios(){
        return this.subDiretorios;
    }

    public List<Diretorio> getArquivos() {
        return arquivos;
    }

}
