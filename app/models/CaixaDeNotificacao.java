package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Neto on 19/08/2016.
 */
public class CaixaDeNotificacao extends Model{

    private List<Notificacao> caixaDeNotificacao;

    public CaixaDeNotificacao(){
        this.caixaDeNotificacao = new ArrayList<>();
//        Notificacao not = new NotificacaoDeLeitura("Marcos");
//        Notificacao not2 = new NotificacaoDeEdicao("Marcos");
//
//        //Só para testar
//        caixaDeNotificacao.add(not);
//        caixaDeNotificacao.add(not2);
////        caixaDeNotificacao.add("Mensagem 1");
////        caixaDeNotificacao.add("Mensagem 2");

    }

    public List<Notificacao> getCaixaDeNotificacao() {
        return this.caixaDeNotificacao;
    }

    public void notificaEdicao(String nome){
        caixaDeNotificacao.add(new NotificacaoDeEdicao(nome));
    }

    public void notificaLeitura(String nome){
        caixaDeNotificacao.add(new NotificacaoDeLeitura(nome));
    }

}
