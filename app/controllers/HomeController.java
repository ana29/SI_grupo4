package controllers;

import javax.inject.*;
import Util.EmailValidator;
import Util.Util;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.*;
import views.html.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

        try{
            if (verificaCredenciais(usuario.getNome(), usuario.getEmail(), usuario.getSenha())) {
                Util.saveUsuario(usuario);
                listaDeUsuarios.add(usuario);
                flash("sucesso", "Usuario cadastrado com sucesso.");
            }
        }catch (Exception e){
            flash("erro", "O usuario não foi cadastrado: " + e.getMessage());
        }

        return redirect(routes.HomeController.index());
    }

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

    private boolean validarLogin(String email, String senha) throws Exception {

        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(email)) {
                if (usuario.getSenha().equals(senha)) {
                    Timestamp horaDoLogin = new Timestamp(System.currentTimeMillis());
                    usuario.setHoraDoLogin(horaDoLogin);
                    usuarioLogado = usuario;

                    session("login", usuario.getEmail());
                    session("token", geraToken());

                    return true;
                } else {
                    throw new Exception("Login ou senha incorretos.");
                }
            }
        }
        throw new Exception("Usuario inexistente");
    }

    private Boolean verificaCredenciais(String nome, String email, String senha) throws Exception{
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2 && nome.length() < 21){
            if (senha.length() > 7){
                if (userMail.validate(email)){
                    return true;
                }else{
                    throw new Exception("Email inválido.");
                }
            }else{
                throw new Exception("Senha inválida.");
            }
        }else {
            throw new Exception("Nome inválido");
        }
    }

    //Renders
    public Result index() {
        return ok(index.render());
    }

    public Result chamarHome() {
        return ok(home.render(usuarioLogado, usuarioLogado.getPastaPessoal()));
    }

    public Result chamarCaixa() {return ok(caixaNotificacoes.render(usuarioLogado)); }

    public Result chamarLixeira() {return ok(lixeira.render(usuarioLogado, usuarioLogado.getLixeira()));}

    public Result chamaTexto(String caminhoDiretorio){
        return ok(texto.render(listaDeArquivos, usuarioLogado.buscaDiretorio(caminhoDiretorio)));
    }

    public Result chamaModificaArquivo(String nomeArquivo, String caminhoDiretorio){
        if (isAutenticate()) {
            Arquivo arquivo = findFileFromList(nomeArquivo);
            return ok(modificaArquivo.render(nomeArquivo, arquivo.getConteudoArquivo(), usuarioLogado.buscaDiretorio(caminhoDiretorio)));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    //Acoes

    public Result criaPasta(String caminhoDiretorioAtual){
        if (isAutenticate()) {
            Diretorio diretorioAtual = usuarioLogado.buscaDiretorio(caminhoDiretorioAtual);
            Diretorio dir = formFactory.form(Diretorio.class).bindFromRequest().get();

            if (dir.getNome() == null || dir.getNome().isEmpty()) {
                return ok(home.render(usuarioLogado, diretorioAtual));
            } else {

                //usuarioLogado.addDir(dir);
                usuarioLogado.criaSubDiretorio(dir.getNome(), caminhoDiretorioAtual);
                Util.saveDiretorio(dir);
                Util.updateUsuario(usuarioLogado);
                return ok(home.render(usuarioLogado, diretorioAtual));

            }
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result criaArquivos(String caminhoDiretorio){
        if (isAutenticate()) {
            DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
            String nomeArquivo = (String) form.getData().get("nomeArquivo");
            String conteudoArquivo = (String) form.getData().get("conteudoFile");
            String extensao = (String) form.getData().get("extensao");

            Arquivo arquivo;
            if (extensao.equals(".txt")) {
                arquivo = new ArquivoTxt(nomeArquivo, conteudoArquivo);
            } else {
                arquivo = new ArquivoMd(nomeArquivo, conteudoArquivo);
            }
            listaDeArquivos.add(arquivo);
            Diretorio dir = usuarioLogado.buscaDiretorio(caminhoDiretorio);
            usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao, caminhoDiretorio);
            return ok(home.render(usuarioLogado, dir));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result abrePasta(String diretorio){
        if (isAutenticate()){
            return ok(home.render(usuarioLogado, usuarioLogado.buscaDiretorio(diretorio)));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result abreArquivo(String nomeArquivo, String caminhoDiretorio){
        if (isAutenticate()) {
            Arquivo arquivo = findFileFromList(nomeArquivo);
            String conteudo = arquivo.getConteudoArquivo();
            if (caminhoDiretorio.equals(usuarioLogado.getCaminhoLixeira())){
                return ok(lixeiraArquivoConteudo.render(nomeArquivo,conteudo,usuarioLogado.getLixeira()));
            }else{
                return ok(arquivoConteudo.render(nomeArquivo, conteudo, usuarioLogado.buscaDiretorio(caminhoDiretorio)));
            }
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result leituraArquivo(String nomeArquivo){
        if (isAutenticate()) {
            Arquivo arquivo = findFileFromList(nomeArquivo);
            String conteudo = arquivo.getConteudoArquivo();

            return ok(leituraArquivo.render(nomeArquivo, conteudo));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result editaNomePasta() {
        String nomeNovo = request().getQueryString("nome").trim();
        String nomeAntigo = request().getQueryString("antigoNomePasta").trim();
        Diretorio dir = usuarioLogado.getDiretorio(nomeAntigo);
        System.out.println(nomeNovo);
        System.out.println(nomeAntigo);
        dir.setNome(nomeNovo);
        System.out.print(dir.getNome());
        return ok(home.render(usuarioLogado, dir));
    }


    public Result editaArquivo(String nomeArquivoASerEditado, String caminhoDiretorio){
        if (isAutenticate()) {
            DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
            String nomeArquivo = (String) form.getData().get("nomeArquivo");
            String conteudoArquivo = (String) form.getData().get("conteudoFile");
            String extensao = (String) form.getData().get("extensao");

            Arquivo arquivo;
            if (extensao.equals(".txt")) {
                arquivo = new ArquivoTxt(nomeArquivo, conteudoArquivo);
            } else {
                arquivo = new ArquivoMd(nomeArquivo, conteudoArquivo);
            }
            listaDeArquivos.add(arquivo);
            usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao, caminhoDiretorio);

            return ok(home.render(usuarioLogado, usuarioLogado.buscaDiretorio(caminhoDiretorio)));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    private Arquivo findFileFromList(String nomeArquivo){
        Arquivo arquivo = null ;
        for (int i=0; i< listaDeArquivos.size();i++){
            if (nomeArquivo.equals(listaDeArquivos.get(i).getNomeArquivo()))
                arquivo =  listaDeArquivos.get(i);
        }
        return arquivo;
    }

    public Result moveArquivoParaLixeira(String nomeArquivo, String caminhoDiretorio){

        if (isAutenticate()) {
            Arquivo arquivo = findFileFromList(nomeArquivo);
            String nome = arquivo.getNomeArquivo();
            String conteudo = arquivo.getConteudoArquivo();
            String extensao = arquivo.getExtensao();


            usuarioLogado.excluirArquivo(nome,extensao,caminhoDiretorio);

            listaDeArquivos.remove(arquivo);
           // arquivo.deletaArquivoSistema((File) arquivo);

            if (extensao.equals(".txt")) {
                arquivo = new ArquivoTxt(nomeArquivo, conteudo);
            } else {
                arquivo = new ArquivoMd(nomeArquivo, conteudo);
            }
            listaDeArquivos.add(arquivo);

            usuarioLogado.addArquivo(nome, conteudo, extensao, usuarioLogado.getCaminhoLixeira());

            return ok(lixeira.render(usuarioLogado,usuarioLogado.getLixeira()));



        } else {
        flash("tokenExpirado", "Você não está autenticado! Realize o login.");
        return redirect(routes.HomeController.index());
        }
    }

    public Result moveDiretorioParaLixeira(String caminhoDiretorio){
        if(isAutenticate()){

            Diretorio dir = usuarioLogado.buscaDiretorio(caminhoDiretorio);

            String nomeDiretorio = dir.getNome();



            usuarioLogado.excluirSubDiretorio(nomeDiretorio, usuarioLogado.getPastaPessoal());
            usuarioLogado.getPastaPessoal().getSubDiretorios().remove(dir);

            usuarioLogado.criaSubDiretorio(nomeDiretorio, usuarioLogado.getCaminhoLixeira());

            return ok(lixeira.render(usuarioLogado, usuarioLogado.getLixeira()));
        }else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
    }

    public Result limpaLixeira(){

        if (isAutenticate()) {
            Diretorio dir = usuarioLogado.getLixeira();

            while (usuarioLogado.lixeira.getArquivos().size()>0 || usuarioLogado.lixeira.getSubDiretorios().size()>0){
                for (int i = 0; i <dir.getArquivos().size() ; i++){
                    Arquivo arq = dir.getArquivos().get(i);
                    usuarioLogado.excluirArquivo(arq.getNomeArquivo(),arq.getExtensao(), usuarioLogado.getCaminhoLixeira());
                    listaDeArquivos.remove(arq);
                    arq.deletaArquivoSistema(arq.getNomeArquivo());

                }

                for (int i = 0; i < dir.getSubDiretorios().size() ; i++) {
                    usuarioLogado.lixeira.getSubDiretorios().remove(dir.subDiretorios.get(i));
                    usuarioLogado.excluirSubDiretorio(dir.getNome(), usuarioLogado.getLixeira());
                }
            }
            return ok(lixeira.render(usuarioLogado,usuarioLogado.getLixeira()));



        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }


    }

    /*
     * Compartilhamento
     */

    public Result compartilha(){
        if (isAutenticate()) {
            DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
            String nomeArquivo = (String) form.getData().get("nomeArquivo");
            String emailUsuario = (String) form.getData().get("emailUsuario");
            String tipo = (String) form.getData().get("tipo");

            if (tipo.equals("edicao"))
                compartilhaEdicao(emailUsuario, nomeArquivo);
            else
                compartilhaLeitura(emailUsuario, nomeArquivo);

            return ok(home.render(usuarioLogado, usuarioLogado.getPastaPessoal()));
        } else {
            flash("tokenExpirado", "Você não está autenticado! Realize o login.");
            return redirect(routes.HomeController.index());
        }
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

    private String geraToken(){
        Random rand = new Random();
        String token = Long.toHexString(rand.nextLong()) + Long.toHexString(rand.nextLong());
        System.out.println(token);
        return token;
    }

    private boolean isAutenticate() {

        if (session("token") != null && verificaTimeStampDoToken()) {
            return true;
        } else {
            return false;
        }
    }
    /*
     * Compressão
     */

    public Result comprime(){
        DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
        String nomeObjeto = (String) form.getData().get("nomeObjeto");
        String tipo = (String) form.getData().get("tipo");
        Arquivo arquivoC;
        for (Arquivo arquivo : listaDeArquivos) {
            if (arquivo.getNomeArquivo().equals(nomeObjeto)) {
                if (tipo.equals("zip")) {
                    arquivoC = new ArquivoZip(arquivo.getFile());
                } else {
                    arquivoC = new ArquivoGzip(arquivo.getFile());
                }
                listaDeArquivos.add(arquivoC);
                usuarioLogado.addComprimidos(arquivoC);
                removeOriginal(arquivo);
                return ok(home.render(usuarioLogado, usuarioLogado.getPastaPessoal()));
            }
        }
        return ok(home.render(usuarioLogado, usuarioLogado.getPastaPessoal()));
    }

    private void removeOriginal(Arquivo arquivo){
        System.out.println("CHAMOU PRA REMOVER");
        usuarioLogado.excluirArquivo(arquivo.getNomeArquivo(), arquivo.getExtensao(), pegaCaminho(usuarioLogado.getPastaPessoal(), arquivo));
        listaDeArquivos.remove(arquivo);
        arquivo.deletaArquivoSistema(arquivo.getNomeArquivo());
    }

    private String pegaCaminho(Diretorio diretorio, Arquivo arquivo){
        if(!diretorio.containsArquivo(arquivo.getNomeArquivo(), arquivo.getExtensao())) {
            System.out.println("O ROOT NAO TEM");
            if (!diretorio.getSubDiretorios().isEmpty()) {
                System.out.println("TEM SUBDIRETORIO");
                for (Diretorio dir : diretorio.getSubDiretorios()) {
                    if (dir.containsArquivo(arquivo.getNomeArquivo(), arquivo.getExtensao())) {
                        System.out.println("ESPERO QUE ACHE");
                        return dir.getRaiz();
                    }
                }
            }
        }
    return diretorio.getRaiz();
    }

    //GETs and SETs------------------------------------------------------
    public List<Usuario> getListaDeUsuarios () {
        return listaDeUsuarios;
    }
    public List<Arquivo> getListaDeArquivos () {
        return listaDeArquivos;
    }
//
//    public static HomeController getInstance() {
//        return instance;
//    }
}
