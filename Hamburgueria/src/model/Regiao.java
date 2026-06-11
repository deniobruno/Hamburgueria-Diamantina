package model;

/**
 * Representa uma região  de entrega da hamburgueria.
 * <p>
 * Regiões são associadas a pedidos e motoqueiros para organizar
 * a logistica de entregas. O administrador é responsável por cadastrar
 * e manter as regiões disponíveis no sistema.
 * </p>
 *
 */
public class Regiao {

    private int id;
    private String nome;
 /**
 * Cria uma nova região com os dados informados.
 *
 * @param id identificador único da região
 * @param nome nome do bairro (ex: Centro, Palha)
 */
    public Regiao(int id, String nome) {
        this.id = id;
        this.nome = nome;
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

    @Override
    public String toString() {
        return "Regiao{id=" + id + ", nome='" + nome + "'}";
    }
}
