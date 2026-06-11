/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factorymethod;

import model.Produto;    
import model.Hamburguer; 
/**A classe é outra "fábrica concreta" dentro do padrão Factory Method.
 * Ela implementa a interface ProdutoFactory para garantir o contrato padrão de criação.
 */
public class HamburguerFactory implements ProdutoFactory {
    @Override
    public Produto criar(int id, String descricao, double valor) {
        return new Hamburguer(id, descricao, valor);
    }
}