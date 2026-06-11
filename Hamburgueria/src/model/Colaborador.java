package model;

/**
 * Representa um colaborador (funcionário) do sistema.
 * Herda de {@link Usuario}.
 */
public class Colaborador extends Usuario {

    private String cargo;
    private String telefone;

    public Colaborador(String nome, String login, String senha, String cargo, String telefone) {
        super(nome, login, senha);
        this.cargo = cargo;
        this.telefone = telefone;
    }

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
