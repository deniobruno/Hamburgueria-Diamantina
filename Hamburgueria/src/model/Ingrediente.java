package model;

/**
 * Representa um ingrediente do estoque da hamburgueria.
 * <p>
 * Armazena a quantidade atual disponível, a unidade de medida e a
 * quantidade mínima que serve como limite para disparo de alertas de
 * reposição. O sistema consome automaticamente 0,1 unidade de cada
 * ingrediente a cada pedido realizado.
 * </p>
 *
 */
public class Ingrediente {

    private int id;
    private String nome;
    private double quantidadeAtual;
    private String unidadeMedida;
    private double quantidadeMinima;
    /**
     * Cria um novo ingrediente com os dados informados.
     *
     * @param id identificador único do ingrediente
     * @param nome nome do ingrediente (ex: Carne)
     * @param quantidadeAtual quantidade disponível no estoque
     * @param unidadeMedida unidade de medida (ex: kg, L, g)
     * @param quantidadeMinima limite minimo para alerta de estoque baixo
     */
    public Ingrediente(int id, String nome, double quantidadeAtual,
                       String unidadeMedida, double quantidadeMinima) {
        this.id = id;
        this.nome = nome;
        this.quantidadeAtual = quantidadeAtual;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeMinima = quantidadeMinima;
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
    public double getQuantidadeAtual(){ 
        return quantidadeAtual;
    }
    public void setQuantidadeAtual(double quantidadeAtual){
        this.quantidadeAtual = quantidadeAtual; 
    }
    public String getUnidadeMedida(){ 
        return unidadeMedida; 
    }
    public void setUnidadeMedida(String unidadeMedida){
        this.unidadeMedida = unidadeMedida; 
    }
    public double getQuantidadeMinima(){ 
        return quantidadeMinima; 
    }
    public void setQuantidadeMinima(double quantidadeMinima){
        this.quantidadeMinima = quantidadeMinima; 
    }
    /**
     * Verifica se o ingrediente está abaixo ou no limite mínimo de estoque.
     *
     * @return {@code true} se {@code quantidadeAtual <= quantidadeMinima}; {@code false} caso não
     */

    public boolean estaEmAlerta(){
        return quantidadeAtual <= quantidadeMinima; 
    }
     /**
     * Reduz a quantidade do ingrediente no estoque, garantindo que o valor
     * nunca seja negativo.
     *
     * @param quantidade valor a ser consumido do estoque
     */
    public void consumir(double quantidade){
        this.quantidadeAtual = Math.max(0, this.quantidadeAtual - quantidade); 
    }
     /**
     * Aumenta a quantidade do ingrediente no estoque.
     *
     * @param quantidade valor a ser adicionado ao estoque
     */
    public void repor(double quantidade){ 
        this.quantidadeAtual += quantidade; 
    }

    @Override
    public String toString() {
        return "Ingrediente{id=" + id + ", nome='" + nome + "', quantidade=" + quantidadeAtual+ unidadeMedida + ", minimo=" + quantidadeMinima
                 + (estaEmAlerta() ? " [ALERTA]" : "") + "}";
    }
}
