package model;

/**
 * Representa uma entrega, associando pedido a motoqueiro e região.
 */
public class Entrega {

    public enum StatusEntrega { AGUARDANDO_RETIRADA, EM_ROTA, CONCLUIDA, FALHA }

    private static int proximoId = 1;

    private int id;
    private int idPedido;
    private int idMotoqueiro;
    private int idRegiao;
    private StatusEntrega status;
    private String horarioRetirada;
    private String horarioConclusao;

    public Entrega(int idPedido, int idMotoqueiro, int idRegiao) {
        this.id = proximoId++;
        this.idPedido = idPedido;
        this.idMotoqueiro = idMotoqueiro;
        this.idRegiao = idRegiao;
        this.status = StatusEntrega.AGUARDANDO_RETIRADA;
        this.horarioRetirada = "";
        this.horarioConclusao = "";
    }

    public Entrega(int id, int idPedido, int idMotoqueiro, int idRegiao, StatusEntrega status, String horarioRetirada, String horarioConclusao) {
        this.id = id;
        this.idPedido = idPedido;
        this.idMotoqueiro = idMotoqueiro;
        this.idRegiao = idRegiao;
        this.status = status;
        this.horarioRetirada = horarioRetirada;
        this.horarioConclusao = horarioConclusao;
        if (id >= proximoId) proximoId = id + 1;
    }

    public void registrarRetirada(String horario){
        this.horarioRetirada = horario; this.status = StatusEntrega.EM_ROTA; 
    }
    public void concluir(String horario){ 
        this.horarioConclusao = horario; this.status = StatusEntrega.CONCLUIDA; 
    }

    public int getId(){ 
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }
    public int getIdPedido(){return idPedido; 
    }
    public void setIdPedido(int idPedido){
        this.idPedido = idPedido; 
    }
    public int getIdMotoqueiro(){
        return idMotoqueiro; 
    }
    public void setIdMotoqueiro(int idMotoqueiro){
        this.idMotoqueiro = idMotoqueiro; 
    }
    public int getIdRegiao() {return idRegiao; 
    }
    public void setIdRegiao(int idRegiao){this.idRegiao = idRegiao; 
    }
    public StatusEntrega getStatus(){ 
        return status; 
    }
    public void setStatus(StatusEntrega status){
        this.status = status; 
    }
    public String getHorarioRetirada(){ 
        return horarioRetirada; 
    }
    public void setHorarioRetirada(String horarioRetirada){
        this.horarioRetirada = horarioRetirada; 
    }
    public String getHorarioConclusao(){ 
        return horarioConclusao; 
    }
    public void setHorarioConclusao(String horarioConclusao){
        this.horarioConclusao = horarioConclusao;
    }

    @Override
    public String toString() {
        return "Entrega{id=" + id + ", pedidoId=" + idPedido + ", motoqueiro=" + idMotoqueiro
                + ", regiao=" + idRegiao + ", status=" + status
                + (horarioRetirada.isEmpty() ? "" : ", retirada='" + horarioRetirada + "'")
                + (horarioConclusao.isEmpty() ? "" : ", conclusao='" + horarioConclusao + "'") + "}";
    }
}
