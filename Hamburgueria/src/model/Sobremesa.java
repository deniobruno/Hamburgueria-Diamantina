/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Subtipo de Produto que representa uma sobremesa do cardápio.
 * Criado via ProdutoFactory, padrão Factory Method.
 */
public class Sobremesa extends Produto {

    public Sobremesa(int idDescricao, String descricao, double valor) {
        super(idDescricao, descricao, valor);
    }

    @Override
    public String toString() {
        return "[SOBREMESA] " + super.toString();
    }
}
