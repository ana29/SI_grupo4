package models;

import com.avaje.ebean.Model;
import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by AnaGodoy on 05/08/16.
 * Foi escolhido fazer uma interface para a criacao de arquivos, pois futuramente o projeto deve aceitar outro formato alem de .txt
 */

public interface Arquivo {

    /**
     * metodo para  criar  Arquivos;
     */
    void criarArquivo();
    String getNomeArquivo();
    String getConteudoArquivo();
    void deletaArquivoSistema(String nomeArquivo);
    String getExtensao();

    File getFile();


    List<String> getCompartilhadosEdicao();

    List<String> getCompartilhadosLeitura();

}
