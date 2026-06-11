package model;

/**
 * Representa uma venda concluída, associando pedido, extrato e colaborador.
 */
public class Venda {

    private static int proximoId = 1;

    private int id;
    private int idPedido;
    private int idExtrato;
    private int idColaborador;
    private String data;
    private String horario;
    private boolean concluida;

    public Venda(int idPedido, int idExtrato, int idColaborador, String data, String horario) {
        this.id = proximoId++;
        this.idPedido = idPedido;
        this.idExtrato = idExtrato;
        this.idColaborador = idColaborador;
        this.data = data;
        this.horario = horario;
        this.concluida = true;
    }

    public Venda(int id, int idPedido, int idExtrato, int idColaborador,
                 String data, String horario, boolean concluida) {
        this.id = id;
        this.idPedido = idPedido;
        this.idExtrato = idExtrato;
        this.idColaborador = idColaborador;
        this.data = data;
        this.horario = horario;
        this.concluida = concluida;
        if (id >= proximoId) proximoId = id + 1;
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
    public int getIdExtrato(){ 
        return idExtrato; 
    }
    public void setIdExtrato(int idExtrato){
        this.idExtrato = idExtrato; 
    }
    public int getIdColaborador(){ 
        return idColaborador; 
    }
    public void setIdColaborador(int idColaborador){ 
        this.idColaborador = idColaborador; 
    }
    public String getData(){ 
        return data; 
    }
    public void setData(String data){ 
        this.data = data; 
    }
    public String getHorario(){ 
        return horario; 
    }
    public void setHorario(String horario){ 
        this.horario = horario; 
    }
    public boolean isConcluida(){ 
        return concluida; 
    }
    public void setConcluida(boolean concluida){ 
        this.concluida = concluida;
    }

    @Override
    public String toString() {
        return "Venda{id=" + id + ", pedidoId=" + idPedido + ", extratoId=" + idExtrato
                + ", colaboradorId=" + idColaborador + ", data='" + data + " " + horario
                + "', concluida=" + concluida + "}";
    }
}
