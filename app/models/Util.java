package models;

import akka.stream.javadsl.Zip;

import java.io.File;
import java.util.zip.*;

public class Util {

    public boolean validaCredenciais(String nome, String email, String senha){
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2)
            if (nome.length() < 21) if (senha.length() > 7) if (userMail.validate(email)) return true;
        return false;
    }

    public void comprimeZip(File objeto){
        //Verificar qual método de zip faz a compressão

    }

    public void comprimeGzip(File nomeObjeto){

    }

}
