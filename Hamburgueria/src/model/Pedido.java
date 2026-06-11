package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa um pedido realizado por um cliente.
 * Contador estático para rastrear instâncias criadas (Alternativa 12).
 * Implementa {@link Comparator} para ordenações por diferentes atributos (Alternativa 13).
 */
public class Pedido implements Comparator<Pedido>{

    public enum Status { PENDENTE, EM_PREPARO, PRONTO, SAIU_ENTREGA, ENTREGUE, CANCELADO }

    private static int totalPedidosCriados = 0;
    private static int proximoId = 1;

    /** Retorna o total de instâncias de Pedido criadas (Alternativa 12). */
    public static int getTotalPedidosCriados(){
        return totalPedidosCriados; 
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
     * @return valor devolvido ao cliente (65%)
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

    /** Compara por data e horário (mais antigo primeiro). */
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
    public int compararPorValor(Pedido p1, Pedido p2){

    if(p1.getValorTotal() > p2.getValorTotal()){
        return 1;
    }

    if(p1.getValorTotal() < p2.getValorTotal()){
        return -1;
    }

    return 0;
}
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