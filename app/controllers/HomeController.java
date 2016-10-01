package controllers;

import Util.EmailValidator;
import Util.Validador;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.sql.Timestamp;
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
    private List<Arquivo> listaDeArquivos = new ArrayList<>();
    private Validador validador;
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    /**
     * Cadastra um usuário no sistema
     *
     * @return O redirecionamento para o login
     */
    public Result cadastrarUsuario() {
        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();

        try {
            if (verificaCredenciais(usuario.getNome(), usuario.getEmail(), usuario.getSenha())) {
                listaDeUsuarios.add(usuario);
                flash("sucesso", "Usuario cadastrado com sucesso.");
            }
        } catch (Exception e) {
            flash("erro", "O usuario não foi cadastrado: " + e.getMessage());
        }
        return redirect(routes.HomeController.index());
    }


    public Result logar() {

        Usuario usuario = formFactory.form(Usuario.class).bindFromRequest().get();

        try {
            if (validarLogin(usuario.getEmail(), usuario.getSenha())) {
                return redirect(routes.HomeController.chamarHome());
            }
        } catch (Exception e) {
            flash("erro", e.getMessage());
            return redirect(routes.HomeController.index());
        }
        return redirect(routes.HomeController.index());
    }

    public Result logOut() {

        session().clear();

        return redirect(routes.HomeController.index());
    }


    private boolean validarLogin(String email, String senha) throws Exception {

        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(email)) {
                if (usuario.getSenha().equals(senha)) {
                    Timestamp horaDoLogin = new Timestamp(System.currentTimeMillis());
                    usuario.setHoraDoLogin(horaDoLogin);
                    //usuarioLogado = usuario;

                    session("login", usuario.getEmail());
                    validador = new Validador(usuarioLogado(session("login")));
                    session("token", validador.geraToken());

                    return true;
                } else {
                    throw new Exception("Login ou senha incorretos.");
                }
            }
        }
        throw new Exception("Usuario inexistente");
    }

    private Boolean verificaCredenciais(String nome, String email, String senha) throws Exception {
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2 && nome.length() < 21) {
            if (senha.length() > 7) {
                if (userMail.validate(email)) {
                    return true;
                } else {
                    throw new Exception("Email inválido.");
                }
            } else {
                throw new Exception("Senha inválida.");
            }
        } else {
            throw new Exception("Nome inválido");
        }
    }

    //Renders
    public Result index() {
        return ok(index.render());
    }

    public Result chamarHome() {
        Usuario usuario = usuarioLogado(session("login"));
        return ok(home.render(usuario, usuario.getPastaPessoal()));
    }

    public Result chamarCaixa() {
        return ok(caixaNotificacoes.render(usuarioLogado(session("login"))));
    }


    public Result chamaTexto(String caminhoDiretorio) {
        return ok(texto.render(listaDeArquivos, usuarioLogado(session("login")).buscaDiretorio(caminhoDiretorio)));
    }



    public Result criaPasta(String caminhoDiretorioAtual) {
        try {
            validador.autenticaUsuario();
            Usuario usuarioLogado = usuarioLogado(session("login"));
            Diretorio diretorioAtual = usuarioLogado.buscaDiretorio(caminhoDiretorioAtual);
            Diretorio dir = formFactory.form(Diretorio.class).bindFromRequest().get();

            if (dir.getNome() == null || dir.getNome().isEmpty()) {
                return ok(home.render(usuarioLogado, diretorioAtual));
            } else {
                usuarioLogado.criaSubDiretorio(dir.getNome(), caminhoDiretorioAtual);
                return ok(home.render(usuarioLogado, diretorioAtual));
            }
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public Result criaArquivos(String caminhoDiretorio) {
        try {
            validador.autenticaUsuario();
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

            Usuario usuarioLogado = usuarioLogado(session("login"));
            Diretorio dir = usuarioLogado.buscaDiretorio(caminhoDiretorio);
            usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao, caminhoDiretorio);

            return ok(home.render(usuarioLogado, dir));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public Result abrePasta(String diretorio) {
        try {
            validador.autenticaUsuario();
            Usuario usuarioLogado = usuarioLogado(session("login"));
            return ok(home.render(usuarioLogado, usuarioLogado.buscaDiretorio(diretorio)));

        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public Result abreArquivo(String nomeArquivo, String caminhoDiretorio) {
        try {
            validador.autenticaUsuario();
            Arquivo arquivo = findFileFromList(nomeArquivo);
            String conteudo = arquivo.getConteudoArquivo();
            return ok(arquivoConteudo.render(nomeArquivo, conteudo, usuarioLogado(session("login")).buscaDiretorio(caminhoDiretorio)));

        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public Result leituraArquivo(String nomeArquivo) {
        try {
            validador.autenticaUsuario();
            Arquivo arquivo = findFileFromList(nomeArquivo);
            String conteudo = arquivo.getConteudoArquivo();

            return ok(leituraArquivo.render(nomeArquivo, conteudo));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }


    public Result deletaArquivo(String nomeArquivo, String caminhoDiretorio) {
        try {
            Usuario usuarioLogado = usuarioLogado(session("login"));
            deletFileFromList(nomeArquivo);
            usuarioLogado.excluirArquivo(nomeArquivo, caminhoDiretorio);

            return ok(home.render(usuarioLogado, usuarioLogado.buscaDiretorio(caminhoDiretorio)));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public Result chamaModificaArquivo(String nomeArquivo, String caminhoDiretorio) {
        try {
            validador.autenticaUsuario();
            Arquivo arquivo = findFileFromList(nomeArquivo);
            return ok(modificaArquivo.render(nomeArquivo, arquivo.getConteudoArquivo(), usuarioLogado(session("login")).buscaDiretorio(caminhoDiretorio)));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    //__________________________________________________________
    public Result editaArquivo(String nomeArquivoASerEditado, String caminhoDiretorio) {
        try {
            validador.autenticaUsuario();
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

            Usuario usuarioLogado = usuarioLogado(session("login"));
            usuarioLogado.addArquivo(arquivo.getNomeArquivo(), arquivo.getConteudoArquivo(), extensao, caminhoDiretorio);

            return ok(home.render(usuarioLogado, usuarioLogado.buscaDiretorio(caminhoDiretorio)));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }

    }
    //_____________________________________________________

    public void deletFileFromList(String nameOfFile) {
        Arquivo arquivo = null;
        for (int i = 0; i < listaDeArquivos.size(); i++) {
            if (nameOfFile.equals(listaDeArquivos.get(i).getNomeArquivo()))
                arquivo = listaDeArquivos.get(i);
            listaDeArquivos.remove(i);
        }
        arquivo.deletaArquivoSistema(nameOfFile);
    }

    public Arquivo findFileFromList(String nomeArquivo) {
        Arquivo arquivo = null;
        for (int i = 0; i < listaDeArquivos.size(); i++) {
            if (nomeArquivo.equals(listaDeArquivos.get(i).getNomeArquivo()))
                arquivo = listaDeArquivos.get(i);
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

    private Usuario usuarioLogado(String email) {
        for (Usuario usuario : this.getListaDeUsuarios()) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }


    /*
     * Compartilhamento
     */

    public Result compartilha() {
        try {
            validador.autenticaUsuario();
            DynamicForm.Dynamic form = formFactory.form().bindFromRequest().get();
            String nomeArquivo = (String) form.getData().get("nomeArquivo");
            String emailUsuario = (String) form.getData().get("emailUsuario");
            String tipo = (String) form.getData().get("tipo");

            if (tipo.equals("edicao"))
                compartilhaEdicao(emailUsuario, nomeArquivo);
            else
                compartilhaLeitura(emailUsuario, nomeArquivo);

            Usuario usuarioLogado = usuarioLogado(session("login"));
            return ok(home.render(usuarioLogado, usuarioLogado.getPastaPessoal()));
        } catch (Exception e) {
            flash("tokenExpirado", e.getMessage());
            return redirect(routes.HomeController.index());
        }
    }

    public void compartilhaEdicao(String emailUsuario, String nomeArquivo) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(emailUsuario)) {
                Arquivo procurado = findFileFromList(nomeArquivo);
                procurado.getCompartilhadosEdicao().add(emailUsuario);
                usuario.getCompartilhados().getArquivos().add(procurado);
                Notificacao edicao = new NotificacaoDeEdicao(usuarioLogado(session("login")).getNome());
                usuario.getCaixaDeNotificacao().getCaixaDeNotificacao().add(edicao);
            }
        }
    }

    public void compartilhaLeitura(String emailUsuario, String nomeArquivo) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(emailUsuario)) {
                Arquivo procurado = findFileFromList(nomeArquivo);
                procurado.getCompartilhadosLeitura().add(emailUsuario);
                usuario.getCompartilhados().getArquivos().add(procurado);
                Notificacao leitura = new NotificacaoDeLeitura(usuarioLogado(session("login")).getNome());
                usuario.getCaixaDeNotificacao().getCaixaDeNotificacao().add(leitura);
            }
        }
    }


}
