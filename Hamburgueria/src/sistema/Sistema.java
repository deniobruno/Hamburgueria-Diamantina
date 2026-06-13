package sistema;

import model.Adicional;
import model.Ingrediente;
import com.google.gson.reflect.TypeToken;
import model.*;
import persistencia.PersistenciaJson;
import factorymethod.ProdutoFactory;
import factorymethod.HamburguerFactory;
import factorymethod.BebidaFactory;
import factorymethod.SobremesaFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe central do sistema de gerenciamento.
 * <p>
 * Implementa o padrão de projeto <b>Singleton</b>, garantindo que apenas
 * uma instância exista durante toda a execução da aplicação. Atua como
 * fachada (Facade), coordenando todas as entidades do domínio: clientes,
 * colaboradores, produtos, pedidos, extratos, vendas, ingredientes,
 * motoqueiros, regiões e entregas.
 * </p>
 * <p>
 * Delega a persistência a {@link PersistenciaJson} e as
 * regras de entrega a {@link GerenciadorEntregas}. Pedidos que não
 * encontram estação de preparo livre são enfileirados em {@link FilaPedidos}.
 * </p>
 *
 */
public class Sistema {

    private static final String DIR = "data/";
    private static final String F_MOTOQUEIROS = DIR + "motoqueiros.json";
    private static final String F_REGIOES = DIR + "regioes.json";
    private static final String F_CLIENTES = DIR + "clientes.json";
    private static final String F_PEDIDOS = DIR + "pedidos.json";
    private static final String F_PRODUTOS = DIR + "produtos.json";
    private static final String F_INGREDIENTES = DIR + "ingredientes.json";
    private static final String F_COLABORADORES = DIR + "colaboradores.json";
    private static final String F_ADMIN = DIR + "administrador.json";
    private static final String F_EXTRATOS = DIR + "extratos.json";
    private static final String F_VENDAS = DIR + "vendas.json";
    private static final String F_ADICIONAIS = DIR + "adicionais.json";
    private static final String F_ENTREGAS = DIR + "entregas.json";

    private List<Cliente> clientes;
    private List<Pedido> pedidos;
    private List<Produto> produtos;
    private List<Ingrediente> ingredientes;
    private List<Colaborador> colaboradores;
    private List<Extrato> extratos;
    private List<Venda> vendas;
    private List<Adicional> adicionais;
    private Administrador administrador;
    private List<Motoqueiro> motoqueiros;
    private List<Regiao> regioes;
    private List<Entrega> entregas;

    private GerenciadorEntregas gerenciadorEntregas;
    private FilaPedidos filaPedidos;

    private static Sistema instancia;
    /**
     * Construtor privado, impede a criação direta de instâncias fora desta classe,
     * garantindo o padrão Singleton. Inicializa todas as listas e os componentes
     * auxiliares {@link GerenciadorEntregas} e {@link FilaPedidos}.
     */

    private Sistema() {
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();
        produtos = new ArrayList<>();
        ingredientes = new ArrayList<>();
        colaboradores = new ArrayList<>();
        extratos = new ArrayList<>();
        vendas = new ArrayList<>();
        adicionais = new ArrayList<>();
        motoqueiros = new ArrayList<>();
        regioes = new ArrayList<>();
        entregas = new ArrayList<>();
        
        gerenciadorEntregas = new GerenciadorEntregas();
        filaPedidos = new FilaPedidos();
    }
    /**
     * Retorna a única instância do sistema (padrão Singleton).
     * Cria a instância na primeira chamada e a reutiliza nas seguintes.
     *
     * @return instância única de {@code Sistema}
     */
    public static Sistema getInstancia() {
        if (instancia == null) instancia = new Sistema();
        return instancia;
    }

     /**
     * Retorna o total de pedidos criados desde o início da execução.
     * Delega a chamada a {@link Pedido#getTotalPedidosCriados()}.
     *
     * @return número total de instâncias de {@link Pedido} criadas
     */
    public static int getTotalPedidosCriados(){ 
        return Pedido.getTotalPedidosCriados(); 
    }

