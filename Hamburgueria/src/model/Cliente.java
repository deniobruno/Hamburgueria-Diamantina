package model;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa um cliente da hamburgueria.
 * <p>
 * Utiliza um contador estático interno para geração automática de IDs únicos
 * e um histórico de pedidos realizados. Implementa {@link Comparator} para
 * permitir múltiplas estratégias de ordenação: por nome (padrão), por ID e
 * por telefone.
 * </p>
 *
 */
public class Cliente implements Comparator<Cliente>{

    private static int proximoId = 1;

    private int id;
    private String nome;
    private String telefone;
    private String endereco;
    private List<Integer> historicoIdPedidos;
    private int idUltimoPedido;
    private String dataUltimoPedido;

    /**
     * Cria um novo cliente com ID gerado automaticamente pelo sistema.
     *
     * @param nome nome completo do cliente
     * @param telefone telefone de contato
     * @param endereco endereço de entrega
     */
    public Cliente(String nome, String telefone, String endereco) {
        this.id = proximoId++;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.historicoIdPedidos = new ArrayList<>();
        this.idUltimoPedido = -1;
        this.dataUltimoPedido = "";
    }
    /**
     * Reconstrói um cliente existente a partir de dados persistidos.
     * Atualiza o contador estático {@code proximoId} se o ID informado for maior ou igual ao atual.
     *
     * @param id identificador único já existente
     * @param nome nome completo do cliente
     * @param telefone telefone de contato
     * @param endereco endereço de entrega
     */
    public Cliente(int id, String nome, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.historicoIdPedidos = new ArrayList<>();
        this.idUltimoPedido = -1;
        this.dataUltimoPedido = "";
        if (id >= proximoId) proximoId = id + 1;
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public String getNome() {
        return nome; 
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() { 
        return telefone; 
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEndereco() {
        return endereco; 
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco; 
    }
    public List<Integer> getHistoricoIdPedidos() { 
        return historicoIdPedidos; 
    }
    public void setHistoricoIdPedidos(List<Integer> historicoIdPedidos) {
        this.historicoIdPedidos = historicoIdPedidos;
    }
    public int getIdUltimoPedido() { 
        return idUltimoPedido; 
    }
    public void setIdUltimoPedido(int idUltimoPedido) {
        this.idUltimoPedido = idUltimoPedido; 
    }
    public String getDataUltimoPedido() { 
        return dataUltimoPedido;
    }
    public void setDataUltimoPedido(String dataUltimoPedido) {
        this.dataUltimoPedido = dataUltimoPedido;
    }
    /**
     * Registra um pedido realizado pelo cliente, atualizando o histórico
     * e os campos de último pedido.
     *
     * @param idPedido identificador do pedido realizado
     * @param data data de realização do pedido (formato {@code Dia/Mes/Ano})
     */
    public void registrarPedido(int idPedido, String data) {
        this.historicoIdPedidos.add(idPedido);
        this.idUltimoPedido = idPedido;
        this.dataUltimoPedido = data;
    }
    /**
     * Reajusta {@code proximoId} a partir do maior id carregado, evitando
     * colisão de IDs após o carregamento via Gson (que ignora o construtor).
     * @param lista lista de clientes carregada do JSON
     */
    public static void ajustarProximoId(List<Cliente> lista){
        int max = 0;
        for (Cliente c : lista) if (c.getId() > max) max = c.getId();
        proximoId = max + 1;
    }

    /**
     * Uso do comparator para ordenar clientes em ordem alfabética pelo nome.
     * A comparação é implementada manualmente,sem uso do compareTo.
     *
     * @param c1 primeiro cliente a ser comparado
     * @param c2 segundo cliente a ser comparado
     * @return valor negativo se {@code c1} vem antes de {@code c2},
     * positivo se {@code c1} vem depois, ou zero se forem iguais
     */
    @Override
    public int compare(Cliente c1, Cliente c2) {

    String nome1 = c1.getNome().toLowerCase();
    String nome2 = c2.getNome().toLowerCase();

    int tamanho = nome1.length();

    if(nome2.length() < tamanho){
        tamanho = nome2.length();
    }

    for(int i = 0; i < tamanho; i++){

        char letra1 = nome1.charAt(i);
        char letra2 = nome2.charAt(i);

        if(letra1 > letra2){
            return 1;
        }

        if(letra1 < letra2){
            return -1;
        }
    }

    if(nome1.length() > nome2.length()){
        return 1;
    }

    if(nome1.length() < nome2.length()){
        return -1;
    }

    return 0;
}
     /**
     * Ordena clientes pelo ID numérico em ordem crescente.
     *
     * @param c1 primeiro cliente a ser comparado
     * @param c2 segundo cliente a ser comparado
     * @return valor negativo se {@code c1.id} é menor, positivo se maior, ou zero se iguais
     */
    public int compararPorId(Cliente c1, Cliente c2){

    if(c1.getId() > c2.getId()){
        return 1;
    }

    if(c1.getId() < c2.getId()){
        return -1;
    }

    return 0;
}
     /**
     * Ordena clientes lexicograficamente pelo número de telefone.
     *
     * @param c1 primeiro cliente a ser comparado
     * @param c2 segundo cliente a ser comparado
     * @return valor negativo se {@code c1} vem antes de {@code c2},
     * positivo se vem depois, ou zero se os telefones forem iguais
     */
    public int compararPorTelefone(Cliente c1, Cliente c2){

    String t1 = c1.getTelefone();
    String t2 = c2.getTelefone();

    int tamanho = Math.min(t1.length(), t2.length());

    for(int i = 0; i < tamanho; i++){

        if(t1.charAt(i) > t2.charAt(i)){
            return 1;
        }

        if(t1.charAt(i) < t2.charAt(i)){
            return -1;
        }
    }

    return 0;
}

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', telefone='" + telefone
                + "', endereco='" + endereco + "', totalPedidos=" + historicoIdPedidos.size()
                + ", ultimoPedidoId=" + (idUltimoPedido == -1 ? "nenhum" : idUltimoPedido) + "}";
    }
}
