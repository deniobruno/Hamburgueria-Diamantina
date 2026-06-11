package factorymethod;

import model.Produto;

/**
 * Interface base para o padrão Factory Method.
 * Define o contrato para a criação de produtos, delegando a
 * instanciação real para as subclasses concretas.
 /**
 * Interface base do padrão de projeto <b>Factory Method</b>.
 * <p>
 * Define o contrato para a criação de produtos do cardápio, delegando
 * as instanciações:
 * {@link HamburguerFactory}, {@link BebidaFactory} e {@link SobremesaFactory}.
 * Permite ao código criar produtos sem precisar conhecer as classes concretas, 
 * promovendo baixo acoplamentoe facilidade de extensão.
 * </p>
 *
 */
public interface ProdutoFactory {
    
     /**
     * Factory Method: cria e retorna uma instância de {@link Produto} do tipo
     * que corresponde à fábrica concreta que implementa esta interface.
     *
     * @param id identificador do produto no cardápio
     * @param descricao nome ou descrição do produto
     * @param valor preço unitário em reais
     * @return nova instância de {@link Produto} 
     */
     Produto criar(int id, String descricao, double valor);
}