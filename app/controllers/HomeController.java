package controllers;

import javax.inject.*;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.*;
import views.html.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

    @Inject
    private FormFactory formFactory;

    private List<Usuario> listaDeUsuarios = new ArrayList<>();
    private List<Arquivo> listaDeArquivos = new ArrayList<>();
    private Usuario usuarioLogado = null;


//    private static HomeController instance = new HomeController();

    public HomeController(){
        listaDeUsuarios = Usuario.find.findList();
    }

    /**
     * Cadastra um usuário no sistema
     * @return O redirecionamento para o login
     */
    public Result cadastrarUsuario(){

        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();

        if (verificaCredenciais(usuario.getNome(), usuario.getEmail(), usuario.getSenha())) {
            Util.saveUsuario(usuario);
            listaDeUsuarios.add(usuario);
            flash("sucesso", "Cadastrado com sucesso.");
        }
        return redirect(routes.HomeController.index());
    }

//    public Result escreverTexto(){
//        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
//        listaDeUsuarios.add(usuario);
//
//        try {
//            if (validarLogin(usuario.getEmail(), usuario.getSenha())){
//                return redirect(routes.HomeController.chamarHome());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return redirect(routes.HomeController.index());
//    }

     //   if (validarLogin(usuario.getEmail(), usuario.getSenha())){
     //       return redirect(routes.HomeController.chamarHome());
     //   }
   //     return redirect(routes.HomeController.index());
  //  }
    public Result logar(){
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();
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

    public Result logOut(){

        usuarioLogado = null;
        session().clear();
        return redirect(routes.HomeController.index());
    }

    //Validacao
    private boolean validarLogin(String email, String senha) throws Exception {

        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(email)) {
                if (usuario.getSenha().equals(senha)) {
                    usuarioLogado = usuario;
                    session("login", usuario.getEmail());
                    return true;
                } else {
                    throw new Exception("Login ou senha incorretos.");
                }
            }
        }
        throw new Exception("Usuario inexistente");
    }

    private Boolean verificaCredenciais(String nome, String email, String senha){
        return Util.validaCredenciais(nome, email, senha);
    }

    //Renders
    public Result index() {
        return ok(index.render());
    }

    public Result chamarHome() {
        return ok(home.render(usuarioLogado));
    }

    public Result chamarCaixa() {return ok(caixaNotificacoes.render(usuarioLogado)); }


    public Result chamaTexto(){return ok(texto.render(listaDeArquivos));}


    /*public  Result salvaArquivo(){

        Arquivo arquivo = formFactory.form(ArquivoTxt.class).bindFromRequest().get();
        listaDeArquivos.add(arquivo);

        return redirect(routes.HomeController.chamarHome());}
*/

    public Result criaPasta(){
        Diretorio dir = formFactory.form(Diretorio.class).bindFromRequest().get();
        if (dir.getNome() == null || dir.getNome().isEmpty()){
            return ok(index.render());
        }else {
            Util.saveDiretorio(dir);
            usuarioLogado.addDir(dir);
            for (Diretorio d: usuarioLogado.getPastaPessoal().getSubDiretorios()) {
                System.out.println(d.getNome());
            }
            return ok(home.render(usuarioLogado));
        }
    }

    public Result editaNomePasta() {
        String nomeNovo = request().getQueryString("nome").trim();
        String nomeAntigo = request().getQueryString("antigoNomePasta").trim();
        Diretorio dir = usuarioLogado.getDiretorio(nomeAntigo);
        dir.setNome(nomeNovo);
        Util.updateDiretorio(dir);
        Util.updateUsuario(usuarioLogado);
        return ok(home.render(usuarioLogado));
    }

    public Result editaNomeArquivo() {
        String nomeNovo = request().getQueryString("nome").trim();
        String nomeAntigo = request().getQueryString("antigoNome").trim();
        return ok(home.render(usuarioLogado));
    }

    public Result criaArquivos(){
        DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
        String nomeArquivo = (String) form.getData().get("nomeArquivo");
        String conteudoArquivo = (String) form.getData().get("conteudoFile");
        String extensao = (String) form.getData().get("extensao");

        Arquivo arquivo;
        if (extensao.equals(".txt")){
            arquivo = new ArquivoTxt(nomeArquivo, conteudoArquivo);
        }
        else {
            arquivo = new ArquivoMd(nomeArquivo, conteudoArquivo);
        }
        listaDeArquivos.add(arquivo);
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao);
        return ok(home.render(usuarioLogado));
    }


    public Result abreArquivo(String nomeArquivo){

        Arquivo arquivo = findFileFromList(nomeArquivo);
        String conteudo = arquivo.getConteudoArquivo();

        return ok(arquivoConteudo.render(nomeArquivo, conteudo));
    }

    public Result leituraArquivo(String nomeArquivo){

        Arquivo arquivo = findFileFromList(nomeArquivo);
        String conteudo = arquivo.getConteudoArquivo();

        return ok(leituraArquivo.render(nomeArquivo, conteudo));
    }


    public Result deletaArquivo(String nomeArquivo){
        deletFileFromList(nomeArquivo);
        usuarioLogado.excluirArquivo(nomeArquivo);
        return ok(home.render(usuarioLogado));
    }

    public Result chamaModificaArquivo(String nomeArquivo){
        Arquivo arquivo=findFileFromList(nomeArquivo);

        return ok(modificaArquivo.render(nomeArquivo, arquivo.getConteudoArquivo()));
    }

    //__________________________________________________________
    public Result editaArquivo(String nomeArquivoASerEditado){

        DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
        String nomeArquivo = (String) form.getData().get("nomeArquivo");
        String conteudoArquivo = (String) form.getData().get("conteudoFile");
        String extensao = (String) form.getData().get("extensao");

        Arquivo arquivo;
        if (extensao.equals(".txt")){
            arquivo = new ArquivoTxt(nomeArquivo, conteudoArquivo);
        }
        else{
            arquivo = new ArquivoMd(nomeArquivo, conteudoArquivo);
        }
        listaDeArquivos.add(arquivo);
        usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao);

        return ok(home.render(usuarioLogado));

    }
    //_____________________________________________________

    public void deletFileFromList(String nameOfFile){
        Arquivo arquivo=null;
        for (int i=0; i< listaDeArquivos.size();i++){
            if (nameOfFile.equals(listaDeArquivos.get(i).getNomeArquivo()))
                 arquivo = listaDeArquivos.get(i);
                listaDeArquivos.remove(i);
        }
        arquivo.deletaArquivoSistema(nameOfFile);
    }

    public Arquivo findFileFromList(String nomeArquivo){
        Arquivo arquivo = null ;
        for (int i=0; i< listaDeArquivos.size();i++){
            if (nomeArquivo.equals(listaDeArquivos.get(i).getNomeArquivo()))
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


    /*
     * Compartilhamento
     */

    public Result compartilha(){

        DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
        String nomeArquivo = (String) form.getData().get("nomeArquivo");
        String emailUsuario = (String) form.getData().get("emailUsuario");
        String tipo = (String) form.getData().get("tipo");

        if (tipo.equals("edicao"))
            compartilhaEdicao(emailUsuario, nomeArquivo);
        else
            compartilhaLeitura(emailUsuario, nomeArquivo);

        return ok(home.render(usuarioLogado));
    }

    public void compartilhaEdicao(String emailUsuario, String nomeArquivo){
        for (Usuario usuario: listaDeUsuarios) {
            if (usuario.getEmail().equals(emailUsuario)){
                Arquivo procurado = findFileFromList(nomeArquivo);
                procurado.getCompartilhadosEdicao().add(emailUsuario);
                usuario.getCompartilhados().getArquivos().add(procurado);
                Notificacao edicao = new NotificacaoDeEdicao(usuarioLogado.getNome());
                usuario.getCaixaDeNotificacao().getCaixaDeNotificacao().add(edicao);
            }
        }
    }

    public void compartilhaLeitura(String emailUsuario, String nomeArquivo){
        for (Usuario usuario: listaDeUsuarios) {
            if(usuario.getEmail().equals(emailUsuario)) {
                Arquivo procurado = findFileFromList(nomeArquivo);
                procurado.getCompartilhadosLeitura().add(emailUsuario);
                usuario.getCompartilhados().getArquivos().add(procurado);
                Notificacao leitura = new NotificacaoDeLeitura(usuarioLogado.getNome());
                usuario.getCaixaDeNotificacao().getCaixaDeNotificacao().add(leitura);
            }
        }
    }
//
//    public static HomeController getInstance() {
//        return instance;
//    }
}
