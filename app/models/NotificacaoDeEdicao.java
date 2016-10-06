package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Manuel Neto on 22/08/2016.
 */


public class NotificacaoDeEdicao extends Model implements Notificacao {

    public String remetente;

    public NotificacaoDeEdicao(String nomeRemetente){
        this.remetente = nomeRemetente;
    }
    public String getMensagem(){
        return this.remetente + " lhe convidou para editar um arquivo";
    }
//    public String getRemetente() {
//        return remetente;
//    }
}
