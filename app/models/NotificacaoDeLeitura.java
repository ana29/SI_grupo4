package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Manuel Neto on 19/08/2016.
 */

public class NotificacaoDeLeitura extends Model implements Notificacao{

    public String remetente;

    public NotificacaoDeLeitura(String nomeRemetente){
        this.remetente = nomeRemetente;
    }
    public String getMensagem(){
        return this.remetente + " lhe convidou para visualizar um arquivo";
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }
}
