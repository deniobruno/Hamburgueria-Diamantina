package sistema;

import model.Entrega;
import model.Motoqueiro;
import model.Regiao;
import java.util.List;

/**
 * Classe responsável pelas regras de negócio relacionadas às entregas.
 * <p>
 * Não armazena listas internamente,recebendo-as por parâmetro em cada operação. 
 * Favorecendo o baixo acoplamento. Impõe a restrição de no mínimo
 * {@link #MINIMO_MOTOQUEIROS} motoqueiros ativos no sistema e realiza
 * a busca de motoqueiro disponível priorizando a região solicitada.
 * </p>
 *
 */
public class GerenciadorEntregas {
    /**
     * Quantidade mínima de motoqueiros ativos permitida no sistema.
     * A remoção de um motoqueiro é bloqueada se violar esse requisito.
     */
    public static final int MINIMO_MOTOQUEIROS = 5;
    /**
     * Cria uma nova instância do gerenciador de entregas.
     */
    public GerenciadorEntregas() {
        /** Construtor vazio, pois não se instancia listas */
    }
    /**
     * Adiciona um motoqueiro à lista fornecida.
     *
     * @param m motoqueiro a ser adicionado
     * @param motoqueiros lista de motoqueiros do sistema
     */
    public void adicionarMotoqueiro(Motoqueiro m, List<Motoqueiro> motoqueiros){ 
        motoqueiros.add(m); 
    }
    /**
     * Remove um motoqueiro da lista, desde que o mínimo de
     * {@link #MINIMO_MOTOQUEIROS} motoqueiros ativos seja mantido.
     *
     * @param id identificador do motoqueiro a remover
     * @param motoqueiros lista de motoqueiros do sistema
     * @return {@code true} se removido com sucesso;
     * {@code false} se a remoção violaria o mínimo exigido
     */
    public boolean removerMotoqueiro(int id, List<Motoqueiro> motoqueiros) {
        long ativos = motoqueiros.stream().filter(Motoqueiro::isDisponivel).count();
        if (ativos <= MINIMO_MOTOQUEIROS) {
            System.out.println("[Aviso] Minimo de " + MINIMO_MOTOQUEIROS + " motoqueiros ativos.");
            return false;
        }
        return motoqueiros.removeIf(m -> m.getId() == id);
    }
    /**
     * Busca um motoqueiro disponível para realizar uma entrega.
     * Prioriza motoqueiros disponíveis na região solicitada; caso não
     * haja, retorna qualquer motoqueiro disponível.
     *
     * @param idRegiao identificador da região de destino da entrega
     * @param motoqueiros lista de motoqueiros do sistema
     * @return motoqueiro disponível encontrado, ou {@code null} se nenhum estiver livre
     */
    public Motoqueiro encontrarMotoqueiro(int idRegiao, List<Motoqueiro> motoqueiros) {
        for (Motoqueiro m : motoqueiros) {
            if (m.isDisponivel() && m.getIdRegiao() == idRegiao) return m;
        }
        for (Motoqueiro m : motoqueiros) {
            if (m.isDisponivel()) return m;
        }
        return null;
    }
    /**
     * Adiciona uma região à lista fornecida.
     *
     * @param r região a ser adicionada
     * @param regioes lista de regiões do sistema
     */
    public void adicionarRegiao(Regiao r, List<Regiao> regioes) { 
        regioes.add(r); 
    }
    /**
     * Busca uma região pelo seu identificador.
     *
     * @param id identificador da região
     * @param regioes lista de regiões do sistema
     * @return a {@link Regiao} encontrada, ou {@code null} se não existir
     */
    public Regiao buscarRegiao(int id, List<Regiao> regioes) {
        return regioes.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

   /**
     * Cria e registra uma nova entrega para o pedido informado.
     * <p>
     * Localiza um motoqueiro disponível (priorizando a região), marca ele
     * como indisponível, instancia o objeto {@link Entrega}, adiciona ele
     * na lista e exibe a confirmação no console.
     * </p>
     *
     * @param idPedido identificador do pedido a ser entregue
     * @param idRegiao identificador da região de destino
     * @param motoqueiros lista de motoqueiros do sistema
     * @param entregas lista de entregas do sistema
     * @param regioes lista de regiões do sistema
     * @return a {@link Entrega} criada, ou {@code null} se não houver motoqueiro disponível
     */
    public Entrega criarEntrega(int idPedido, int idRegiao, List<Motoqueiro> motoqueiros, List<Entrega> entregas, List<Regiao> regioes) {
        Motoqueiro moto = encontrarMotoqueiro(idRegiao, motoqueiros);
        
        if (moto == null) { 
            System.out.println("[Erro] Nenhum motoqueiro disponivel."); 
            return null; 
        }
        
        moto.setDisponivel(false);
        Entrega e = new Entrega(idPedido, moto.getId(), idRegiao);
        entregas.add(e);
        
        Regiao r = buscarRegiao(idRegiao, regioes);
        System.out.println("[Entrega] Pedido #" + idPedido + " → " + moto.getNome()
                + " (regiao: " + (r != null ? r.getNome() : idRegiao) + ")");
        return e;
    }

   /**
     * Conclui uma entrega existente, libera o motoqueiro responsável
     * e manda a notificação de conclusão via {@link Motoqueiro#notificarEntregaConcluida(int)}.
     *
     * @param idEntrega identificador da entrega a ser concluída
     * @param horario horário de conclusão no formato {@code HH:mm}
     * @param entregas lista de entregas do sistema
     * @param motoqueiros lista de motoqueiros do sistema
     */
    public void concluirEntrega(int idEntrega, String horario, List<Entrega> entregas, List<Motoqueiro> motoqueiros) {
        for (Entrega e : entregas) {
            if (e.getId() == idEntrega) {
                e.concluir(horario);
                for (Motoqueiro m : motoqueiros) {
                    if (m.getId() == e.getIdMotoqueiro()) {
                        m.setDisponivel(true);
                        m.notificarEntregaConcluida(e.getIdPedido());
                        break;
                    }
                }
                return;
            }
        }
        System.out.println("[Erro] Entrega #" + idEntrega + " nao encontrada.");
    }
}