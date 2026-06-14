import model.*;
import sistema.Sistema;
import ui.Menu;
import factorymethod.*;
import java.util.*;


/**
 * Ponto de entrada do sistema.
 * <p>
 * Em modo normal abre o menu interativo. Em modo de teste
 * ({@code java Main teste}) executa o harness das Alternativas 18 a 21
 * (Iterator x for-each, Comparator + Collections.sort, find x binarySearch
 * e o cenário completo de 10 clientes) e encerra.
 * </p>
 * 
 * Obtém a instância única de {@link Sistema} (padrão Singleton), carrega
 * os dados persistidos em JSON e inicia o loop principal de login/logout.
 * Após cada sessão encerrada, persiste os dados e retorna à tela de login.
 * O sistema é encerrado quando o usuário opta por sair na tela de login.
 * </p>
 *
 */

/**
 * Subclasse auxiliar utilizada exclusivamente para demonstrar o acesso
 * direto ao contador estático protegido {@code totalProdutosProtected}
 * da superclasse {@link Produto} (Estratégia B, Alternativa 11(B)).
 */
class ProdutoDemo extends Produto {
    /**
     * Cria uma instância demo de Produto com valores neutros.
     */
    public ProdutoDemo(){
        super(0, "demo", 0); 
    }
    /**
     * Retorna o valor do contador estático protegido herdado de {@link Produto},
     * demonstrando o acesso direto sem necessidade de getter (Estratégia B).
     *
     * @return valor atual de {@code totalProdutosProtected}
     */
    public static int getContadorProtected(){
        return totalProdutosProtected; 
    }
}
    /**
     * Método principal da aplicação.
     * <p>
     * Inicializa o sistema, carrega os dados e realiza o ciclo de
     * autenticação: redireciona para o menu de administrador ou colaborador
     * de acordo com o tipo de usuário autenticado, salvando os dados ao fim de
     * cada sessão.
     * </p>
     *
     */
public class Main {

    public static void main(String[] args) {
        
    /** Alternativa 11 (Estratégia B): subclasse acessando o contador protected diretamente */
        System.out.println("[Demo] Contador protected (Estrategia B): " + ProdutoDemo.getContadorProtected());

        Sistema s = Sistema.getInstancia();
        s.carregar();
        
        /** Modo de teste automatizado: java Main teste */
        if (args.length > 0 && args[0].equalsIgnoreCase("teste")) {
            executarTestes(s);
            s.salvarTudo();
            System.out.println("\n[Testes concluidos] Dados salvos.");
            return;
        }

        /** Menu interativo, volta ao login após cada logout */
        Menu menu = new Menu(s);
        while (true) {
            Usuario usuario = menu.telaLogin();
            if (usuario == null) break;
            if (usuario instanceof Administrador) menu.menuAdministrador((Administrador) usuario);
            else if (usuario instanceof Colaborador) menu.menuColaborador((Colaborador) usuario);
            s.salvarTudo();
            System.out.println("Sessao encerrada. Retornando ao login...");
        }

        s.salvarTudo();
        System.out.println("Sistema encerrado. Ate logo!");
    }
   
    /** TESTES — Alternativas 18 a 21 */
    

    /** Orquestra todos os testes pedidos no enunciado (18 a 21). */
    private static void executarTestes(Sistema s) {
        System.out.println("\n TESTES (Alternativas 18 a 21)  ");
        cenario10Clientes(s);     // item 21 (popula os dados usados pelos demais)
        demoIteratorForeach(s);   // item 18
        demoComparatorSort(s);    // item 19
        demoFindBinarySearch(s);  // item 20
    }

    /**
     * ALTERNATIVA 18 — Iterator (while hasNext/next) e for-each.
     * Demonstra que o laço for-each é uma forma simplificada de utilizar o Iterator.
     */
    private static void demoIteratorForeach(Sistema s) {
        System.out.println("\n Alternativa 18: Iterator (while hasNext/next) x for-each");
        List<Cliente> clientes = new ArrayList<>(s.getClientes());

        System.out.println("(a) Percorrendo com Iterator");
        Iterator<Cliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            Cliente c = iterator.next();
            System.out.println(c);
        }

        System.out.println("(b) Percorrendo com for-each");
        for (Cliente c : clientes) {
            System.out.println(c);
        }

