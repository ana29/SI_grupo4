package models;

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
    String getNomeComExtensao();
    void setConteudoArquivo(String novoConteudo);
    void deletaArquivoSistema(String nomeArquivo);


}
