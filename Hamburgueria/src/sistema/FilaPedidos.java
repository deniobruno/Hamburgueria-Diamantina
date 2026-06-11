package sistema;

import model.Pedido;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Fila FIFO para gerenciamento de pedidos em espera (Alternativa 16).
 */
public class FilaPedidos {

    private final Queue<Pedido> fila;

    public FilaPedidos(){ 
        this.fila = new LinkedList<>(); 
    }

    public void enfileirar(Pedido pedido) {
        fila.offer(pedido);
        System.out.println("[Fila] Pedido #" + pedido.getId() + " adicionado. Tamanho: " + fila.size());
    }

    public Pedido desenfileirar() {
        Pedido p = fila.poll();
        if (p != null) System.out.println("[Fila] Pedido #" + p.getId() + " retirado. Restante: " + fila.size());
        return p;
    }

    public Pedido espiar(){
        return fila.peek(); 
    }
    public boolean remover(int idPedido){
        return fila.removeIf(p -> p.getId() == idPedido);
    }
    public boolean isEmpty(){
        return fila.isEmpty(); 
    }
    public int tamanho(){
        return fila.size(); 
    }

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
