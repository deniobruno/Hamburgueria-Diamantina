/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Subtipo de Produto que representa um hambúrguer do cardápio.
 * Criado via ProdutoFactory,padrão Factory Method.
 */
public class Hamburguer extends Produto {

    public Hamburguer(int idDescricao, String descricao, double valor) {
        super(idDescricao, descricao, valor);
        
    }

    @Override
    public String toString() {
        return "[HAMBURGUER] " + super.toString();
    }
}
