/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


 /**
 * Subtipo de {@link Produto} que representa uma sobremesa do cardápio.
 * <p>
 * Instâncias criadas exclusivamente por meio de {@link factorymethod.SobremesaFactory}, seguindo o padrão de projeto<b>Factory Method</b>.
 * </p>
 *
 * @author Dênio Mingote
 */
public class Sobremesa extends Produto {
/**
* Cria uma nova sobremesa com os dados informados.
*
* @param idDescricao identificador do produto no cardápio
* @param descricao nome ou descrição da sobremesa
* @param valor preço unitário em reais
*/
    public Sobremesa(int idDescricao, String descricao, double valor) {
        super(idDescricao, descricao, valor);
    }

    @Override
    public String toString() {
        return "[SOBREMESA] " + super.toString();
    }
}
