/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factorymethod;

import model.Produto;    
import model.Bebidas; 
/** A classe é outra "fábrica concreta" dentro do padrão Factory Method.
 * Ela implementa a interface ProdutoFactory para garantir o contrato padrão de criação.
 */
public class BebidaFactory implements ProdutoFactory {
    @Override
    public Produto criar(int id, String descricao, double valor) {
        return new Bebidas(id, descricao, valor);
    }
    
}
