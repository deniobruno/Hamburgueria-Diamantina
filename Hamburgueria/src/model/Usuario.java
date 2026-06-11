package model;

/**
 * Classe abstrata que representa um usuário do sistema (Administrador ou Colaborador).
 * <p>
 * Serve como superclasse para {@link Administrador} e {@link Colaborador},
 * centralizando os atributos e comportamentos comuns.
 * Mantém um contador estático interno ({@code proximoId}) para geração
 * automática de identificadores únicos a cada nova instância criada.
 * </p>
 *
 */
public abstract class Usuario {

    private static int proximoId = 1;

    private int id;
    private String nome;
    private String login;
    private String senha;
    
     /**
     * Cria um novo usuário com ID gerado automaticamente pelo sistema.
     *
     * @param nome nome completo do usuário
     * @param login login utilizado na autenticação
     * @param senha senha utilizada na autenticação
     */

    public Usuario(String nome, String login, String senha) {
        this.id = proximoId++;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }
     /**
     * Reconstrói um usuário existente a partir de dados persistidos.
     * Atualiza o contador estático {@code proximoId} se o ID informado for maior ou igual ao atual,
     * garantindo que futuros IDs não colidam com os já existentes.
     *
     * @param id identificador único já existente do usuário
     * @param nome nome completo do usuário
     * @param login login utilizado na autenticação
     * @param senha senha utilizada na autenticação
     */
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
     /**
     * Verifica se as credenciais informadas são deste usuário.
     *
     * @param login login a ser verificado
     * @param senha senha a ser verificada
     * @return {@code true} se login e senha estiverem certo; {@code false} caso não.
     */
    public boolean autenticar(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', login='" + login + "'}";
    }
}
