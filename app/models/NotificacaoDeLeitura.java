package models;

/**
 * Created by Manuel Neto on 19/08/2016.
 */
public class NotificacaoDeLeitura implements Notificacao{

    public String remetente;

    public NotificacaoDeLeitura(String nomeRemetente){
        this.remetente = nomeRemetente;
    }
    public String getMensagem(){
        return this.remetente + " lhe convidou para editar um arquivo";
    }
}