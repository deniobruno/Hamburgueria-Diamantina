package sistema;

import model.Adicional;
import model.Ingrediente;
import com.google.gson.reflect.TypeToken;
import model.*;
import persistencia.PersistenciaJson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe central do sistema de hamburgueria.
 * Gerencia todas as entidades e coordena as operações.
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

    public static Sistema getInstancia() {
        if (instancia == null) instancia = new Sistema();
        return instancia;
    }

    /** Retorna total de pedidos criados no sistema. */
    public static int getTotalPedidosCriados(){ 
        return Pedido.getTotalPedidosCriados(); 
    }

    /** Persistência */

    public void carregar() {
        PersistenciaJson.garantirDiretorio(DIR);
        clientes = PersistenciaJson.carregarLista(F_CLIENTES,new TypeToken<List<Cliente>>(){}.getType());
        pedidos = PersistenciaJson.carregarLista(F_PEDIDOS,new TypeToken<List<Pedido>>(){}.getType());
        produtos = PersistenciaJson.carregarLista(F_PRODUTOS,new TypeToken<List<Produto>>(){}.getType());
        ingredientes = PersistenciaJson.carregarLista(F_INGREDIENTES,new TypeToken<List<Ingrediente>>(){}.getType());
        colaboradores = PersistenciaJson.carregarLista(F_COLABORADORES,new TypeToken<List<Colaborador>>(){}.getType());
        extratos = PersistenciaJson.carregarLista(F_EXTRATOS,new TypeToken<List<Extrato>>(){}.getType());
        vendas = PersistenciaJson.carregarLista(F_VENDAS,new TypeToken<List<Venda>>(){}.getType());
        adicionais = PersistenciaJson.carregarLista(F_ADICIONAIS,new TypeToken<List<Adicional>>(){}.getType());
        administrador = PersistenciaJson.carregarObjeto(F_ADMIN,Administrador.class);
        motoqueiros = PersistenciaJson.carregarLista(F_MOTOQUEIROS,new TypeToken<List<Motoqueiro>>(){}.getType());
        regioes   = PersistenciaJson.carregarLista(F_REGIOES,new TypeToken<List<Regiao>>(){}.getType());
        
        if (administrador == null) {
            administrador = new Administrador("Admin", "admin", "admin123", "admin@hamburgueria.com");
            PersistenciaJson.salvarObjeto(administrador, F_ADMIN);
        }
        System.out.println("[Sistema] Carregado. Clientes: " + clientes.size()
                + " | Pedidos: " + pedidos.size() + " | Produtos: " + produtos.size());
    }

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
        
        PersistenciaJson.salvarObjeto(administrador, F_ADMIN);
    }

    /** Autenticação */

    public Usuario autenticar(String login, String senha) {
        if (administrador != null && administrador.autenticar(login, senha)) return administrador;
        for (Colaborador c : colaboradores) if (c.autenticar(login, senha)) return c;
        return null;
    }

    /** CRUD Clientes */

    public void incluirCliente(Cliente c) {
        clientes.add(c);
        PersistenciaJson.salvarLista(clientes, F_CLIENTES);
    }

    public boolean editarCliente(int id, String nome, String telefone, String endereco) {
        Cliente c = buscarCliente(id);
        if (c == null) return false;
        if (nome != null) c.setNome(nome);
        if (telefone != null) c.setTelefone(telefone);
        if (endereco != null) c.setEndereco(endereco);
        PersistenciaJson.salvarLista(clientes, F_CLIENTES);
        return true;
    }

    public boolean removerCliente(int id) {
        boolean ok = clientes.removeIf(c -> c.getId() == id);
        if (ok) PersistenciaJson.salvarLista(clientes, F_CLIENTES);
        return ok;
    }

    public Cliente buscarCliente(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Cliente> buscarClientePorNome(String nome) {
        String n = nome.toLowerCase();
        return clientes.stream().filter(c -> c.getNome().toLowerCase().contains(n)).collect(Collectors.toList());
    }

    /** CRUD Colaboradores */

    public void incluirColaborador(Colaborador c) {
        colaboradores.add(c);
        PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
    }

    public boolean editarColaborador(int id, String nome, String cargo, String telefone) {
        Colaborador c = buscarColaborador(id);
        if (c == null) return false;
        if (nome != null)c.setNome(nome);
        if (cargo != null) c.setCargo(cargo);
        if (telefone != null) c.setTelefone(telefone);
        PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
        return true;
    }

    public boolean removerColaborador(int id) {
        boolean ok = colaboradores.removeIf(c -> c.getId() == id);
        if (ok) PersistenciaJson.salvarLista(colaboradores, F_COLABORADORES);
        return ok;
    }

    public Colaborador buscarColaborador(int id){
        return colaboradores.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public boolean alterarSenha(String login, String senhaAtual, String novaSenha) {
        Usuario u = autenticar(login, senhaAtual);
        if (u == null) { System.out.println("[Erro] Senha atual incorreta."); return false; }
        u.setSenha(novaSenha);
        salvarTudo();
        System.out.println("[Sistema] Senha alterada com sucesso.");
        return true;
    }

    /**  CRUD Produtos */

    public void incluirProduto(Produto p) {
        produtos.add(p);
        PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
    }

    public boolean editarProduto(int id, String descricao, double valor) {
        Produto p = buscarProduto(id);
        if (p == null) return false;
        if (descricao != null)p.setDescricao(descricao);
        if (valor > 0)p.setValor(valor);
        PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
        return true;
    }

    public boolean removerProduto(int id) {
        boolean ok = produtos.removeIf(p -> p.getIdDescricao() == id);
        if (ok) PersistenciaJson.salvarLista(produtos, F_PRODUTOS);
        return ok;
    }

    public Produto buscarProduto(int id) {
        return produtos.stream().filter(p -> p.getIdDescricao() == id).findFirst().orElse(null);
    }

    public Pedido realizarPedido(int idCliente, List<Integer> idsProduto, List<Integer> idsAdicionais,
                                  String horarioEntrega, int idColaborador, int idRegiao) {
        
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null || idsProduto == null || idsProduto.isEmpty()) {
            System.out.println("[Erro] Cliente nao encontrado ou nenhum produto selecionado.");
            return null;
        }

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("Dia/Mes/Ano"));
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
        
        atualizarEstoqueAposPedido();
        return pedido;
    }

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

    public boolean removerPedido(int idPedido) {
        boolean ok = pedidos.removeIf(p -> p.getId() == idPedido);
        if (ok) salvarTudo();
        return ok;
    }


    public void receberIngrediente(Ingrediente ingrediente) {
        Ingrediente ex = ingredientes.stream().filter(i -> i.getId() == ingrediente.getId()).findFirst().orElse(null);
        if (ex != null) { ex.repor(ingrediente.getQuantidadeAtual()); System.out.println("[Estoque] Reposicao: " + ex); }
        else { ingredientes.add(ingrediente); System.out.println("[Estoque] Novo: " + ingrediente); }
        PersistenciaJson.salvarLista(ingredientes, F_INGREDIENTES);
        verificarAlertas();
    }

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

    private void atualizarEstoqueAposPedido() {
        for (Ingrediente i : ingredientes) i.consumir(0.1);
        verificarAlertas();
        PersistenciaJson.salvarLista(ingredientes, F_INGREDIENTES);
    }

    public List<Cliente> getClientes(){ 
        return clientes; 
    }
    public List<Pedido> getPedidos(){ 
        return pedidos; 
    }
    public List<Produto> getProdutos(){
        return produtos; 
    }
    public List<Ingrediente> getIngredientes(){ 
        return ingredientes; 
    }
    public List<Colaborador> getColaboradores(){
        return colaboradores;
    }
    public List<Extrato> getExtratos(){
        return extratos; 
    }
    public List<Venda> getVendas(){ 
        return vendas; 
    }
    public List<Adicional> getAdicionais(){
        return adicionais;
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
    
    /** Getters adicionados para expor as listas centralizadas */
    public List<Motoqueiro> getMotoqueiros() {
        return motoqueiros;
    }
    public List<Regiao> getRegioes() {
        return regioes;
    }
    public List<Entrega> getEntregas() {
        return entregas;
    }

    @Override
    public String toString() {
        return "Sistema{clientes=" + clientes.size() + ", pedidos=" + pedidos.size()
                + ", produtos=" + produtos.size() + ", colaboradores=" + colaboradores.size()
                + ", estacoes=" + EstacaoPreparo.estacoes.length + "}";
    }
}