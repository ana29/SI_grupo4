package models;

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
    String getconteudoFile();
    void compartilharEdicao(String emailUsuario);
    void compartilharLeitura(String emailUsuario);
    List<String> getCompartilhadosEdicao();
    List<String> getCompartilhadosLeitura();

    String getNomeDono();
}
