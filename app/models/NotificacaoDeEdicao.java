package models;

/**
 * Created by Manuel Neto on 22/08/2016.
 */
public class NotificacaoDeEdicao implements Notificacao {

    public String remetente;

    public NotificacaoDeEdicao(String nomeRemetente){
        this.remetente = nomeRemetente;
    }
    public String getMensagem(){
        return this.remetente + " lhe convidou para fazer a leitura de um arquivo";
    }
}
