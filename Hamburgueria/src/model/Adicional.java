package model;

/**
 * Representa um adicional que pode ser acrescentado a um lanche.
 * <p>
 * Exemplos de adicionais: bacon, queijo duplo, molho especial.
 * Cada adicional possui um identificador único, nome e valor unitário.
 * </p>
 *
 */
public class Adicional {

    private int id;
    private String nome;
    private double valor;
    /**
     * Cria um novo adicional com os dados informados.
     *
     * @param id identificador único do adicional
     * @param nome nome do adicional (ex: Bacon)
     * @param valor preço do adicional em reais
     */
    public Adicional(int id, String nome, double valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
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
    public double getValor() {
        return valor; 
    }
    public void setValor(double valor){
        this.valor = valor; 
    }

    @Override
    public String toString() {
        return "Adicional{id=" + id + ", nome='" + nome + "', valor=R$" + String.format("%.2f", valor) + "}";
    }
}
