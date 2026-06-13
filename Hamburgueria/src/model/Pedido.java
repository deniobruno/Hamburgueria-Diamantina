package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa um pedido realizado pelo cliente da hamburgueria.
 * <p>
 * Mantém um contador estático ({@code totalPedidosCriados}) que rastreia
 * o total de instâncias criadas durante toda a execução do sistema
 * (Alternativa 12). Implementa {@link Comparator} para permitir três
 * critérios de ordenação distintos: por data (padrão), por valor total
 * e por status (Alternativa 13).
 * </p>
 *
 */
public class Pedido implements Comparator<Pedido>{
    /**
     * Enumeração dos possíveis estados de um pedido no fluxo de atendimento.
     */
    public enum Status { PENDENTE, EM_PREPARO, PRONTO, SAIU_ENTREGA, ENTREGUE, CANCELADO }

    private static int totalPedidosCriados = 0;
    private static int proximoId = 1;

    /**
     * Retorna o total de instâncias de {@code Pedido} criadas desde o início
     * da execução do sistema (Alternativa 12).
     *
     * @return número total de pedidos instanciados
     */
    public static int getTotalPedidosCriados(){
        return totalPedidosCriados; 
    }
    /**
     * Sincroniza o contador de pedidos com o nº de pedidos persistidos.
     * Chamado por {@code Sistema.carregar()} (o Gson não executa o construtor na leitura).
     * @param valor novo valor do contador
     */
      public static void setTotalPedidosCriados(int valor){
        totalPedidosCriados = valor;
    }
    /**
     * Reajusta {@code proximoId} a partir do maior id já existente, evitando
     * colisão de IDs após o carregamento (o Gson ignora o construtor de reconstrução).
     * @param lista lista de pedidos carregada do JSON
     */
      public static void ajustarProximoId(java.util.List<Pedido> lista){
        int max = 0;
        for (Pedido p : lista) if (p.getId() > max) max = p.getId();
        proximoId = max + 1;
   }
    private int id;
    private int idCliente;
    private List<Integer> idsProdutos;
    private List<Integer> idsAdicionais;
    private String data;
    private String horarioPedido;
    private String horarioEntregaPrevisto;
    private Status status;
    private double valorTotal;
    private double valorCancelamento;
    /**
     * Cria um novo pedido com ID gerado automaticamente pelo sistema.
     * Incrementa o contador estático {@code totalPedidosCriados}.
     *
     * @param idCliente identificador do cliente que realizou o pedido
     * @param idsProdutos lista de IDs dos produtos solicitados
     * @param data data do pedido no formato {@code Dia/Mes/Ano}
     * @param horarioPedido horário de criação no formato {@code HH:mm}
     * @param horarioEntregaPrevisto horário previsto para a entrega
     * @param valorTotal valor total calculado (produtos e adicionais)
     */

    public Pedido(int idCliente, List<Integer>idsProdutos, String data, String horarioPedido, String horarioEntregaPrevisto, double valorTotal) {
        this.id = proximoId++;
        this.idCliente = idCliente;
        this.idsProdutos = idsProdutos;
        this.idsAdicionais = new ArrayList<>();
        this.data = data;
        this.horarioPedido = horarioPedido;
        this.horarioEntregaPrevisto = horarioEntregaPrevisto;
        this.status = Status.PENDENTE;
        this.valorTotal = valorTotal;
        this.valorCancelamento = 0;
        totalPedidosCriados++;
    }
     /**
     * Reconstrói um pedido existente a partir de dados persistidos.
     * Também incrementa {@code totalPedidosCriados} e atualiza {@code proximoId} se necessário.
     *
     * @param id identificador único já existente
     * @param idCliente identificador do cliente
     * @param idsProdutos lista de IDs dos produtos
     * @param data data do pedido no formato {@code Dia/Mes/Ano}
     * @param horarioPedido horário de criação no formato {@code HH:mm}
     * @param horarioEntregaPrevisto horário previsto para a entrega
     * @param valorTotal valor total do pedido
     * @param status status atual do pedido
     */
    public Pedido(int id, int idCliente, List<Integer> idsProdutos, String data, String horarioPedido, String horarioEntregaPrevisto, double valorTotal, Status status) {
        this.id = id;
        this.idCliente = idCliente;
        this.idsProdutos = idsProdutos;
        this.idsAdicionais = new ArrayList<>();
        this.data = data;
        this.horarioPedido = horarioPedido;
        this.horarioEntregaPrevisto = horarioEntregaPrevisto;
        this.status = status;
        this.valorTotal = valorTotal;
        this.valorCancelamento = 0;
        if (id >= proximoId) proximoId = id + 1;
        totalPedidosCriados++;
    }
    /**
     * Cancela o pedido, retendo 35% do valor como taxa.
     * <p>
     * Atualiza o status para {@link Status#CANCELADO} e imprime no console
     * o valor retido e o valor devolvido ao cliente (65%).
     * </p>
     *
     * @return valor devolvido ao cliente, correspondente a 65% do total
     * @throws IllegalStateException se o pedido já foi entregue ou já está cancelado
     */
    public double cancelar() {
        if (status == Status.ENTREGUE) throw new IllegalStateException("Pedido já entregue.");
        if (status == Status.CANCELADO) throw new IllegalStateException("Pedido já está cancelado.");
        this.valorCancelamento = valorTotal * 0.35;
        this.status = Status.CANCELADO;
        double devolvido = valorTotal * 0.65;
        System.out.printf("Pedido #%d cancelado. Taxa retida: R$%.2f | Devolvido: R$%.2f%n",
                id, valorCancelamento, devolvido);
        return devolvido;
    }

