package model;

/**
 * Representa um motoqueiro responsável pelas entregas.
 * O sistema mantém no mínimo 5 motoqueiros ativos.
 */
public class Motoqueiro {

    private static int proximoId = 1;

    private int id;
    private String nome;
    private String telefone;
    private int idRegiao;
    private boolean disponivel;

    public Motoqueiro(String nome, String telefone, int idRegiao) {
        this.id = proximoId++;
        this.nome = nome;
        this.telefone = telefone;
        this.idRegiao = idRegiao;
        this.disponivel = true;
    }

    public Motoqueiro(int id, String nome, String telefone, int idRegiao, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.idRegiao = idRegiao;
        this.disponivel = disponivel;
        if (id >= proximoId) proximoId = id + 1;
    }

    public void notificarEntregaConcluida(int idPedido) {
        System.out.println("[WhatsApp] " + nome + " informou: Pedido #" + idPedido + " entregue!");
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
