package controllers;

import models.*;
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
    private Util util = new Util();
    private List<Usuario> listaDeUsuarios = new ArrayList<>();
    private List<Arquivo> listaDeArquivos = new ArrayList<>();
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
        return util.validaCredenciais(nome, email, senha);
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

    public Result chamarCaixa() {return ok(caixaNotificacoes.render(usuarioLogado)); }

    public Result chamaTexto(){return ok(texto.render(listaDeArquivos));}

    public Result chamaTextoMd(){return ok(textoMd.render(listaDeArquivos));}

    //_____________________________________________________
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
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), ".txt");
        return ok(home.render(usuarioLogado));
    }

    public  Result criaArquivosMd(){
        ArquivoMd arquivoMd = formFactory.form(ArquivoMd.class).bindFromRequest().get();
        listaDeArquivos.add(arquivoMd);
        usuarioLogado.addArquivo(arquivoMd.getNomeArquivo(), arquivoMd.getConteudoArquivo(), ".md");
        return ok(home.render(usuarioLogado));

    }

    public Result abreArquivo(String nomeArquivo){

        Arquivo arquivo = findFileFromList(nomeArquivo);
        String conteudo = arquivo.getConteudoArquivo();

        return ok(arquivoConteudo.render(nomeArquivo, conteudo));
    }

    //__________________________________________________________
    public Result deletaArquivo(String nomeArquivo){

        deletFile(nomeArquivo);

        return ok(home.render(usuarioLogado));
    }

    public Result chamaModificaArquivo(String nomeArquivo){
        Arquivo arquivo=findFileFromList(nomeArquivo);

        return ok(modificaArquivo.render(nomeArquivo, arquivo.getConteudoArquivo()));
    }
    public Result editaArquivo(String nomeArquivo){

        ArquivoTxt arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);

        deletFile(nomeArquivo);
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), ".txt");

        return ok(home.render(usuarioLogado));

    }
    //_____________________________________________________
    public void deletFile(String nameOfFile){
        for (int i=0; i< listaDeArquivos.size();i++){
            if (listaDeArquivos.get(i).getNomeArquivo().equals(nameOfFile))
                listaDeArquivos.remove(i);
        }

    }

    public Arquivo findFileFromList(String nomeArquivo){
        Arquivo arquivo = null ;
        for (int i=0; i< listaDeArquivos.size();i++){
            if (nomeArquivo.equals(listaDeArquivos.get(i).getNomeComExtensao()))
                arquivo =  listaDeArquivos.get(i);
        }
        return arquivo;
    }
    //_____________________________________________________
    //GETs and SETs
    public List<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    public List<Arquivo> getListaDeArquivos() {
        return listaDeArquivos;
    }




}
