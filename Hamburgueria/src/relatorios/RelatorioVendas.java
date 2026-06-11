package relatorios;

import model.Pedido;
import model.Venda;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gera relatórios de vendas e pedidos (diário, mensal e balanço).
 */
public class RelatorioVendas {

    public static void emitirDia(List<Venda> vendas, List<Pedido> pedidos, String data) {
        List<Venda> filtradas = vendas.stream().filter(v -> v.getData().equals(data)).collect(Collectors.toList());
        System.out.println("\n========================================");
        System.out.println("  RELATORIO DE VENDAS — " + data);
        System.out.println("========================================");
        System.out.println("Total de vendas: " + filtradas.size());
        double total = 0;
        for (Venda v : filtradas) {
            Pedido p = buscarPedido(pedidos, v.getIdPedido());
            if (p != null) {
                total += p.getValorTotal();
                System.out.printf("  Pedido #%d | Cliente #%d | R$%.2f | %s%n",
                        p.getId(), p.getIdCliente(), p.getValorTotal(), p.getStatus());
            }
        }
        System.out.printf("TOTAL ARRECADADO: R$%.2f%n", total);
        System.out.println("========================================\n");
    }

    public static void emitirMes(List<Venda> vendas, List<Pedido> pedidos, String mesAno) {
        List<Venda> filtradas = vendas.stream()
                .filter(v -> v.getData().length() >= 7 && v.getData().substring(3).equals(mesAno))
                .collect(Collectors.toList());
        System.out.println("\n========================================");
        System.out.println("  RELATORIO MENSAL — " + mesAno);
        System.out.println("========================================");
        System.out.println("Total de vendas: " + filtradas.size());
        double total = filtradas.stream()
                .mapToDouble(v -> { Pedido p = buscarPedido(pedidos, v.getIdPedido()); return p != null ? p.getValorTotal() : 0; })
                .sum();
        System.out.printf("TOTAL ARRECADADO: R$%.2f%n", total);
        System.out.println("========================================\n");
    }

    public static void gerarBalanco(List<Pedido> pedidos, String mesAno) {
        List<Pedido> filtrados = pedidos.stream()
                .filter(p -> p.getData().length() >= 7 && p.getData().substring(3).equals(mesAno))
                .collect(Collectors.toList());
        long entregues = filtrados.stream().filter(p -> p.getStatus() == Pedido.Status.ENTREGUE).count();
        long cancelados = filtrados.stream().filter(p -> p.getStatus() == Pedido.Status.CANCELADO).count();
        double totalBruto = filtrados.stream().mapToDouble(Pedido::getValorTotal).sum();
        double taxas = filtrados.stream().mapToDouble(Pedido::getValorCancelamento).sum();
        double ticket = filtrados.isEmpty() ? 0 : totalBruto / filtrados.size();
        System.out.println("\n========================================");
        System.out.println("  BALANCO MENSAL — " + mesAno);
        System.out.println("========================================");
        System.out.println("Total de pedidos:      " + filtrados.size());
        System.out.println("Pedidos entregues:     " + entregues);
        System.out.println("Pedidos cancelados:    " + cancelados);
        System.out.printf("Total bruto:           R$%.2f%n", totalBruto);
        System.out.printf("Taxas de cancelamento: R$%.2f%n", taxas);
        System.out.printf("Ticket medio:          R$%.2f%n", ticket);
        System.out.println("========================================\n");
    }

    public static List<Pedido> pesquisarPorIntervalo(List<Pedido> pedidos,
            String dataInicio, String dataFim, String horaInicio, String horaFim) {
        return pedidos.stream().filter(p -> {
            String d = p.getData();
            boolean ok = d.compareTo(dataInicio) >= 0 && d.compareTo(dataFim) <= 0;
            if (!ok || horaInicio == null || horaFim == null) return ok;
            String h = p.getHorarioPedido();
            return h.compareTo(horaInicio) >= 0 && h.compareTo(horaFim) <= 0;
        }).collect(Collectors.toList());
    }

    private static Pedido buscarPedido(List<Pedido> pedidos, int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}
