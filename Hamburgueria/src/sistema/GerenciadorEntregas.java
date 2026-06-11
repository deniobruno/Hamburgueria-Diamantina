package sistema;

import model.Entrega;
import model.Motoqueiro;
import model.Regiao;
import java.util.List;

/**
 * Classe que processa regras de negócio das entregas.
 * Não armazena estado (listas), apenas executa ações recebendo os dados por parâmetro.
 */
public class GerenciadorEntregas {

    public static final int MINIMO_MOTOQUEIROS = 5;

    public GerenciadorEntregas() {
        /** Construtor vazio, pois não se instancia listas */
    }

    public void adicionarMotoqueiro(Motoqueiro m, List<Motoqueiro> motoqueiros){ 
        motoqueiros.add(m); 
    }

    public boolean removerMotoqueiro(int id, List<Motoqueiro> motoqueiros) {
        long ativos = motoqueiros.stream().filter(Motoqueiro::isDisponivel).count();
        if (ativos <= MINIMO_MOTOQUEIROS) {
            System.out.println("[Aviso] Minimo de " + MINIMO_MOTOQUEIROS + " motoqueiros ativos.");
            return false;
        }
        return motoqueiros.removeIf(m -> m.getId() == id);
    }

    public Motoqueiro encontrarMotoqueiro(int idRegiao, List<Motoqueiro> motoqueiros) {
        for (Motoqueiro m : motoqueiros) {
            if (m.isDisponivel() && m.getIdRegiao() == idRegiao) return m;
        }
        for (Motoqueiro m : motoqueiros) {
            if (m.isDisponivel()) return m;
        }
        return null;
    }

    public void adicionarRegiao(Regiao r, List<Regiao> regioes) { 
        regioes.add(r); 
    }

    public Regiao buscarRegiao(int id, List<Regiao> regioes) {
        return regioes.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

   
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