    public int getId(){ 
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }
    public int getIdCliente(){
        return idCliente; 
    }
    public void setIdCliente(int idCliente){
        this.idCliente = idCliente; 
    }
    public List<Integer> getIdsProdutos(){
        return idsProdutos; 
    }
    public void setIdsProdutos(List<Integer> idsProdutos){ 
        this.idsProdutos = idsProdutos; 
    }
    public List<Integer> getIdsAdicionais(){ 
        return idsAdicionais; 
    }
    public void setIdsAdicionais(List<Integer> idsAdicionais){
        this.idsAdicionais = idsAdicionais; 
    }
    /**
     * Adiciona um adicional à lista de adicionais deste pedido.
     *
     * @param idAdicional identificador do adicional a ser incluído
     */
    public void adicionarAdicional(int idAdicional){ 
        this.idsAdicionais.add(idAdicional); 
    }
    public String getData(){
        return data; 
    }
    public void setData(String data){ 
        this.data = data; 
    }
    public String getHorarioPedido(){
        return horarioPedido;
    }
    public void setHorarioPedido(String horarioPedido){
        this.horarioPedido = horarioPedido; 
    }
    public String getHorarioEntregaPrevisto(){
        return horarioEntregaPrevisto; 
    }
    public void setHorarioEntregaPrevisto(String h)
    { this.horarioEntregaPrevisto = h; 
    }
    public Status getStatus(){
        return status; 
    }
    public void setStatus(Status status){
        this.status = status;
    }
    public double getValorTotal(){
        return valorTotal; 
    }
    public void setValorTotal(double valorTotal){ 
        this.valorTotal = valorTotal; 
    }
    public double getValorCancelamento(){
        return valorCancelamento; 
    }
    public void setValorCancelamento(double valorCancelamento){ 
        this.valorCancelamento = valorCancelamento; 
    }

      /**
     * Comparator padrão: ordena pedidos por data em ordem cronológica crescente
     * (mais antigo primeiro). A comparação é feita via formato
     * {@code Dia/Mes/Ano}, sem uso de classes de data.
     *
     * @param p1 primeiro pedido a ser comparado
     * @param p2 segundo pedido a ser comparado
     * @return valor negativo se {@code p1} é mais antigo, positivo se mais recente,
     * ou zero se as datas forem iguais
     */
    @Override
    public int compare(Pedido p1, Pedido p2) {

    String[] data1 = p1.getData().split("/");
    String[] data2 = p2.getData().split("/");

    int dia1 = Integer.parseInt(data1[0]);
    int mes1 = Integer.parseInt(data1[1]);
    int ano1 = Integer.parseInt(data1[2]);

    int dia2 = Integer.parseInt(data2[0]);
    int mes2 = Integer.parseInt(data2[1]);
    int ano2 = Integer.parseInt(data2[2]);

    if(ano1 > ano2)
        return 1;

    if(ano1 < ano2)
        return -1;

    if(mes1 > mes2)
        return 1;

    if(mes1 < mes2)
        return -1;

    if(dia1 > dia2)
        return 1;

    if(dia1 < dia2)
        return -1;

    return 0;
}
     /**
     * Ordena pedidos pelo valor total em ordem crescente.
     *
     * @param p1 primeiro pedido a ser comparado
     * @param p2 segundo pedido a ser comparado
     * @return valor negativo se {@code p1} tem menor valor, positivo se maior, zero se iguais
     */
    public int compararPorValor(Pedido p1, Pedido p2){

    if(p1.getValorTotal() > p2.getValorTotal()){
        return 1;
    }

    if(p1.getValorTotal() < p2.getValorTotal()){
        return -1;
    }

    return 0;
}
     /**
     * Ordena pedidos pela posição ordinal do seu {@link Status} no enum,
     * permitindo agrupar pedidos por etapa do fluxo de atendimento.
     *
     * @param p1 primeiro pedido a ser comparado
     * @param p2 segundo pedido a ser comparado
     * @return valor negativo se {@code p1} tem status anterior, positivo se posterior,
     * zero se iguais
     */
    public int compararPorStatus(Pedido p1, Pedido p2){

    int status1 = p1.getStatus().ordinal();
    int status2 = p2.getStatus().ordinal();

    if(status1 > status2){
        return 1;
    }

    if(status1 < status2){
        return -1;
    }

    return 0;
}

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", clienteId=" + idCliente + ", produtoId=" + idsProdutos
                + ", data='" + data + "', horario='" + horarioPedido
                + "', entregaPrevista='" + horarioEntregaPrevisto
                + "', valor=R$" + String.format("%.2f", valorTotal)
                + ", status=" + status
                + (status == Status.CANCELADO ? ", taxaRetida=R$" + String.format("%.2f", valorCancelamento) : "")
                + "}";
    }
}