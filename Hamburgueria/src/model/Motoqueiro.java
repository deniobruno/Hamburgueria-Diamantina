package model;
import java.util.List;


/**
 * Representa um motoqueiro responsável pelas entregas.
 * <p>
 * O sistema deixa como um mínimo de 5 motoqueiros ativos, controlado por
 * {@link sistema.GerenciadorEntregas#MINIMO_MOTOQUEIROS}. Cada motoqueiro
 * possui uma região de preferência e uma disponibilidade gerenciada
 * pelo {@link sistema.GerenciadorEntregas}.
 * </p>
 *
 */
public class Motoqueiro {

    private static int proximoId = 1;

    private int id;
    private String nome;
    private String telefone;
    private int idRegiao;
    private boolean disponivel;
    
    /**
     * Cria um novo motoqueiro com ID gerado automaticamente e disponibilidade
     * inicial definida como {@code true}.
     *
     * @param nome nome completo do motoqueiro
     * @param telefone telefone 
     * @param idRegiao identificador da região de preferência para entregas
     */

    public Motoqueiro(String nome, String telefone, int idRegiao) {
        this.id = proximoId++;
        this.nome = nome;
        this.telefone = telefone;
        this.idRegiao = idRegiao;
        this.disponivel = true;
    }
    /**
     * Reconstrói um motoqueiro existente a partir de dados persistidos.
     *
     * @param id identificador único já existente
     * @param nome nome completo do motoqueiro
     * @param telefone telefone de contato
     * @param idRegiao identificador da região de preferência
     * @param disponivel indica se o motoqueiro está disponível para uma nova entrega
     */

    public Motoqueiro(int id, String nome, String telefone, int idRegiao, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.idRegiao = idRegiao;
        this.disponivel = disponivel;
        if (id >= proximoId) proximoId = id + 1;
    }
/**
* Simula o envio de uma notificação via WhatsApp do motoqueiro informando
* que a entrega do pedido foi concluída com sucesso.
*
* @param idPedido identificador do pedido entregue
*/
    public void notificarEntregaConcluida(int idPedido) {
        System.out.println("[WhatsApp] " + nome + " informou: Pedido #" + idPedido + " entregue!");
    }

    public int getId(){ 
        return id; 
    }
    /**
     * Reajusta {@code proximoId} a partir do maior id carregado (evita colisão pós-load).
     * @param lista lista de motoqueiros carregada do JSON
     */
    public static void ajustarProximoId(List<Motoqueiro> lista){
        int max = 0;
        for (Motoqueiro m : lista) if (m.getId() > max) max = m.getId();
        proximoId = max + 1;
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
    public String getTelefone(){ 
        return telefone; 
    }
    public void setTelefone(String telefone){
        this.telefone = telefone; 
    }
    public int getIdRegiao(){ 
        return idRegiao; 
    }
    public void setIdRegiao(int idRegiao){
        this.idRegiao = idRegiao; 
    }
    public boolean isDisponivel(){
        return disponivel;
    }
    public void setDisponivel(boolean disponivel){ 
        this.disponivel = disponivel; 
    }

    @Override
    public String toString() {
        return "Motoqueiro{id=" + id + ", nome='" + nome + "', telefone='" + telefone
                + "', regiaoId=" + idRegiao + ", disponivel=" + disponivel + "}";
    }
}
