package sistema;

import model.Pedido;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Fila FIFO para gerenciamento de pedidos em espera (Alternativa 16).
 * <p>
 * Utilizada quando todas as estações de preparo estão ocupadas:
 * novos pedidos são enfileirados e processados na ordem de chegada
 * à medida que as estações ficam disponíveis. Internamente utiliza
 * {@link LinkedList} por meio da interface {@link Queue}.
 * </p>
 *
 */
public class FilaPedidos {

    private final Queue<Pedido> fila;
 /**
 * Cria uma nova fila de pedidos vazia.
 */
    public FilaPedidos(){ 
        this.fila = new LinkedList<>(); 
    }
 /**
 * Adiciona um pedido ao final da fila e imprime o tamanho atual.
 *
 * @param pedido pedido a ser enfileirado
 */
    public void enfileirar(Pedido pedido) {
        fila.offer(pedido);
        System.out.println("[Fila] Pedido #" + pedido.getId() + " adicionado. Tamanho: " + fila.size());
    }
 /**
 * Remove e retorna o primeiro pedido da fila (FIFO).
 *
 * @return o pedido mais antigo da fila, ou {@code null} se a fila estiver vazia
 */
    public Pedido desenfileirar() {
        Pedido p = fila.poll();
        if (p != null) System.out.println("[Fila] Pedido #" + p.getId() + " retirado. Restante: " + fila.size());
        return p;
    }
 /**
 * Retorna o primeiro pedido da fila sem remover ele.
 *
 * @return o pedido no início da fila, ou {@code null} se vazia
 */
    public Pedido espiar(){
        return fila.peek(); 
    }
 
 /**
 * Remove da fila o pedido com o identificador informado, caso exista.
 *
 * @param idPedido identificador do pedido a ser removido
 * @return {@code true} se o pedido foi encontrado e removido; {@code false} caso contrário
 */

    public boolean remover(int idPedido){
        return fila.removeIf(p -> p.getId() == idPedido);
    }
 /**
 * Verifica se a fila está vazia.
 *
 * @return {@code true} se não há pedidos em espera; {@code false} caso contrário
 */
    public boolean isEmpty(){
        return fila.isEmpty(); 
    }
 /**
 * Retorna a quantidade de pedidos atualmente na fila.
 *
 * @return número de pedidos em espera
 */
    public int tamanho(){
        return fila.size(); 
    }
 /**
 * Imprime no console todos os pedidos atualmente em espera na fila.
 */
    public void imprimirFila() {
        if (fila.isEmpty()) { System.out.println("[Fila] Fila vazia."); 
        return; 
        }
        System.out.println("[Fila] Pedidos em espera (" + fila.size() + "):");
        for (Pedido p : fila) System.out.println("  " + p);
    }

    @Override
    public String toString() { 
        return "FilaPedidos{tamanho=" + fila.size() + "}"; 
    }
}
