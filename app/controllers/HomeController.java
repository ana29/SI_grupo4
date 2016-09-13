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

    /**
     * Cadastra um usuário no sistema
     * @return O redirecionamento para o login
     */
    public Result cadastrarUsuario(){
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();

        if (verificaCredenciais(usuario.getNome(), usuario.getEmail(), usuario.getSenha())) {
            listaDeUsuarios.add(usuario);
            flash("sucesso", "Cadastrado com sucesso.");
        }
        return redirect(routes.HomeController.index());
    }

    /**
     * Loga um usuário no sistema.
      * @return O redirecionamento para a tela Home, caso as credenciais estejam válidas, ou recarrega a tela de login.
     */
    public Result logar(){

        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
        listaDeUsuarios.add(usuario);

        try {
            if (validarLogin(usuario.getEmail(), usuario.getSenha())){
                return redirect(routes.HomeController.chamarHome());
            }
        } catch (Exception e) {
            flash("erro", e.getMessage());
            return redirect(routes.HomeController.index());
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

    /**
     * Faz logout do sistema.
     * @return O redirecionamento para a tela de login.
     */
    public Result logOut(){
        usuarioLogado = null;
        session().clear();

        return redirect(routes.HomeController.index());
    }

    /**
     * Realiza a validação das credenciais para login.
     * @param email Email do usuário cadastrado.
     * @param senha Senha do usuário cadastrado.
     * @return Boolean com o resultado da validação.
     */
    private boolean validarLogin(String email, String senha) throws Exception {

        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(email)) {
                if (usuario.getSenha().equals(senha)) {
                    usuarioLogado = usuario;
                    session("login", usuario.getEmail());
                    return true;
                }else{
                    throw new Exception("Login ou senha incorretos.");
                }
            }
        }
        throw new Exception("Usuario inesistente");
    }

    /**
     * Realiza a validação das credenciais para cadastro.
     * @param nome Nome do usuário.
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     * @return Boolean com o resultado da validação.
     */
    private Boolean verificaCredenciais(String nome, String email, String senha){
        return util.validaCredenciais(nome, email, senha);
    }

    /**
     * Renderiza a página de login e cadastro.
     * @return A página de login e cadastro.
     */
    public Result index() {
        return ok(index.render());
    }

    /**
     * Renderiza a página home do usuário.
     * @return A página home do usuário.
     */
    public Result chamarHome() {
        return ok(home.render(usuarioLogado));
    }

    /**
     * Rendereriza a página de notificações do usuário.
     * @return A página de notificações do usuário.
     */
    public Result chamarCaixa() {return ok(caixaNotificacoes.render(usuarioLogado)); }

    /**
     * Renderiza a página de criação de documento.
     * @return A página de criação de documento.
     */
    public Result chamaTexto(){return ok(texto.render(listaDeArquivos));}

    public Result chamaTextoMd(){return ok(textoMd.render(listaDeArquivos));}

    /*public  Result salvaArquivo(){

        Arquivo arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);

        return redirect(routes.HomeController.chamarHome());}
*/
    /**
     * Renderiza a página home do usuário após a criação de uma pasta.
     * @return A página home do usuário após a criação de uma pasta.
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

    public Result escreverTexto(){
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
        listaDeUsuarios.add(usuario);

        try {
            if (validarLogin(usuario.getEmail(), usuario.getSenha())){
                return redirect(routes.HomeController.chamarHome());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return redirect(routes.HomeController.index());
        }

        return null;
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
        deletFileFromList(nomeArquivo);

        usuarioLogado.excluirArquivo(nomeArquivo);

        return ok(home.render(usuarioLogado));
    }

    public Result chamaModificaArquivo(String nomeArquivo){
        Arquivo arquivo=findFileFromList(nomeArquivo);

        return ok(modificaArquivo.render(nomeArquivo, arquivo.getConteudoArquivo()));
    }
    public Result editaArquivo(String nomeArquivo){

        ArquivoTxt arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);

        deletFileFromList(nomeArquivo);
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), ".txt");

        return ok(home.render(usuarioLogado));

    }
    //_____________________________________________________

    public void deletFileFromList(String nameOfFile){
        Arquivo arquivo=null;
        for (int i=0; i< listaDeArquivos.size();i++){
            if (nameOfFile.equals(listaDeArquivos.get(i).getNomeComExtensao()))
                 arquivo = listaDeArquivos.get(i);
                listaDeArquivos.remove(i);
        }
        arquivo.deletaArquivoSistema(nameOfFile);
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
