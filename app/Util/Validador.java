package Util;

import models.Usuario;

import java.sql.Timestamp;
import java.util.Random;

import static play.mvc.Controller.session;

/**
 * Created by marco on 01/10/2016.
 */
public class Validador {
    private Usuario usuarioLogado;

    public Validador(Usuario usuario){
        this.usuarioLogado = usuario;
    }

    public String geraToken(){
        Random rand = new Random();
        String token = Long.toHexString(rand.nextLong()) + Long.toHexString(rand.nextLong());
        System.out.println(token);
        return token;
    }

    public boolean autenticaUsuario() throws Exception{

        if(session("token") != null && verificaTimeStampDoToken()){
            return true;
        }else{
            throw new Exception("Você não está autenticado! Realize o login.");
        }

    }

    //Validacao

    private boolean verificaTimeStampDoToken(){

        //horaAtual -> pega a hora da realizacao de determinada requisição
        Timestamp horaAtual = new Timestamp(System.currentTimeMillis());
        //diasLong -> será a diferenca entra a hr que logou e a hora da requisição
        Long diasLong = horaAtual.getTime() - usuarioLogado.getHoraDoLogin().getTime();
        //dias -> converte diasLong para a quantidade de dias
        long dias = diasLong/(1000*60*60*24);

        if(dias > 1){
            return false;
        }
        return true;
    }

}
