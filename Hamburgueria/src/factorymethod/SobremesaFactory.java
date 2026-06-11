/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factorymethod;

import model.Produto;    
import model.Sobremesa; 
/**
 * A atua como uma "fábrica concreta" dentro do padrão Factory Method.
 * Seu único objetivo é encapsular a lógica de criação de objetos do tipo Sobremesa.
 * Ela implementa a interface ProdutoFactory, o que garante que o sistema possua
 * um contrato padrão para criar diferentes tipos de produtos sem expor a lógica de instanciação.
 */
public class SobremesaFactory implements ProdutoFactory {
    @Override
    public Produto criar(int id, String descricao, double valor) {
        return new Sobremesa(id, descricao, valor);
    }
}
