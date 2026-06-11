package model;

/**
 * Representa o administrador do sistema.
 * Herda de {@link Usuario} e possui acesso total ao sistema.
 */
public class Administrador extends Usuario {

    private String email;

    public Administrador(String nome, String login, String senha, String email) {
        super(nome, login, senha);
        this.email = email;
    }

    public Administrador(int id, String nome, String login, String senha, String email) {
        super(id, nome, login, senha);
        this.email = email;
    }

    public String getEmail(){ 
        return email; 
    }
    public void setEmail(String email){ 
        this.email = email; 
    }

    @Override
    public String toString() {
        return "Administrador{id=" + getId() + ", nome='" + getNome()
                + "', login='" + getLogin() + "', email='" + email + "'}";
    }
}
