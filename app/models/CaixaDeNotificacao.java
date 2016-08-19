package models;

import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Neto on 19/08/2016.
 */
public class CaixaDeNotificacao {

    @Constraints.Required
    private List<Notificacao> caixaDeNotificacao;

    public CaixaDeNotificacao(){
        this.caixaDeNotificacao = new ArrayList<>();
        Notificacao not = new Notificacao();
        caixaDeNotificacao.add(not);
//        caixaDeNotificacao.add("Mensagem 1");
//        caixaDeNotificacao.add("Mensagem 2");

    }

    public List<Notificacao> getCaixaDeNotificacao() {
        return this.caixaDeNotificacao;
    }

    @Override
    public String toString() {
        return "Hello";
    }
}
