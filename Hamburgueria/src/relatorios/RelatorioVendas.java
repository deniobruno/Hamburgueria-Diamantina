package relatorios;

import model.Pedido;
import model.Venda;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitária responsável pela geração de relatórios de vendas e
 * balanços financeiros da hamburgueria.
 * <p>
 * Todos os métodos são estáticos, a classe funciona como namespace de
 * funções, não sendo necessário instanciar. Utiliza a
 * Stream API do Java para filtragens e agregações sobre as listas de
 * pedidos e vendas fornecidas por parâmetro.
 * </p>
 *
 */
public class RelatorioVendas {
    /**
     * Emite um relatório diário de vendas, listando todos os pedidos
     * vendidos na data informada e o total arrecadado.
     *
     * @param vendas lista de vendas do sistema
     * @param pedidos lista de pedidos do sistema
     * @param data data a filtrar no formato {@code Dia/Mes/Ano}
     */
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
    /**
     * Emite um relatório mensal de vendas, exibindo o total de vendas
     * e o valor arrecadado no mês informado.
     *
     * @param vendas lista de vendas do sistema
     * @param pedidos lista de pedidos do sistema
     * @param mesAno mês e ano a filtrar no formato {@code Mes/Ano}
     * comparado com a substring final da data de cada venda)
     */
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
    /**
     * Gera um balanço mensal consolidado com total de pedidos, entregas,
     * cancelamentos, valor bruto arrecadado, taxas de cancelamento e ticket médio.
     *
     * @param pedidos lista de pedidos do sistema
     * @param mesAno mês e ano a filtrar no formato {@code Mes/Ano}
     */
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
     /**
     * Pesquisa pedidos dentro de um intervalo de datas e, opcionalmente,
     * dentro de um intervalo de horários. A comparação de datas e horários
     * é feitasobre as strings nos formatos {@code Dia/Mes/Ano} e {@code HH:mm}.
     *
     * @param pedidos lista de pedidos do sistema
     * @param dataInicio data inicial do intervalo (formato {@code Dia/Mes/Ano})
     * @param dataFim data final do intervalo (formato {@code Dia/Mes/Ano})
     * @param horaInicio horário inicial do intervalo (formato {@code HH:mm}),
     * ou {@code null} para ignorar filtro de horário
     * @param horaFim horário final do intervalo (formato {@code HH:mm}),
     * ou {@code null} para ignorar filtro de horário
     * @return lista de pedidos que se enquadram no intervalo informado
     */
    public static List<Pedido> pesquisarPorIntervalo(List<Pedido> pedidos,
            String dataInicio, String dataFim, String horaInicio, String horaFim) {
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        java.time.LocalDate ini = java.time.LocalDate.parse(dataInicio, fmt);
        java.time.LocalDate fim = java.time.LocalDate.parse(dataFim, fmt);
        return pedidos.stream().filter(p -> {
            java.time.LocalDate d;
            try { d = java.time.LocalDate.parse(p.getData(), fmt); }
            catch (Exception e) { return false; } // data fora do formato esperado é ignorada
            boolean ok = !d.isBefore(ini) && !d.isAfter(fim);   // intervalo cronológico, inclusivo
            if (!ok || horaInicio == null || horaFim == null) return ok;
            String h = p.getHorarioPedido();                    // HH:mm
            return h.compareTo(horaInicio) >= 0 && h.compareTo(horaFim) <= 0;
        }).collect(Collectors.toList());
    }
    
    /**
     * Busca um pedido pelo seu identificador dentro da lista fornecida.
     *
     * @param pedidos lista de pedidos a pesquisar
     * @param id identificador do pedido desejado
     * @return o {@link Pedido} encontrado, ou {@code null} se não existir
     */
    private static Pedido buscarPedido(List<Pedido> pedidos, int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}
