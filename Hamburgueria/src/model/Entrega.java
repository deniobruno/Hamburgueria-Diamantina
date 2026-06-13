package model;
import java.util.List;
 
/**

 * Representa o processo de entrega de um pedido da hamburgueria.
 * <p>
 * Associa um pedido a um motoqueiro e a uma região de entrega,
 * rastreando o status e os horários de retirada e conclusão.
 * </p>
 */
public class Entrega {
 /**
 * Enumeração dos possíveis estados de uma entrega.
 */
    public enum StatusEntrega { AGUARDANDO_RETIRADA, EM_ROTA, CONCLUIDA, FALHA }

    private static int proximoId = 1;

    private int id;
    private int idPedido;
    private int idMotoqueiro;
    private int idRegiao;
    private StatusEntrega status;
    private String horarioRetirada;
    private String horarioConclusao;
    
 /**
 * Cria uma nova entrega com ID gerado automaticamente, associando
 * o pedido ao motoqueiro e à região. O status inicial é
 * {@link StatusEntrega#AGUARDANDO_RETIRADA}.
 *
 * @param idPedido identificador do pedido a ser entregue
 * @param idMotoqueiro identificador do motoqueiro responsável
 * @param idRegiao identificador da região de entrega
 */

    public Entrega(int idPedido, int idMotoqueiro, int idRegiao) {
        this.id = proximoId++;
        this.idPedido = idPedido;
        this.idMotoqueiro = idMotoqueiro;
        this.idRegiao = idRegiao;
        this.status = StatusEntrega.AGUARDANDO_RETIRADA;
        this.horarioRetirada = "";
        this.horarioConclusao = "";
    }
  /**
  * Reconstrói uma entrega existente a partir de dados persistidos (ex.: deserialização JSON).
  *
  * @param id identificador único já existente
  * @param idPedido identificador do pedido
  * @param idMotoqueiro identificador do motoqueiro
  * @param idRegiao identificador da região
  * @param status status atual da entrega
  * @param horarioRetirada horário em que o motoqueiro retirou o pedido
  * @param horarioConclusao horário em que a entrega foi concluída
  */

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
 /**
 * Registra a retirada do pedido pelo motoqueiro, atualizando o horário
 * e alterando o status para {@link StatusEntrega#EM_ROTA}.
 *
 * @param horario horário de retirada no formato {@code HH:mm}
 */
    public void registrarRetirada(String horario){
        this.horarioRetirada = horario; this.status = StatusEntrega.EM_ROTA; 
    }
 /**
 * Conclui a entrega, registrando o horário de conclusão e alterando
 * o status para {@link StatusEntrega#CONCLUIDA}.
 *
 * @param horario horário de conclusão da entrega no formato {@code HH:mm}
 */
    public void concluir(String horario){ 
        this.horarioConclusao = horario; this.status = StatusEntrega.CONCLUIDA; 
    }

    public int getId(){ 
        return id; 
    }
    /**
     * Reajusta {@code proximoId} a partir do maior id carregado (evita colisão).
     * @param lista lista de entregas carregada do JSON
     */
    public static void ajustarProximoId(List<Entrega> lista){
        int max = 0;
        for (Entrega e : lista) if (e.getId() > max) max = e.getId();
        proximoId = max + 1;
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