        System.out.println("Explicacao: 'for (Cliente c : lista)' e' compilado para um laco que usa "
                + "lista.iterator(), chamando hasNext() e next(), exatamente como em (a). "
                + "Funciona para qualquer objeto que implemente Iterable.");
    }

    /**
     * ALTERNATIVA 19 — Collections.sort com o Comparator implementado,
     * executado duas vezes com atributos diferentes (nome e id).
     */
    private static void demoComparatorSort(Sistema s) {
        System.out.println("\nAlternativa 19: Collections.sort com Comparator (2x)");
        List<Cliente> lista = new ArrayList<>(s.getClientes());
        if (lista.isEmpty()){
            System.out.println("(sem clientes para ordenar)"); 
        return;
        }

        /* Cliente implementa Comparator<Cliente>; usamos uma instância existente como
           "função de comparação". compare() ordena por NOME; compararPorId() por ID. */
        Cliente comparador = lista.get(0);

        System.out.println("1a ordenacao: por NOME (metodo compare)");
        Collections.sort(lista, comparador);
        lista.forEach(System.out::println);

        System.out.println("2a ordenacao: por ID (metodo compararPorId)");
        Collections.sort(lista, (a, b) -> comparador.compararPorId(a, b));
        lista.forEach(System.out::println);

        /* O mesmo para Pedido (por valor), mostrando o Comparator de Pedido. */
        List<Pedido> pedidos = new ArrayList<>(s.getPedidos());
        if (!pedidos.isEmpty()) {
            Pedido cmpPedido = pedidos.get(0);
            System.out.println("Pedidos por valor (compararPorValor)");
            Collections.sort(pedidos, (a, b) -> cmpPedido.compararPorValor(a, b));
            pedidos.forEach(System.out::println);
        }
    }

    /**
     * ALTERNATIVA 20 — método find próprio (Iterator + Comparator) comparado
     * com Collections.binarySearch.
     */
    private static void demoFindBinarySearch(Sistema s) {
        System.out.println("\nAlternativa 20: find (iterator+comparator) x binarySearch");
        List<Cliente> lista = new ArrayList<>(s.getClientes());
        if (lista.isEmpty()){
            System.out.println("(sem clientes para buscar)"); 
        return; 
        }

        Comparator<Cliente> porId = (a, b) -> Integer.compare(a.getId(), b.getId());
        Cliente alvo = lista.get(lista.size() - 1); // escolhe um alvo existente

        // find implementado: percorre com Iterator usando o Comparator (O(n), sem exigir ordenação)
        Cliente achadoFind = findCliente(lista, porId, alvo);
        System.out.println("find() encontrou: " + achadoFind);

        // binarySearch: exige a lista previamente ordenada pelo mesmo Comparator (O(log n))
        Collections.sort(lista, porId);
        int idx = Collections.binarySearch(lista, alvo, porId);
        System.out.println("binarySearch() retornou indice " + idx + " -> "
                + (idx >= 0 ? lista.get(idx) : "nao encontrado"));

        System.out.println("Comparacao: o find percorre 1 a 1 (O(n)) e nao exige ordenacao; "
                + "o binarySearch e' O(log n), porem so funciona com a lista JA ordenada pelo mesmo comparator.");
    }

    /**
     * Busca linear com Iterator e Comparator: retorna o primeiro cliente
     * considerado igual ao alvo pelo comparator (compare == 0).
     */
    private static Cliente findCliente(List<Cliente> lista, Comparator<Cliente> cmp, Cliente alvo) {
        Iterator<Cliente> it = lista.iterator();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (cmp.compare(c, alvo) == 0) return c;
        }
        return null;
    }

    /**
     * ALTERNATIVA 21 — cenário completo: cadastra 10 clientes e processa pedidos
     * variados (com baixa de estoque, entrega, cancelamento e emissão de recibo).
     */
    private static void cenario10Clientes(Sistema s) {
        System.out.println("\nAlternativa 21: Cenario completo com 10 clientes");

        /* Garante cardápio (via Factory Method) e estoque para o cenário. */
        if (s.getProdutos().isEmpty()) {
            ProdutoFactory fHamburguer = new HamburguerFactory();
            ProdutoFactory fBebida = new BebidaFactory();
            Produto xbacon = fHamburguer.criar(1, "X-Bacon", 25.0);
            xbacon.vincularIngrediente(1); // pão
            xbacon.vincularIngrediente(2); // bacon
            Produto coca = fBebida.criar(2, "Coca-Cola", 7.0);
            coca.vincularIngrediente(3);   // refrigerante
            s.incluirProduto(xbacon);
            s.incluirProduto(coca);
        }
        if (s.getIngredientes().isEmpty()) {
            s.receberIngrediente(new Ingrediente(1, "Pao",          100, "un", 10));
            s.receberIngrediente(new Ingrediente(2, "Bacon",         50, "un",  5));
            s.receberIngrediente(new Ingrediente(3, "Refrigerante",  80, "un",  8));
        }

        int idColaborador = s.getColaboradores().isEmpty() ? 1 : s.getColaboradores().get(0).getId();
        int idRegiao      = s.getRegioes().isEmpty()       ? 1 : s.getRegioes().get(0).getId();

        for (int i = 1; i <= 10; i++) {
            Cliente cliente = new Cliente("Cliente " + i, "389911-2200" + String.format("%02d", i), "Rua " + i);
            s.incluirCliente(cliente);

            List<Integer> produtos = new ArrayList<>();
            produtos.add(1);                 // todos pedem o hambúrguer
            if (i % 2 == 0) produtos.add(2); // pares também pedem bebida
            List<Integer> adicionais = new ArrayList<>();

            System.out.println("\nAtendimento " + i + " (" + cliente.getNome() + ")");
            Pedido pedido = s.realizarPedido(cliente.getId(), produtos, adicionais,
                    "20:" + String.format("%02d", i), idColaborador, idRegiao);
            if (pedido == null) continue;

            if (i == 3) {
                // Variação: desistência -> cancelamento (retém 35%)
                System.out.println(">> Cliente 3 desistiu: cancelando o pedido...");
                s.cancelarPedido(pedido.getId());
            } else if (i % 2 == 0) {
                // Variação: conclui a entrega (motoqueiro avisa via WhatsApp)
                final int idPed = pedido.getId();
                Entrega entrega = s.getEntregas().stream()
                         .filter(e -> e.getIdPedido() == idPed)
                        .reduce((a, b) -> b).orElse(null); // pega a entrega mais recente desse pedido
                if (entrega != null) {
                    s.getGerenciadorEntregas().concluirEntrega(entrega.getId(), "21:00",
                            s.getEntregas(), s.getMotoqueiros());
                }
            }
        }

        System.out.println("\nResumo do cenario");
        System.out.println("Total de pedidos criados (metodo de classe, alternativa 12): " + Sistema.getTotalPedidosCriados());
        System.out.println("Total de produtos criados (alternativa 11): " + Produto.getTotalProdutosPrivate());
        EstacaoPreparo.imprimirStatus();
        s.getFilaPedidos().imprimirFila();
        s.verificarAlertas();
    }
}
