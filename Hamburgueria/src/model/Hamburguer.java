/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Subtipo de Produto que representa um hambúrguer do cardápio.
 * Subtipo de {@link Produto} que representa um hambúrguer do cardápio.
 * <p>
 * Instâncias criadas exclusivamente por meio de {@link factorymethod.HamburguerFactory}, seguindo o padrão de projeto <b>Factory Method</b>.
 * </p>
 */
public class Hamburguer extends Produto {
/**
* Cria um novo hambúrguer com os dados informados.
*
* @param idDescricao identificador do produto no cardápio
* @param descricao nome ou descrição do hambúrguer
* @param valor preço unitário em reais
*/
    public Hamburguer(int idDescricao, String descricao, double valor) {
        super(idDescricao, descricao, valor);
        
    }

    @Override
    public String toString() {
        return "[HAMBURGUER] " + super.toString();
    }
}
