package model;

/**
 * Representa uma estação de preparo da hamburgueria.
 * O sistema possui exatamente 3 estações em vetor estático de tamanho fixo (Alternativa 5).
 */
public class EstacaoPreparo {

    /** Vetor estático fixo com as 3 estações de trabalho (Alternativa 5). */
    public static final EstacaoPreparo[] estacoes = new EstacaoPreparo[3];

    static {
        estacoes[0] = new EstacaoPreparo(1, "Estacao Grelhados");
        estacoes[1] = new EstacaoPreparo(2, "Estacao Montagem");
        estacoes[2] = new EstacaoPreparo(3, "Estacao Embalagem");
    }

    private int id;
    private String nome;
    private Integer idPedidoAtual;

    public EstacaoPreparo(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.idPedidoAtual = null;
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){
        this.id = id; 
    }
    public String getNome(){
        return nome; 
    }
    public void setNome(String nome){ 
        this.nome = nome; 
    }
    public Integer getIdPedidoAtual(){ 
        return idPedidoAtual; 
    }
    public void setIdPedidoAtual(Integer idPedidoAtual){
        this.idPedidoAtual = idPedidoAtual; 
    }
    public boolean isLivre(){
        return idPedidoAtual == null; 
    }
    public void alocarPedido(int idPedido){
        this.idPedidoAtual = idPedido;
    }
    public void liberar(){ 
        this.idPedidoAtual = null; 
    }
    
    public static EstacaoPreparo encontrarEstacaoLivre() {
        for (EstacaoPreparo e : estacoes) {
            if (e.isLivre()) return e;
        }
        return null;
    }

    public static void imprimirStatus() {
        System.out.println("=== Estacoes de Preparo ===");
        for (EstacaoPreparo e : estacoes) System.out.println(e);
    }

    @Override
    public String toString() {
        return "EstacaoPreparo{id=" + id + ", nome='" + nome
                + "', status=" + (isLivre() ? "LIVRE" : "Pedido #" + idPedidoAtual) + "}";
    }
}
