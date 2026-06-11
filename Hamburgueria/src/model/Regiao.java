package model;

/**
 * Representa uma região (bairro ou área) de entrega da hamburgueria.
 */
public class Regiao {

    private int id;
    private String nome;

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
