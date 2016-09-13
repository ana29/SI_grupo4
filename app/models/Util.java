package models;

public class Util {

    public boolean validaCredenciais(String nome, String email, String senha){
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2)
            if (nome.length() < 21) if (senha.length() > 7) if (userMail.validate(email)) return true;
        return false;
    }

}