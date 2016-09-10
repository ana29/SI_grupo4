package controllers;

import models.Arquivo;
import models.Usuario;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by mateusmorais on 09/09/16.
 */
public class FileController extends Controller{

    //pega os usuarios cadastrados do sistema
    private List<Usuario> usuariosCadastrados;
    //pega os arquivos salvos;
    private List<Arquivo> arquivos;

    public void notificaCompartilhamento() {
        int indice = usuariosCadastrados.size();
        for (Arquivo arquivo : arquivos) {
            for (Usuario usuario : usuariosCadastrados) {
                if (usuario.getEmail().equals(arquivo.getCompartilhadosEdicao().get(indice))){
                    //Aqui o arquivo será adicionado na pasta de compartilhados
                    usuario.getCaixaDeNotificacao().notificaEdicao(arquivo.getNomeDono());
                } else if (usuario.getEmail().equals(arquivo.getCompartilhadosLeitura().get(indice))){
                    //Aqui o arquivo será adicionado na pasta de compartilhados
                    usuario.getCaixaDeNotificacao().notificaLeitura(arquivo.getNomeDono());
                }
                indice--;
            }
        }
    }

}
