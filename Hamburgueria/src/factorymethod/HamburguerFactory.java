/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factorymethod;

import model.Produto;    
import model.Hamburguer; 

/**
 * Fábrica concreta do padrão de projeto <b>Factory Method</b> responsável
 * pela criação de instâncias do tipo {@link Hamburguer}.
 * <p>
 * Implementa {@link ProdutoFactory}, garantindo o contrato padrão de criação
 * de produtos e desacoplando o código cliente da classe {@link Hamburguer}.
 * </p>
 *
 */
public class HamburguerFactory implements ProdutoFactory {
/**
* Cria e retorna uma nova instância de {@link Hamburguer}.
*
* @param id identificador do produto no cardápio
* @param descricao nome ou descrição do hambúrguer
* @param valor preço unitário em reais
* @return nova instância de {@link Hamburguer}
*/
    @Override
    public Produto criar(int id, String descricao, double valor) {
        return new Hamburguer(id, descricao, valor);
    }
}