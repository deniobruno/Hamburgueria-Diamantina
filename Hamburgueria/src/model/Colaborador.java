package model;

/**
 * Representa um colaborador (funcionário) do sistema.
 * <p>
 * Estende {@link Usuario} adicionando cargo e telefone.
 * Colaboradores acessam o sistema por meio do menu de atendimento,
 * com permissões operacionais (pedidos, clientes, entregas), mas sem
 * acesso às funções exclusivas do administrador.
 * </p>
 *
 */
public class Colaborador extends Usuario {

    private String cargo;
    private String telefone;

/**
* Cria um novo colaborador com ID gerado automaticamente pelo sistema.
*
* @param nome nome completo do colaborador
* @param login login utilizado na autenticação
* @param senha senha utilizada na autenticação
* @param cargo cargo ocupado pelo colaborador (ex: Atendente, Balconista)
* @param telefone telefone de contato do colaborador
*/
    public Colaborador(String nome, String login, String senha, String cargo, String telefone) {
        super(nome, login, senha);
        this.cargo = cargo;
        this.telefone = telefone;
    }
/**
* Reconstrói um colaborador existente a partir de dados persistidos (ex.: deserialização JSON).
*
* @param id identificador único já existente
* @param nome nome completo do colaborador
* @param login login utilizado na autenticação
* @param senha senha utilizada na autenticação
* @param cargo cargo ocupado pelo colaborador
* @param telefone telefone de contato do colaborador
*/

    public Colaborador(int id, String nome, String login, String senha, String cargo, String telefone) {
        super(id, nome, login, senha);
        this.cargo = cargo;
        this.telefone = telefone;
    }

    public String getCargo(){ 
        return cargo; 
    }
    public void setCargo(String cargo){ 
        this.cargo = cargo; 
    }
    public String getTelefone(){ 
        return telefone; 
    }
    public void setTelefone(String telefone){
        this.telefone = telefone; 
    }

    @Override
    public String toString() {
        return "Colaborador{id=" + getId() + ", nome='" + getNome()
                + "', login='" + getLogin() + "', cargo='" + cargo
                + "', telefone='" + telefone + "'}";
    }
}
