package controllers;

import models.Diretorio;
import models.EmailValidator;
import models.Usuario;
import models.Arquivo;
import models.ArquivoTxt;
import play.data.FormFactory;
import play.mvc.*;


import views.html.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    @Inject
    private FormFactory formFactory;
    private List<Usuario> listaDeUsuarios = new ArrayList<>();
    private List<ArquivoTxt> listaDeArquivos = new ArrayList<>();
    private Usuario usuarioLogado = null;
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    public Result cadastrarUsuario(){
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
        if (verificaCredenciais(usuario.getNome(), usuario.getEmail(), usuario.getSenha())) {
            listaDeUsuarios.add(usuario);
        }
        return redirect(routes.HomeController.index());
    }

    public Result escreverTexto(){
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
        listaDeUsuarios.add(usuario);

        if (validarLogin(usuario.getEmail(), usuario.getSenha())){
            return redirect(routes.HomeController.chamarHome());
        }
        return redirect(routes.HomeController.index());
    }
    public Result logar(){

        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();

        if (validarLogin(usuario.getEmail(), usuario.getSenha())){
            return redirect(routes.HomeController.chamarHome());
        }
        return redirect(routes.HomeController.index());
    }

    public Result logOut(){

        usuarioLogado = null;

        return redirect(routes.HomeController.index());
    }

    //Validacao
    public boolean validarLogin(String email, String senha){

        for (int i = 0; i < listaDeUsuarios.size(); i++){
            Usuario usuario = listaDeUsuarios.get(i);
            if (usuario.getEmail().equals(email)){
                if (usuario.getSenha().equals(senha)){
                    usuarioLogado = usuario;
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    private Boolean verificaCredenciais(String nome, String email, String senha){
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2)
            if (nome.length() < 21) if (senha.length() > 7) if (userMail.validate(email)) return true;
        return false;
    }
    //Renders
    public Result index() {
        return ok(index.render());
    }

    public Result chamarLogin() {
        return ok(login.render(listaDeUsuarios));
    }

    public Result chamarCadastro() {
        return ok(cadastro.render(listaDeUsuarios));
    }

    public Result menuUsuario(){
        return ok(usuario.render(usuarioLogado));
    }

    public Result chamarHome() {
        return ok(home.render(usuarioLogado));
    }


    public Result chamaTexto(){return ok(texto.render(listaDeArquivos));}


    /*public  Result salvaArquivo(){

        Arquivo arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);

        return redirect(routes.HomeController.chamarHome());}
*/
    public Result criaPasta(){
        LOGGER.info("ENTROU NO CONTROLLER");
        Diretorio dir = formFactory.form(Diretorio.class).bindFromRequest().get();

        if (dir.getNome() == null || dir.getNome().isEmpty()){
            return ok(home.render(usuarioLogado));
        }else {
            usuarioLogado.criaSubDiretorio(dir.getNome());
            return ok(home.render(usuarioLogado));
        }
    }

    public Result criaArquivos(){
        ArquivoTxt arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getconteudoFile());
        return ok(home.render(usuarioLogado));
    }
    //GETs and SETs
    public List<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }
    public List<ArquivoTxt> getListaDeArquivos() {
        return listaDeArquivos;
    }

}
