package models;
import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;

@Entity
@Table(name = "Usuarios")
public class Usuario extends Model{
//
    public static Model.Finder<Long, Usuario> find = new Model.Finder<>(Usuario.class);

    @Id @GeneratedValue
    private Long id;

    @Column
    private String nome;

    @Column
    @Constraints.Email
    private String email;

    @Column
    @Constraints.Required
    private String senha;

//    @OneToOne
    private Diretorio pastaPessoal;

//    @OneToOne
    private Diretorio compartilhados;

//    @OneToOne
    private CaixaDeNotificacao caixaDeNotificacao;

    public Usuario(){
        this.pastaPessoal = new Diretorio("root");
        this.compartilhados = new Diretorio("Compartilhados");
        this.caixaDeNotificacao = new CaixaDeNotificacao();
    }
//
//    public Usuario(String nome, String email, String senha){
//        this.caixaDeNotificacao = new CaixaDeNotificacao();
//        this.pastaPessoal = new Diretorio("root");
////        this.compartilhados = new Diretorio("Compartilhados");
//        setNome(nome);
//        setEmail(email);
//        setSenha(senha);
//    }

    public void addDir(Diretorio diretorio){
        if (!pastaPessoal.containsDiretorio(diretorio.getNome())){
            pastaPessoal.getSubDiretorios().add(diretorio);
            update();
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = diretorio.getNome() + "(" + count + ")";
                diretorio.setNome(novoNome);
                if (!pastaPessoal.containsDiretorio(novoNome)){
                    pastaPessoal.getSubDiretorios().add(diretorio);
                    update();
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    public  void addArquivo(String nomeArquivo, String conteudoFile, String extensao){
        if (!pastaPessoal.containsArquivo(nomeArquivo, extensao)){
            auxExtensao(nomeArquivo, conteudoFile, extensao);
        }
        else{
            boolean adicionado = false;
            int count = 1;
            while (!adicionado){
                String novoNome = nomeArquivo+ "(" + count + ")";
                if (!pastaPessoal.containsArquivo(novoNome, extensao)){
                    auxExtensao(novoNome, conteudoFile, extensao);
                    adicionado = true;
                }
                count ++;
            }
        }
    }

    private void auxExtensao(String nomeArquivo, String conteudoFile, String extensao) {
        if (extensao.equals(".txt")){
            pastaPessoal.getArquivos().add(new ArquivoTxt(nomeArquivo, conteudoFile));
        }
        else{
            pastaPessoal.getArquivos().add(new ArquivoMd(nomeArquivo, conteudoFile));

        }
    }

    public void excluirSubDiretorio(String nome){
        for (Diretorio d: pastaPessoal.getSubDiretorios()) {
            if (d.getNome().equals(nome)){
                pastaPessoal.getSubDiretorios().remove(d);
            }
        }
    }

    public Diretorio getDiretorio(String nome) {
        for (Diretorio dir : pastaPessoal.getSubDiretorios()) {
            if (dir.getNome().equals(nome)) {
                return dir;
            }
        }
        return null;
    }

    public void excluirArquivo(String nome){
        if (pastaPessoal.containsArquivo(nome, ""))
            pastaPessoal.getArquivos().remove(nome);
    }

//    public Diretorio getArquivo(String nome){
//        for (ArquivoTxt arquivo: pastaPessoal.subDiretorios) {
//            if (arquivo.getNome().equals(nome)){
//                return dir;
//            }
//        }
//        return null;
//    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public Diretorio getPastaPessoal() {
        return pastaPessoal;
    }

    public Diretorio getCompartilhados(){ return compartilhados;}

    public CaixaDeNotificacao getCaixaDeNotificacao() {
        return caixaDeNotificacao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setPastaPessoal(Diretorio pastaPessoal) {
        this.pastaPessoal = pastaPessoal;
    }

    public void setCompartilhados(Diretorio compartilhados) {
        this.compartilhados = compartilhados;
    }

    public void setCaixaDeNotificacao(CaixaDeNotificacao caixaDeNotificacao) {
        this.caixaDeNotificacao = caixaDeNotificacao;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Finder<Long, Usuario> getFind() {
        return find;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id + "\n" +
                ", email='" + email + '\'' + "\n" +
                ", nome='" + nome + '\'' + "\n" +
                ", senha='" + senha + '\'' + "\n" +
                ", pastaPessoal=" + this.pastaPessoal + " nome: " + this.pastaPessoal.getNome() + "\n" +
                ", compartilhados=" + compartilhados + "\n" +
                ", caixaDeNotificacao=" + caixaDeNotificacao +
                '}';
    }
}