     /**
     * Carrega todos os dados persistidos dos arquivos JSON para as listas em memória.
     * Se o arquivo do administrador não existir, cria um administrador padrão
     * ({@code admin / admin123}) e o persiste imediatamente.
     */
    public void carregar() {
        PersistenciaJson.garantirDiretorio(DIR);
        clientes = PersistenciaJson.carregarLista(F_CLIENTES,new TypeToken<List<Cliente>>(){}.getType());
        pedidos = PersistenciaJson.carregarLista(F_PEDIDOS,new TypeToken<List<Pedido>>(){}.getType());
        /* Reconstrói os subtipos de Produto (Hamburguer/Bebidas/Sobremesa) preservando o
           polimorfismo: o Gson releria tudo como Produto puro, então usamos o campo "tipo"
           gravado em cada produto para recriar a classe concreta via fábrica. */
        List<Produto> produtosBrutos = PersistenciaJson.carregarLista(F_PRODUTOS,new TypeToken<List<Produto>>(){}.getType());
        produtos = new ArrayList<>();
        for (Produto p : produtosBrutos) produtos.add(reconstruirProduto(p));

        ingredientes = PersistenciaJson.carregarLista(F_INGREDIENTES,new TypeToken<List<Ingrediente>>(){}.getType());
        colaboradores = PersistenciaJson.carregarLista(F_COLABORADORES,new TypeToken<List<Colaborador>>(){}.getType());
        extratos = PersistenciaJson.carregarLista(F_EXTRATOS,new TypeToken<List<Extrato>>(){}.getType());
        vendas = PersistenciaJson.carregarLista(F_VENDAS,new TypeToken<List<Venda>>(){}.getType());
        adicionais = PersistenciaJson.carregarLista(F_ADICIONAIS,new TypeToken<List<Adicional>>(){}.getType());
        administrador = PersistenciaJson.carregarObjeto(F_ADMIN,Administrador.class);
        motoqueiros = PersistenciaJson.carregarLista(F_MOTOQUEIROS,new TypeToken<List<Motoqueiro>>(){}.getType());
        regioes   = PersistenciaJson.carregarLista(F_REGIOES,new TypeToken<List<Regiao>>(){}.getType());
        entregas  = PersistenciaJson.carregarLista(F_ENTREGAS,new TypeToken<List<Entrega>>(){}.getType());
        
        if (administrador == null) {
            administrador = new Administrador("Admin", "admin", "admin123", "admin@hamburgueria.com");
            PersistenciaJson.salvarObjeto(administrador, F_ADMIN);
        }
        /* Seed mínimo para o sistema de entregas: garante o mínimo de 5 motoqueiros
           "em ação" (Alternativa 15) e algumas regiões na primeira execução. */
        if (motoqueiros.isEmpty()) {
            for (int i = 1; i <= 6; i++) motoqueiros.add(new Motoqueiro("Motoqueiro " + i, "3899000000" + i, 1));
        }
        if (regioes.isEmpty()) {
            regioes.add(new Regiao(1, "Centro"));
            regioes.add(new Regiao(2, "Palha"));
            regioes.add(new Regiao(3, "Rio Grande"));
        }

        /* Reajusta os contadores de id estáticos a partir do maior id carregado,
           evitando colisões (o Gson não executa os construtores na desserialização). */
        Cliente.ajustarProximoId(clientes);
        Pedido.ajustarProximoId(pedidos);
        Venda.ajustarProximoId(vendas);
        Extrato.ajustarProximoId(extratos);
        Motoqueiro.ajustarProximoId(motoqueiros);
        Entrega.ajustarProximoId(entregas);
        int maiorUsuario = colaboradores.stream().mapToInt(Colaborador::getId).max().orElse(0);
        if (administrador != null) maiorUsuario = Math.max(maiorUsuario, administrador.getId());
        Usuario.ajustarProximoId(maiorUsuario);

        /* Sincroniza os contadores de instância com o que foi persistido (Alternativas 11 e 12). */
        Produto.setTotalProdutos(produtos.size());
        Pedido.setTotalPedidosCriados(pedidos.size());
        System.out.println("[Sistema] Carregado. Clientes: " + clientes.size()
                + " | Pedidos: " + pedidos.size() + " | Produtos: " + produtos.size());
    }
      /**
+     * Reconstrói um {@link Produto} carregado do JSON na sua classe concreta correta
+     * (Hamburguer/Bebidas/Sobremesa), usando o discriminador {@code tipo} e a
+     * fábrica correspondente. Preserva o polimorfismo perdido na desserialização.
+     *
+     * @param base produto lido do JSON (sempre instanciado como Produto puro pelo Gson)
+     * @return instância do subtipo correto, ou o próprio Produto base se o tipo for desconhecido
+     */
    private Produto reconstruirProduto(Produto base) {
        if (base == null) return null;
        String tipo = base.getTipo() == null ? "Produto" : base.getTipo();
        ProdutoFactory fabrica;
        switch (tipo) {
            case "Hamburguer": fabrica = new HamburguerFactory(); break;
            case "Bebidas":    fabrica = new BebidaFactory();      break;
            case "Sobremesa":  fabrica = new SobremesaFactory();   break;
            default:           return base; // Produto base puro
        }
        Produto novo = fabrica.criar(base.getIdDescricao(), base.getDescricao(), base.getValor());
        if (base.getAdicionaisDisponiveis() != null) novo.setAdicionaisDisponiveis(base.getAdicionaisDisponiveis());
        if (base.getIdsIngredientes() != null) novo.setIdsIngredientes(base.getIdsIngredientes());
        return novo;
    }
    /**
     * Persiste todas as listas e o objeto administrador nos respectivos arquivos JSON,
     * garantindo que os dados em memória sejam salvos ao fim de cada sessão.
     */
    public void salvarTudo() {
        PersistenciaJson.salvarLista(clientes,F_CLIENTES);
        PersistenciaJson.salvarLista(pedidos,F_PEDIDOS);
        PersistenciaJson.salvarLista(produtos,F_PRODUTOS);
        PersistenciaJson.salvarLista(ingredientes,F_INGREDIENTES);
        PersistenciaJson.salvarLista(colaboradores,F_COLABORADORES);
        PersistenciaJson.salvarLista(extratos,F_EXTRATOS);
        PersistenciaJson.salvarLista(vendas,F_VENDAS);
        PersistenciaJson.salvarLista(adicionais,F_ADICIONAIS);
        PersistenciaJson.salvarLista(this.motoqueiros,F_MOTOQUEIROS);
        PersistenciaJson.salvarLista(this.regioes,F_REGIOES);
        PersistenciaJson.salvarLista(this.entregas,F_ENTREGAS);
        PersistenciaJson.salvarObjeto(administrador, F_ADMIN);
    }

