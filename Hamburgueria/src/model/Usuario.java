package model;

/**
 * Classe abstrata que representa um usuário do sistema (Administrador ou Colaborador).
 */
public abstract class Usuario {

    private static int proximoId = 1;

    private int id;
    private String nome;
    private String login;
    private String senha;

    public Usuario(String nome, String login, String senha) {
        this.id = proximoId++;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public Usuario(int id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        if (id >= proximoId) proximoId = id + 1;
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){ 
        this.id = id;
    }
    public String getNome(){ 
        return nome; 
    }
    public void setNome(String nome){ 
        this.nome = nome; 
    }
    public String getLogin(){ 
        return login; 
    }
    public void setLogin(String login){
        this.login = login; 
    }
    public String getSenha(){ 
        return senha; 
    }
    public void setSenha(String senha){ 
        this.senha = senha; 
    }
    public boolean autenticar(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', login='" + login + "'}";
    }
}
