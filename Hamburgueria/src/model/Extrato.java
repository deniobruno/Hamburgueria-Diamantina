package model;

/**
 * Representa o extrato/recibo gerado automaticamente para cada pedido.
 */
public class Extrato {

    private static final String NOME_EMPRESA = "Hamburgueria Diamantina";
    private static final String CNPJ_EMPRESA = "12.590.920/0001-07";
    private static int proximoId = 1;

    private int id;
    private int idPedido;
    private int idCliente;
    private String nomeCliente;
    private double valorTotal;
    private double valorCancelamento;
    private boolean cancelado;
    private String dataGeracao;
    private String horarioGeracao;
    private String descricaoProduto;

    public Extrato(int idPedido, int idCliente, String nomeCliente, double valorTotal,
                   String dataGeracao, String horarioGeracao, String descricaoProduto) {
        this.id = proximoId++;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.valorTotal = valorTotal;
        this.valorCancelamento = 0;
        this.cancelado = false;
        this.dataGeracao = dataGeracao;
        this.horarioGeracao = horarioGeracao;
        this.descricaoProduto = descricaoProduto;
    }

    public Extrato(int id, int idPedido, int idCliente, String nomeCliente,
                   double valorTotal, double valorCancelamento, boolean cancelado,
                   String dataGeracao, String horarioGeracao, String descricaoProduto) {
        this.id = id;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.valorTotal = valorTotal;
        this.valorCancelamento = valorCancelamento;
        this.cancelado = cancelado;
        this.dataGeracao = dataGeracao;
        this.horarioGeracao = horarioGeracao;
        this.descricaoProduto = descricaoProduto;
        if (id >= proximoId) proximoId = id + 1;
    }

    public void marcarCancelado(double taxaRetida) {
        this.cancelado = true;
        this.valorCancelamento = taxaRetida;
    }

    public void imprimir() {
        String linha = "================================================";
        System.out.println(linha);
        System.out.println("           " + NOME_EMPRESA);
        System.out.println("           CNPJ: " + CNPJ_EMPRESA);
        System.out.println(linha);
        System.out.println("EXTRATO #" + id + "   Pedido #" + idPedido);
        System.out.println("Data: " + dataGeracao + "  Hora: " + horarioGeracao);
        System.out.println("Cliente: " + nomeCliente + " (ID: " + idCliente + ")");
        System.out.println("Produto: " + descricaoProduto);
        System.out.println(linha);
        if (cancelado) {
            System.out.printf("Status: CANCELADO%n");
            System.out.printf("Taxa de cancelamento (35%%): R$%.2f%n", valorCancelamento);
            System.out.printf("Valor devolvido (65%%):      R$%.2f%n", valorTotal - valorCancelamento);
        } else {
            System.out.printf("Status: PAGO%n");
            System.out.printf("TOTAL: R$%.2f%n", valorTotal);
        }
        System.out.println(linha);
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){
        this.id = id;
    }
    public int getIdPedido(){
        return idPedido;
    }
    public void setIdPedido(int idPedido){ 
        this.idPedido = idPedido; 
    }
    public int getIdCliente(){
        return idCliente;
    }
    public void setIdCliente(int idCliente){
        this.idCliente = idCliente;
    }
    public String getNomeCliente(){ 
        return nomeCliente; 
    }
    public void setNomeCliente(String nomeCliente){
        this.nomeCliente = nomeCliente; 
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
    public void setValorCancelamento(double v){
        this.valorCancelamento = v; 
    }
    public boolean isCancelado(){
        return cancelado;
    }
    public void setCancelado(boolean cancelado){
        this.cancelado = cancelado; 
    }
    public String getDataGeracao(){ 
        return dataGeracao;
    }
    public void setDataGeracao(String dataGeracao){
        this.dataGeracao = dataGeracao; 
    }
    public String getHorarioGeracao(){ 
        return horarioGeracao; 
    }
    public void setHorarioGeracao(String horarioGeracao){ 
        this.horarioGeracao = horarioGeracao;
    }
    public String getDescricaoProduto(){ 
        return descricaoProduto; 
    }
    public void setDescricaoProduto(String descricaoProduto){
        this.descricaoProduto = descricaoProduto; 
    }

    @Override
    public String toString() {
        return "Extrato{id=" + id + ", pedidoId=" + idPedido + ", cliente='" + nomeCliente
                + "', valor=R$" + String.format("%.2f", valorTotal)
                + (cancelado ? ", CANCELADO, taxaRetida=R$" + String.format("%.2f", valorCancelamento) : "")
                + ", data='" + dataGeracao + " " + horarioGeracao + "'}";
    }
}
