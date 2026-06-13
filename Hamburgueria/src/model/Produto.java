package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um produto/lanche do cardápio.
 *
 * Estratégias de contador estático (Alternativa 11):
 * Estratégia A: {@code private static} com get/set:
 * Vantagem: encapsulamento total, nenhuma outra classe pode alterar o contador
 * diretamente. Segue o princípio de menor privilégio.
 * Desvantagem: requer métodos extras (get/set).
 *
 * Estratégia B: {@code protected static}:
 * Vantagem: subclasses e classes do mesmo pacote podem ler/escrever diretamente,
 * reduzindo código boilerplate.
 * Desvantagem: qualquer subclasse pode acidentalmente sobrescrever o valor,
 * quebrando o encapsulamento.
 */
public class Produto {

    /** Estratégia A: private static + get/set */
    private static int totalProdutosPrivate = 0;

 /**
 * Retorna o total de produtos criados, Estratégia A (contador privado).
 *
 * @return número total de instâncias de {@code Produto} criadas
 */    
    public static int getTotalProdutosPrivate(){ 
        return totalProdutosPrivate; 
    }
    /** Estratégia B: protected static, acessível por subclasses diretamente */
    protected static int totalProdutosProtected = 0;
  /**
  * Sincroniza ambos os contadores estáticos com o valor informado.
  * Chamado por {@code Sistema.carregar()} após a leitura do JSON para que os
  * contadores reflitam os produtos persistidos (o Gson não executa o construtor
  * ao desserializar, então o incremento dentro do construtor não roda na leitura).
  *
  * @param valor novo valor´para ambos os contadores
  */
    public static void setTotalProdutos(int valor){
         totalProdutosPrivate = valor;
         totalProdutosProtected = valor;
     }

    private int idDescricao;
    private String descricao;
    private double valor;
    private List<Adicional> adicionaisDisponiveis;
    
    /**
     * Discriminador de tipo usado para preservar o polimorfismo na persistência.
     * É preenchido automaticamente com o nome da classe concreta no construtor
     * ("Hamburguer", "Bebidas", "Sobremesa" ou "Produto"). Na leitura do JSON,
     * {@code Sistema} usa este campo para reconstruir o subtipo correto via fábrica.
     */
    private String tipo;

    /** IDs dos ingredientes que compõem este produto (receita), usados na baixa de estoque. */
    private List<Integer> idsIngredientes;
    /**
    * Cria um novo produto, incrementando ambos os contadores estáticos.
    *
    * @param idDescricao identificador do produto no cardápio
    * @param descricao nome ou descrição do produto
    * @param valor preço unitário em reais
    */
    public Produto(int idDescricao, String descricao, double valor) {
        this.idDescricao = idDescricao;
        this.descricao = descricao;
        this.valor = valor;
        this.adicionaisDisponiveis = new ArrayList<>();
        this.idsIngredientes = new ArrayList<>();
        this.tipo = getClass().getSimpleName(); // "Hamburguer", "Bebidas", "Sobremesa" ou "Produto"
        totalProdutosPrivate++;
        totalProdutosProtected++;
    }

    public int getIdDescricao(){ 
        return idDescricao; 
    }
    public void setIdDescricao(int idDescricao){
        this.idDescricao = idDescricao;
    }
    public String getDescricao(){ 
        return descricao; 
    }
    public void setDescricao(String descricao){
        this.descricao = descricao; 
    }
    public double getValor(){ 
        return valor; 
    }
    public void setValor(double valor){ 
        this.valor = valor; 
    }
    public List<Adicional> getAdicionaisDisponiveis(){ 
        return adicionaisDisponiveis; 
    }
    public void setAdicionaisDisponiveis(List<Adicional> adicionaisDisponiveis){
        this.adicionaisDisponiveis = adicionaisDisponiveis; 
    }
   /**
    * Acrescenta um adicional à lista de adicionais disponíveis para este produto.
    *
    * @param a adicional a ser incluído
    */
    public void adicionarAdicional(Adicional a){ 
        this.adicionaisDisponiveis.add(a); 
    }
    public String getTipo(){
        return tipo;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public List<Integer> getIdsIngredientes(){
        if (idsIngredientes == null) idsIngredientes = new ArrayList<>();
        return idsIngredientes;
    }
    public void setIdsIngredientes(List<Integer> idsIngredientes){
        this.idsIngredientes = idsIngredientes;
    }
    /**
     * Vincula um ingrediente (por id) à receita deste produto.
     * @param idIngrediente identificador do ingrediente consumido por este produto
     */
    public void vincularIngrediente(int idIngrediente){
        getIdsIngredientes().add(idIngrediente);
    }

    @Override
    public String toString() {
        return "Produto{id=" + idDescricao + ", descricao='" + descricao
                + "', valor=R$" + String.format("%.2f", valor)
                + ", adicionais=" + adicionaisDisponiveis.size() + "}";
    }
}
