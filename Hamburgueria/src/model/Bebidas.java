/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

 /**
 * Subtipo de {@link Produto} que representa uma bebida do cardápio.
 * <p>
 * Instâncias criadas exclusivamente por meio de{@link factorymethod.BebidaFactory}, seguindo o padrão de projeto <b>Factory Method</b>.
 * </p>
 */
public class Bebidas extends Produto {
/**
* Cria uma nova bebida com os dados informados.
*
* @param idDescricao identificador do produto no cardápio
* @param descricao nome ou descrição da bebida
* @param valor preço unitário em reais
*/
    public Bebidas(int idDescricao, String descricao, double valor) {
        super(idDescricao, descricao, valor);
    }

    @Override
    public String toString() {
        return "[BEBIDA] " + super.toString();
    }
}