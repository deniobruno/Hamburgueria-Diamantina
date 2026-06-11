package model;

 /**
 * Representa uma estação de preparo da cozinha da hamburgueria.
 * <p>
 * O sistema possui exatamente 3 estações de trabalho armazenadas em um
 * vetor estático de tamanho fixo (Alternativa 5): Grelhados, Montagem
 * e Embalagem. As estações são inicializadas em bloco {@code static} e
 * compartilhadas por toda a aplicação. Quando todas estão ocupadas, novos
 * pedidos são enfileirados em {@link sistema.FilaPedidos}.
 * </p>
 *
 */
public class EstacaoPreparo {

 /**
 * Vetor estático fixo com as 3 estações de trabalho da cozinha (Alternativa 5).
 * Inicializado em bloco estático com as estações: Grelhados (1),
 * Montagem (2) e Embalagem (3).
 */
    public static final EstacaoPreparo[] estacoes = new EstacaoPreparo[3];

    static {
        estacoes[0] = new EstacaoPreparo(1, "Estacao Grelhados");
        estacoes[1] = new EstacaoPreparo(2, "Estacao Montagem");
        estacoes[2] = new EstacaoPreparo(3, "Estacao Embalagem");
    }

    private int id;
    private String nome;
    private Integer idPedidoAtual;
/**
* Cria uma estação de preparo com o ID e nome informados.
* A estação é criada no estado livre ({@code idPedidoAtual = null}).
*
* @param id identificador da estação (1, 2 ou 3)
* @param nome nome descritivo da estação (ex: Estacao Grelhados)
     */
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
/**
* Verifica se a estação está livre para receber um novo pedido.
*
* @return {@code true} se nenhum pedido está sendo preparado;{@code false} caso não.
* 
*/
    public boolean isLivre(){
        return idPedidoAtual == null; 
    }
/**
* Aloca um pedido a esta estação, deixando ela como ocupada.
*
* @param idPedido identificador do pedido a ser preparado nesta estação
*/
    public void alocarPedido(int idPedido){
        this.idPedidoAtual = idPedido;
    }
/**
* Libera a estação, tornando-a disponível para o próximo pedido.
*/
    public void liberar(){ 
        this.idPedidoAtual = null; 
    }
/**
* Percorre o vetor estático {@link #estacoes} e retorna a primeira
* estação que estiver livre.
*
* @return a primeira {@code EstacaoPreparo} disponível, ou {@code null}
*se todas estiverem ocupadas
*/
    public static EstacaoPreparo encontrarEstacaoLivre() {
        for (EstacaoPreparo e : estacoes) {
            if (e.isLivre()) return e;
        }
        return null;
    }
/**
* Imprime no console o status atual de todas as estações de preparo.
*/
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