     /**
     * Autentica um usuário verificando as credenciais contra o administrador
     * e, em seguida, contra todos os colaboradores cadastrados.
     *
     * @param login login informado
     * @param senha senha informada
     * @return o {@link Usuario} autenticado (Administrador ou Colaborador),
     * ou {@code null} se as credenciais forem inválidas
     */
    public Usuario autenticar(String login, String senha) {
        if (administrador != null && administrador.autenticar(login, senha)) return administrador;
        for (Colaborador c : colaboradores) if (c.autenticar(login, senha)) return c;
        return null;
    }

     /**
     * Inclui um novo cliente no sistema e persiste a lista atualizada.
     *
     * @param c cliente a ser incluído
     */

    public void incluirCliente(Cliente c) {
        clientes.add(c);
        PersistenciaJson.salvarLista(clientes, F_CLIENTES);
    }
    /**
     * Edita os dados de um cliente existente. Apenas os campos não nulos
     * são atualizados.
     *
     * @param id identificador do cliente a editar
     * @param nome novo nome, ou {@code null} para não alterar
     * @param telefone novo telefone, ou {@code null} para não alterar
     * @param endereco novo endereço, ou {@code null} para não alterar
     * @return {@code true} se o cliente foi encontrado e atualizado;
     * {@code false} se não existir
     */
    public boolean editarCliente(int id, String nome, String telefone, String endereco) {
        Cliente c = buscarCliente(id);
        if (c == null) return false;
        if (nome != null) c.setNome(nome);
        if (telefone != null) c.setTelefone(telefone);
        if (endereco != null) c.setEndereco(endereco);
        PersistenciaJson.salvarLista(clientes, F_CLIENTES);
        return true;
    }
    /**
     * Remove um cliente do sistema pelo seu identificador.
     *
     * @param id identificador do cliente a remover
     * @return {@code true} se removido com sucesso; {@code false} se não encontrado
     */
    public boolean removerCliente(int id) {
        boolean ok = clientes.removeIf(c -> c.getId() == id);
        if (ok) PersistenciaJson.salvarLista(clientes, F_CLIENTES);
        return ok;
    }
     /**
     * Busca um cliente pelo seu identificador.
     *
     * @param id identificador do cliente
     * @return o {@link Cliente} encontrado, ou {@code null} se não existir
     */

