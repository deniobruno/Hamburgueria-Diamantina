package model;

/**
 * Representa o administrador do sistema.
 * <p>
 * Estende {@link Usuario} adicionando o campo de e-mail.
 * O administrador possui acesso total ao sistema, incluindo funcionalidades
 * exclusivas como gerenciamento de motoqueiros e regiões de entrega.
 * </p>
 *
 */
public class Administrador extends Usuario {

    private String email;
    
    /**
     * Cria um novo administrador com ID gerado automaticamente pelo sistema.
     *
     * @param nome nome completo do administrador
     * @param login login utilizado na autenticação
     * @param senha senha utilizada na autenticação
     * @param email endereço de e-mail do administrador
     */

    public Administrador(String nome, String login, String senha, String email) {
        super(nome, login, senha);
        this.email = email;
    }
     /**
     * Reconstrói um administrador existente a partir de dados persistidos.
     *
     * @param id identificador único já existente
     * @param nome nome completo do administrador
     * @param login login utilizado na autenticação
     * @param senha senha utilizada na autenticação
     * @param email endereço de e-mail do administrador
     */

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
