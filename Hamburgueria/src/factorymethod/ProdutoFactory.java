package factorymethod;

import model.Produto;

/**
 * Interface base para o padrão Factory Method.
 * Define o contrato para a criação de produtos, delegando a
 * instanciação real para as subclasses concretas.
 */
public interface ProdutoFactory {
    
    // Factory Method que deve ser implementado pelas subclasses.
     Produto criar(int id, String descricao, double valor);
}