    public Cliente buscarCliente(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
    /**
     * Busca clientes cujo nome contenha o texto informado (busca parcial).
     *
     * @param nome texto para pesquisar no nome dos clientes
     * @return lista de clientes cujo nome contém o texto pesquisado
     */
    public List<Cliente> buscarClientePorNome(String nome) {
        String n = nome.toLowerCase();
        return clientes.stream().filter(c -> c.getNome().toLowerCase().contains(n)).collect(Collectors.toList());
    }

     /**
     * Inclui um novo colaborador no sistema e persiste a lista atualizada.
     *
     * @param c colaborador a ser incluído
     */

    public void incluirColaborador(Colaborador c) {
        colaboradores.add(c);
        PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
    }

     /**
     * Edita os dados de um colaborador existente. Apenas os campos não nulos
     * são atualizados.
     *
     * @param id identificador do colaborador a editar
     * @param nome novo nome, ou {@code null} para não alterar
     * @param cargo novo cargo, ou {@code null} para não alterar
     * @param telefone novo telefone, ou {@code null} para não alterar
     * @return {@code true} se encontrado e atualizado; {@code false} se não existir
     */
    public boolean editarColaborador(int id, String nome, String cargo, String telefone) {
        Colaborador c = buscarColaborador(id);
        if (c == null) return false;
        if (nome != null)c.setNome(nome);
        if (cargo != null) c.setCargo(cargo);
        if (telefone != null) c.setTelefone(telefone);
        PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
        return true;
    }
    /**
     * Remove um colaborador do sistema pelo seu identificador.
     *
     * @param id identificador do colaborador a remover
     * @return {@code true} se removido; {@code false} se não encontrado
     */
    public boolean removerColaborador(int id) {
        boolean ok = colaboradores.removeIf(c -> c.getId() == id);
        if (ok) PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
        return ok;
    }
    /**
     * Busca um colaborador pelo seu identificador.
     *
     * @param id identificador do colaborador
     * @return o {@link Colaborador} encontrado, ou {@code null} se não existir
     */
    public Colaborador buscarColaborador(int id){
        return colaboradores.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
    /**
     * Altera a senha de um usuário após autenticar com a senha atual.
     * Persiste todas as entidades ao final da operação.
     *
     * @param login login do usuário
     * @param senhaAtual senha atual para validação
     * @param novaSenha nova senha desejada
     * @return {@code true} se a senha foi alterada; {@code false} se a autenticação falhou
     */
    public boolean alterarSenha(String login, String senhaAtual, String novaSenha) {
        Usuario u = autenticar(login, senhaAtual);
        if (u == null) { System.out.println("[Erro] Senha atual incorreta."); return false; }
        u.setSenha(novaSenha);
        salvarTudo();
        System.out.println("[Sistema] Senha alterada com sucesso.");
        return true;
    }

     /**
     * Inclui um novo produto no cardápio e persiste a lista atualizada.
     *
     * @param p produto a ser incluído
     */
    public void incluirProduto(Produto p) {
        produtos.add(p);
        PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
    }
    /**
     * Edita os dados de um produto existente.
     *
     * @param id identificador do produto a editar
     * @param descricao nova descrição, ou {@code null} para não alterar
     * @param valor novo valor
     * @return {@code true} se encontrado e atualizado; {@code false} se não existir
     */
    public boolean editarProduto(int id, String descricao, double valor) {
        Produto p = buscarProduto(id);
        if (p == null) return false;
        if (descricao != null)p.setDescricao(descricao);
        if (valor > 0)p.setValor(valor);
        PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
        return true;
    }
    /**
     * Remove um produto do cardápio pelo seu identificador.
     *
     * @param id identificador do produto a remover
     * @return {@code true} se removido; {@code false} se não encontrado
     */
    public boolean removerProduto(int id) {
        boolean ok = produtos.removeIf(p -> p.getIdDescricao() == id);
        if (ok) PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
        return ok;
    }
    /**
     * Busca um produto pelo seu identificador.
     *
     * @param id identificador do produto
     * @return o {@link Produto} encontrado, ou {@code null} se não existir
     */
    public Produto buscarProduto(int id) {
        return produtos.stream().filter(p -> p.getIdDescricao() == id).findFirst().orElse(null);
    }
    /**
     * Realiza fluxo de um novo pedido: valida cliente e produtos,
     * calcula o valor total (produtos + adicionais), cria os objetos
     * {@link Pedido}, {@link Extrato} e {@link Venda}, aloca uma estação de
     * preparo (ou enfileira o pedido), cria a entrega e atualiza o estoque.
     *
     * @param idCliente identificador do cliente que realizou o pedido
     * @param idsProduto lista de IDs dos produtos solicitados
     * @param idsAdicionais lista de IDs dos adicionais solicitados
     * @param horarioEntrega horário previsto para a entrega no formato {@code HH:mm}
     * @param idColaborador identificador do colaborador que registrou o pedido
     * @param idRegiao identificador da região de entrega
     * @return o {@link Pedido} criado, ou {@code null} se cliente não encontrado
     * ou nenhum produto informado
     */
    public Pedido realizarPedido(int idCliente, List<Integer> idsProduto, List<Integer> idsAdicionais,
                                  String horarioEntrega, int idColaborador, int idRegiao) {
        
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null || idsProduto == null || idsProduto.isEmpty()) {
            System.out.println("[Erro] Cliente nao encontrado ou nenhum produto selecionado.");
            return null;
        }

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        double valorTotal = 0;
        StringBuilder nomesProdutos = new StringBuilder();

        /** Calcula o valor total de TODOS os produtos selecionados */
        for (int idProd : idsProduto) {
            Produto produto = buscarProduto(idProd);
            if (produto != null) {
                valorTotal += produto.getValor();
                nomesProdutos.append(produto.getDescricao()).append(" + ");
            } else {
                System.out.println("[Aviso] Produto ID " + idProd + " nao encontrado.");
            }
        }

        /** Soma o valor dos adicionais */
        for (int idAd : idsAdicionais) {
            for (Adicional ad : adicionais) {
                if (ad.getId() == idAd) { 
                    valorTotal += ad.getValor(); 
                    break; 
                }
            }
        }

        Pedido pedido = new Pedido(idCliente, idsProduto, data, hora, horarioEntrega, valorTotal);
        pedido.setIdsAdicionais(idsAdicionais);
        pedidos.add(pedido); // Dinâmico 
        cliente.registrarPedido(pedido.getId(), data);

        Extrato extrato = new Extrato(pedido.getId(), idCliente, cliente.getNome(),
                valorTotal, data, hora, nomesProdutos.toString());
        extratos.add(extrato);
        extrato.imprimir();

        vendas.add(new Venda(pedido.getId(), extrato.getId(), idColaborador, data, hora));

        EstacaoPreparo estacao = EstacaoPreparo.encontrarEstacaoLivre();
        if (estacao != null) {
            estacao.alocarPedido(pedido.getId());
            pedido.setStatus(Pedido.Status.EM_PREPARO);
            System.out.println("[Sistema] Pedido #" + pedido.getId() + " → " + estacao.getNome());
        } else {
            filaPedidos.enfileirar(pedido);
        }

        gerenciadorEntregas.criarEntrega(pedido.getId(), idRegiao, this.motoqueiros, this.entregas, this.regioes);
        
        atualizarEstoqueAposPedido(idsProduto);
        return pedido;
    }
    /**
     * Cancela um pedido existente, deixando marcado  o extrato correspondente como cancelado,
     * liberando a estação de preparo e chamando o próximo pedido da fila, se houver.
     * Persiste todos os dados ao final.
     *
     * @param idPedido identificador do pedido a ser cancelado
     * @return {@code true} se cancelado com sucesso; {@code false} se não encontrado
     * ou em estado inválido para cancelamento
     */
    public boolean cancelarPedido(int idPedido) {
        Pedido pedido = pedidos.stream().filter(p -> p.getId() == idPedido).findFirst().orElse(null);
        if (pedido == null) { System.out.println("[Erro] Pedido #" + idPedido + " nao encontrado."); return false; }
        try {
            pedido.cancelar();
            for (Extrato e : extratos) if (e.getIdPedido() == idPedido) { e.marcarCancelado(pedido.getValorCancelamento()); e.imprimir(); break; }
            for (EstacaoPreparo est : EstacaoPreparo.estacoes) {
                if (est.getIdPedidoAtual() != null && est.getIdPedidoAtual() == idPedido) {
                    est.liberar();
                    Pedido prox = filaPedidos.desenfileirar();
                    if (prox != null) { est.alocarPedido(prox.getId()); prox.setStatus(Pedido.Status.EM_PREPARO); }
                    break;
                }
            }
            salvarTudo();
            return true;
        } catch (IllegalStateException ex) {
            System.out.println("[Erro] " + ex.getMessage());
            return false;
        }
    }
     /**
     * Remove um pedido do sistema pelo seu identificador e persiste a lista.
     *
     * @param idPedido identificador do pedido a remover
     * @return {@code true} se removido; {@code false} se não encontrado
     */
    public boolean removerPedido(int idPedido) {
        boolean ok = pedidos.removeIf(p -> p.getId() == idPedido);
        if (ok) salvarTudo();
        return ok;
    }

    /**
     * Registra o recebimento de um ingrediente no estoque.
     * Se o ingrediente já existir, repõe a quantidade; caso contrário, cadastra
     * como novo. Persiste a lista e verifica alertas ao final.
     *
     * @param ingrediente ingrediente recebido com a quantidade a repor ou cadastrada
     */
    public void receberIngrediente(Ingrediente ingrediente) {
        Ingrediente ex = ingredientes.stream().filter(i -> i.getId() == ingrediente.getId()).findFirst().orElse(null);
        if (ex != null) { ex.repor(ingrediente.getQuantidadeAtual()); System.out.println("[Estoque] Reposicao: " + ex); }
        else { ingredientes.add(ingrediente); System.out.println("[Estoque] Novo: " + ingrediente); }
        PersistenciaJson.salvarLista(ingredientes, F_INGREDIENTES);
        verificarAlertas();
    }
    /**
     * Verifica o estoque de todos os ingredientes e imprime alertas para
     * os que estiverem abaixo do nível mínimo configurado.
     */
    public void verificarAlertas() {
        boolean alerta = false;
        for (Ingrediente i : ingredientes) {
            if (i.estaEmAlerta()) {
                System.out.println("[ALERTA] " + i.getNome() + ": " + i.getQuantidadeAtual()
                        + " " + i.getUnidadeMedida() + " (min: " + i.getQuantidadeMinima() + ")");
                alerta = true;
            }
        }
        if (!alerta) System.out.println("[Estoque] Todos em nivel adequado.");
    }
    /**
     * Dá baixa no estoque consumindo os ingredientes que compõem os produtos do pedido
     * (receita de cada produto via {@link Produto#getIdsIngredientes()}). Em seguida
     * verifica alertas e persiste a lista de ingredientes.
     *
     * @param idsProduto ids dos produtos efetivamente pedidos
     */
    private void atualizarEstoqueAposPedido(List<Integer> idsProduto) {
        for (int idProd : idsProduto) {
            Produto p = buscarProduto(idProd);
            if (p == null) continue;
            for (int idIng : p.getIdsIngredientes()) {
                Ingrediente ing = ingredientes.stream().filter(i -> i.getId() == idIng).findFirst().orElse(null);
                if (ing != null) ing.consumir(1.0); // 1 unidade do ingrediente por produto pedido
            }
        }
        verificarAlertas();
        PersistenciaJson.salvarLista(ingredientes, F_INGREDIENTES);
    }
      /**
      * Imprime todos os pedidos de um cliente específico (Alternativa 8),
     * cruzando o id do cliente com a lista de pedidos.
     * @param idCliente identificador do cliente
     */
    public void imprimirPedidosDoCliente(int idCliente) {
        Cliente c = buscarCliente(idCliente);
        if (c == null) { System.out.println("[Erro] Cliente nao encontrado."); return; }
        System.out.println("Pedidos do cliente " + c.getNome() + " (ID " + idCliente + "):");
        boolean algum = false;
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente) { System.out.println("  " + p); algum = true; }
        }
        if (!algum) System.out.println("  (nenhum pedido registrado)");
    }

    /* --- Mutação controlada de motoqueiros/regiões (encapsulamento) --- */

    /**
     * Adiciona um motoqueiro através do {@link GerenciadorEntregas} e persiste.
     * @param m motoqueiro a adicionar
     */
    public void adicionarMotoqueiro(Motoqueiro m) {
        gerenciadorEntregas.adicionarMotoqueiro(m, this.motoqueiros);
        PersistenciaJson.salvarLista(motoqueiros, F_MOTOQUEIROS);
    }
    /**
     * Remove um motoqueiro respeitando o mínimo do {@link GerenciadorEntregas} e persiste.
     * @param id id do motoqueiro
     * @return {@code true} se removido
     */
    public boolean removerMotoqueiro(int id) {
        boolean ok = gerenciadorEntregas.removerMotoqueiro(id, this.motoqueiros);
        if (ok) PersistenciaJson.salvarLista(motoqueiros, F_MOTOQUEIROS);
        return ok;
    }
    /**
     * Cria uma região com id automático, adiciona via gerenciador e persiste.
     * @param nome nome da região
     * @return a região criada
     */
    public Regiao adicionarRegiao(String nome) {
        int novoId = regioes.stream().mapToInt(Regiao::getId).max().orElse(0) + 1;
        Regiao r = new Regiao(novoId, nome);
        gerenciadorEntregas.adicionarRegiao(r, this.regioes);
        PersistenciaJson.salvarLista(regioes, F_REGIOES);
        return r;
    }
    /**
     * Remove uma região e persiste.
     * @param id id da região
     * @return {@code true} se removida
     */
    public boolean removerRegiao(int id) {
        boolean ok = regioes.removeIf(r -> r.getId() == id);
        if (ok) PersistenciaJson.salvarLista(regioes, F_REGIOES);
        return ok;
    }

    /* --- Getters: devolvem visões somente-leitura para proteger o estado interno --- */


    public List<Cliente> getClientes(){ 
        return Collections.unmodifiableList(clientes);
    }
    public List<Pedido> getPedidos(){ 
        return Collections.unmodifiableList(pedidos); 
    }
    public List<Produto> getProdutos(){
        return Collections.unmodifiableList(produtos);
    }
    public List<Ingrediente> getIngredientes(){ 
       return Collections.unmodifiableList(ingredientes); 
    }
    public List<Colaborador> getColaboradores(){
        return Collections.unmodifiableList(colaboradores);
    }
    public List<Extrato> getExtratos(){
        return Collections.unmodifiableList(extratos);
    }
    public List<Venda> getVendas(){ 
        return Collections.unmodifiableList(vendas); 
    }
    public List<Adicional> getAdicionais(){
        return Collections.unmodifiableList(adicionais);
    }
    public Administrador getAdministrador(){
        return administrador; 
    }
    public GerenciadorEntregas getGerenciadorEntregas(){
        return gerenciadorEntregas; 
    }
    public FilaPedidos getFilaPedidos(){ 
        return filaPedidos; 
    }
    public List<Motoqueiro> getMotoqueiros() {
       return Collections.unmodifiableList(motoqueiros);
    }
    public List<Regiao> getRegioes() {
        return Collections.unmodifiableList(regioes);
    }
    public List<Entrega> getEntregas() {
        return Collections.unmodifiableList(entregas);
    }

    @Override
    public String toString() {
        return "Sistema{clientes=" + clientes.size() + ", pedidos=" + pedidos.size()
                + ", produtos=" + produtos.size() + ", colaboradores=" + colaboradores.size()
                + ", estacoes=" + EstacaoPreparo.estacoes.length + "}";
    }
}