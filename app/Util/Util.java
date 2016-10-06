package Util;

import models.Diretorio;
import models.Usuario;

public class Util {

    public static boolean validaCredenciais(String nome, String email, String senha){
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2)
            if (nome.length() < 21) if (senha.length() > 7) if (userMail.validate(email)) return true;
        return false;
    }

    public static void saveUsuario(Usuario usuario){
        usuario.save();
    }

    public static void updateUsuario(Usuario usuario){
        usuario.update();
    }

    public static void deleteUsuario(Usuario usuario){
        usuario.delete();
    }

    public static void saveDiretorio(Diretorio dir){
        dir.save();
    }

    public static void updateDiretorio(Diretorio dir){
        dir.update();
    }

    public static void deleteDiretorio(Diretorio dir){
        dir.delete();
    }

}
