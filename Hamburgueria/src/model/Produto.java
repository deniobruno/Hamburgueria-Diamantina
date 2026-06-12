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
  /**
  * Define o valor do contador privado. Utilizado pela persistência
  * para restaurar o estado ao carregar dados do JSON.
  *
  * @param valor novo valor do contador
  */
    protected static void setTotalProdutosPrivate(int valor){
        totalProdutosPrivate = valor;
    }

    /** Estratégia B: protected static, acessível por subclasses diretamente */
    protected static int totalProdutosProtected = 0;

    private int idDescricao;
    private String descricao;
    private double valor;
    private List<Adicional> adicionaisDisponiveis;
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

    @Override
    public String toString() {
        return "Produto{id=" + idDescricao + ", descricao='" + descricao
                + "', valor=R$" + String.format("%.2f", valor)
                + ", adicionais=" + adicionaisDisponiveis.size() + "}";
    }
}
