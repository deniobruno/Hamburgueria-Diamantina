package ui;

import model.Ingrediente;
import model.*;
import factorymethod.ProdutoFactory;
import factorymethod.HamburguerFactory;
import factorymethod.BebidaFactory;
import factorymethod.SobremesaFactory;
import relatorios.RelatorioVendas;
import sistema.Sistema;

import java.util.*;
/**
 * Camada de interface com o usuário via console.
 * <p>
 * Responsável por toda a interação de entrada e saída via {@link Scanner}.
 * Recebe a instância de {@link Sistema} no construtor e delega todas as
 * operações de negócio a ela. Aplica o padrão de projeto <b>Factory Method</b>
 * ao cadastrar produtos (selecionando a fábrica concreta conforme o tipo
 * digitado) e utiliza {@link Comparator} sem o compareTo para exibir listagens
 * ordenadas de clientes e pedidos.
 * </p>
 */
public class Menu {

    private final Scanner sc;
    private final Sistema sistema;
/**
* Cria o menu de interface, associando ele a  instância do sistema fornecida.
*
* @param sistema instância única de {@link Sistema} (Singleton)
*/
    public Menu(Sistema sistema) {
        this.sistema = sistema;
        this.sc = new Scanner(System.in);
    }
/**
* Exibe a tela de login e autentica o usuário.
* <p>
* Permite até 3 tentativas de login. Se o usuário digitar {@code "sair"}
* ou {@code "0"} no campo de login, retorna {@code null} sinalizando o
* encerramento do sistema. Após 3 tentativas falhas, o acesso é bloqueado.
* </p>
*
* @return o {@link Usuario} autenticado (Administrador ou Colaborador),
* ou {@code null} se o usuário optou por sair ou excedeu as tentativas
 */
    public Usuario telaLogin() {
        System.out.println("\n================================================");
        System.out.println("       Hamburgueria Diamantina       ");
        System.out.println("================================================");
        System.out.println("Dica: Digite 'sair' no login para desligar o sistema.\n");

        for (int i = 0; i < 3; i++) {
            System.out.print("Login: "); 
            String login = sc.nextLine().trim();

            /** Se digitar sair, encerra o programa de forma limpa */
            if (login.equalsIgnoreCase("sair") || login.equals("0")) {
                return null; 
            }

            System.out.print("Senha: "); 
            String senha = sc.nextLine().trim();

            Usuario u = sistema.autenticar(login, senha);
            if (u != null) { 
                System.out.println("Bem-vindo(a), " + u.getNome() + "!"); 
                return u; 
            }
            System.out.println("Credenciais invalidas. Tentativas: " + (2 - i) + " restante(s).");
        }
        System.out.println("Acesso bloqueado por excesso de tentativas."); 
        return null;
    }
/**
* Exibe e gerencia o menu principal do administrador.
* <p>
* Oferece 10 opções, o loop é encerrado quando o administrador
* seleciona a opção {@code 0} (Sair).
* </p>
*
* @param admin administrador autenticado na sessão atual
*/
    public void menuAdministrador(Administrador admin) {
        boolean ok = true;
        while (ok) {
            System.out.println("\n======= MENU ADMINISTRADOR =======");
            System.out.println("1. Colaboradores");
            System.out.println("2. Clientes");
            System.out.println("3. Produtos");
            System.out.println("4. Estoque");
            System.out.println("5. Relatorios");
            System.out.println("6. Alterar senha");
            System.out.println("7. Estacoes");
            System.out.println("8. Motoqueiros");   /** Exclusivo admin */
            System.out.println("9. Regioes");       /** Exclusivo admin */
            System.out.println("10. Info sistema");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            switch (sc.nextLine().trim()) {
                case "1"  -> menuColaboradores();
                case "2"  -> menuClientes();
                case "3"  -> menuProdutos();
                case "4"  -> menuEstoque();
                case "5"  -> menuRelatorios();
                case "6"  -> fluxoSenha(admin.getLogin());
                case "7"  -> EstacaoPreparo.imprimirStatus();
                case "8"  -> menuMotoqueiros();
                case "9"  -> menuRegioes();
                case "10" -> System.out.println(sistema + "\nTotal pedidos: " + Sistema.getTotalPedidosCriados());
                case "0"  -> ok = false;
                default   -> System.out.println("Opcao invalida.");
            }
        }
    }
/**
* Exibe e gerencia o menu principal do colaborador.
* <p>
* Oferece 12 opções operacionais, o loop é encerrado quando o
* colaborador seleciona a opção {@code 0} (Sair).
* </p>
*
* @param col colaborador autenticado na sessão atual
*/

    public void menuColaborador(Colaborador col) {
        boolean ok = true;
        while (ok) {
            System.out.println("\n======= MENU COLABORADOR =======");
            System.out.println("1. Novo pedido");
            System.out.println("2. Cancelar pedido");
            System.out.println("3. Ver pedidos");
            System.out.println("4. Buscar por periodo");
            System.out.println("5. Fila de espera");
            System.out.println("6. Concluir entrega");
            System.out.println("7. Clientes");
            System.out.println("8. Produtos");
            System.out.println("9. Estoque");
            System.out.println("10. Relatorios");
            System.out.println("11. Estacoes");
            System.out.println("12. Alterar senha");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            switch (sc.nextLine().trim()) {
                case "1"  -> fluxoNovoPedido(col.getId());
                case "2"  -> { System.out.print("ID do pedido: "); sistema.cancelarPedido(lerInt()); }
                case "3"  -> listarPedidos();
                case "4"  -> fluxoPesquisa();
                case "5"  -> sistema.getFilaPedidos().imprimirFila();
                case "6"  -> fluxoConcluirEntrega();
                case "7"  -> menuClientes();
                case "8"  -> menuProdutos();
                case "9"  -> menuEstoque();
                case "10" -> menuRelatorios();
                case "11" -> EstacaoPreparo.imprimirStatus();
                case "12" -> fluxoSenha(col.getLogin());
                case "0"  -> ok = false;
                default   -> System.out.println("Opcao invalida.");
            }
        }
    }

/**
* Exibe o submenu de gerenciamento de motoqueiros (exclusivo do administrador).
* Permite listar, cadastrar e remover motoqueiros.
*/
    private void menuMotoqueiros() {
        System.out.println("\n--- Motoqueiros --- 1.Listar 2.Cadastrar 3.Remover");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            /**Busca a lista diretamente do Sistema*/
            case "1" -> sistema.getMotoqueiros().forEach(System.out::println);
            case "2" -> {
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Telefone: ");String tel = sc.nextLine();
                System.out.print("ID da regiao: "); int idReg = lerInt();
                /**Passa a lista do Sistema como parâmetro */
                sistema.getGerenciadorEntregas().adicionarMotoqueiro(new Motoqueiro(nome, tel, idReg), sistema.getMotoqueiros());
                sistema.salvarTudo();
                System.out.println("Motoqueiro cadastrado.");
            }
            case "3" -> {
                System.out.print("ID: ");
                /** Passa a lista do Sistema como parâmetro */
                boolean ok = sistema.getGerenciadorEntregas().removerMotoqueiro(lerInt(), sistema.getMotoqueiros());
                System.out.println(ok ? "Removido." : "Nao encontrado ou minimo de 5 atingido.");
                if (ok) sistema.salvarTudo();
            }
        }
    }
/**
* Exibe o submenu de gerenciamento de regiões de entrega (exclusivo do administrador).
* Permite listar, cadastrar e remover regiões com geração automática de ID.
*/
    private void menuRegioes() {
        System.out.println("\n--- Regioes --- 1.Listar 2.Cadastrar 3.Remover");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            /**Busca a lista diretamente do Sistema*/
            case "1" -> sistema.getRegioes().forEach(System.out::println);
            case "2" -> {
                System.out.print("Nome da regiao: "); String nome = sc.nextLine().trim();
                int novoId = sistema.getRegioes().stream()
                        .mapToInt(Regiao::getId).max().orElse(0) + 1;
                /** Passa a lista do Sistema como parâmetro */
                sistema.getGerenciadorEntregas().adicionarRegiao(new Regiao(novoId, nome), sistema.getRegioes());
                sistema.salvarTudo();
                System.out.println("Regiao cadastrada com ID " + novoId + ".");
            }
            case "3" -> {
                System.out.print("ID: "); int id = lerInt();
                /** Busca a lista diretamente do Sistema*/
                boolean ok = sistema.getRegioes().removeIf(r -> r.getId() == id);
                if (ok) sistema.salvarTudo();
                System.out.println(ok ? "Removida." : "Nao encontrada.");
            }
        }
    }

/**
* Exibe o submenu de gerenciamento de colaboradores.
* Permite listar, incluir, editar e remover colaboradores do sistema.
*/
    private void menuColaboradores() {
        System.out.println("\n--- Colaboradores --- 1.Listar 2.Incluir 3.Editar 4.Remover");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            case "1" -> sistema.getColaboradores().forEach(System.out::println);
            case "2" -> {
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Login: ");String login = sc.nextLine();
                System.out.print("Senha: ");String senha = sc.nextLine();
                System.out.print("Cargo: ");String cargo = sc.nextLine();
                System.out.print("Telefone: ");String tel = sc.nextLine();
                sistema.incluirColaborador(new Colaborador(nome, login, senha, cargo, tel));
                System.out.println("Colaborador cadastrado.");
            }
            case "3" -> {
                System.out.print("ID: ");int id = lerInt();
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Cargo: ");String cargo = sc.nextLine();
                System.out.print("Tel: ");String tel = sc.nextLine();
                System.out.println(sistema.editarColaborador(id, nvl(nome), nvl(cargo), nvl(tel)) ? "Atualizado." : "Nao encontrado.");
            }
            case "4" -> { System.out.print("ID: "); System.out.println(sistema.removerColaborador(lerInt()) ? "Removido." : "Nao encontrado."); }
        }
    }
/**
* Exibe o submenu de gerenciamento de clientes.
* Permite listar, incluir, editar, remover e buscar clientes por nome.
*/
    private void menuClientes() {
        System.out.println("\n--- Clientes --- 1.Listar 2.Incluir 3.Editar 4.Remover 5.Buscar");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            case "1" -> listarClientes();
            case "2" -> {
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Telefone: ");String tel  = sc.nextLine();
                System.out.print("Endereco: ");String end  = sc.nextLine();
                sistema.incluirCliente(new Cliente(nome, tel, end));
                System.out.println("Cliente cadastrado.");
            }
            case "3" -> {
                System.out.print("ID: ");int id = lerInt();
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Tel: ");String tel = sc.nextLine();
                System.out.print("End: "); String end = sc.nextLine();
                System.out.println(sistema.editarCliente(id, nvl(nome), nvl(tel), nvl(end)) ? "Atualizado." : "Nao encontrado.");
            }
            case "4" -> { System.out.print("ID: "); System.out.println(sistema.removerCliente(lerInt()) ? "Removido." : "Nao encontrado."); }
            case "5" -> { System.out.print("Nome: "); sistema.buscarClientePorNome(sc.nextLine()).forEach(System.out::println); }
        }
    }
/**
* Exibe o submenu de gerenciamento de produtos do cardápio.
* <p>
* Quando incluir, aplica o padrão <b>Factory Method</b>: seleciona a fábrica
*concreta ({@link HamburguerFactory}, {@link BebidaFactory} ou
* {@link SobremesaFactory}) conforme o tipo digitado pelo usuario,
* e delega a instanciação do produto à fábrica escolhida.
* </p>
*/
    private void menuProdutos() {
        System.out.println("\n--- Produtos --- 1.Listar 2.Incluir 3.Editar 4.Remover");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            case "1" -> sistema.getProdutos().forEach(System.out::println);
            case "2" -> {
                System.out.print("ID: ");int id = lerInt();
                System.out.print("Descricao: "); String desc = sc.nextLine();
                System.out.print("Valor R$: ");double val = lerDouble();
                System.out.print("Tipo (hamburguer/bebida/sobremesa): "); String tipo = sc.nextLine().trim().toLowerCase();

                /** Aplicação do Padrão Factory Method pelo código Cliente (Menu)*/
                ProdutoFactory fabrica = null;
                
                switch (tipo) {
                    case "hamburguer":
                    case "lanche":
                        fabrica = new HamburguerFactory();
                        break;
                    case "bebida":
                    case "drink":
                        fabrica = new BebidaFactory();
                        break;
                    case "sobremesa":
                    case "doce":
                        fabrica = new SobremesaFactory();
                        break;
                    default:
                        System.out.println("[Erro] Tipo desconhecido. Produto nao cadastrado.");
                        break;
                }

                /** Se a fábrica for definida corretamente, ela cria o produto */
                if (fabrica != null) {
                    Produto novoProduto = fabrica.criar(id, desc, val);
                    sistema.incluirProduto(novoProduto);
                    System.out.println("Produto cadastrado com sucesso.");
                }
            }
            case "3" -> {
                System.out.print("ID: ");int id = lerInt();
                System.out.print("Nova descricao: ");String desc = sc.nextLine();
                System.out.print("Novo valor: ");double val = lerDouble();
                System.out.println(sistema.editarProduto(id, nvl(desc), val) ? "Atualizado." : "Nao encontrado.");
            }
            case "4" -> { System.out.print("ID: "); System.out.println(sistema.removerProduto(lerInt()) ? "Removido." : "Nao encontrado."); }
        }
    }
/**
* Exibe o submenu de gerenciamento de estoque de ingredientes.
* Permite visualizar o estoque atual, registrar recebimento de ingredientes
* e verificar alertas de estoque mínimo.
*/
    private void menuEstoque() {
        System.out.println("\n--- Estoque --- 1.Ver 2.Adicionar 3.Verificar alertas");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            case "1" -> sistema.getIngredientes().forEach(System.out::println);
            case "2" -> {
                System.out.print("ID: ");int id = lerInt();
                System.out.print("Nome: ");String nome = sc.nextLine();
                System.out.print("Quantidade: ");double qtd = lerDouble();
                System.out.print("Unidade: ");String un = sc.nextLine();
                System.out.print("Quantidade minima: ");double min = lerDouble();
                sistema.receberIngrediente(new Ingrediente(id, nome, qtd, un, min));
            }
            case "3" -> sistema.verificarAlertas();
        }
    }
/**
* Exibe o submenu de relatórios financeiros.
* Permite gerar relatório diário, mensal e balanço mensal.
*/
    private void menuRelatorios() {
        System.out.println("\n--- Relatorios --- 1.Dia 2.Mes 3.Balanco mensal");
        System.out.print("Opcao: ");
        switch (sc.nextLine().trim()) {
            case "1" -> { System.out.print("Data (Dia/Mes/Ano): ");RelatorioVendas.emitirDia(sistema.getVendas(), sistema.getPedidos(), sc.nextLine()); }
            case "2" -> { System.out.print("Mes/Ano: ");RelatorioVendas.emitirMes(sistema.getVendas(), sistema.getPedidos(), sc.nextLine()); }
            case "3" -> { System.out.print("Mes/Ano: ");RelatorioVendas.gerarBalanco(sistema.getPedidos(), sc.nextLine()); }
        }
    }
/**
* O passo a passo do colaborador pelo fluxo de cadastro de um novo pedido:
* seleciona cliente, os produtos, horário de entrega, adicionais
* e região, então delega a criação ao {@link Sistema#realizarPedido}.
*
* @param idCol identificador do colaborador que está registrando o pedido
*/
    private void fluxoNovoPedido(int idCol) {
        listarClientes();
        System.out.print("ID do cliente: ");  
        int idC = lerInt();
        
        sistema.getProdutos().forEach(System.out::println);
        
        /** Captura múltiplos produtos */
        System.out.print("IDs dos produtos (separados por virgula): ");  
        List<Integer> idsP = new ArrayList<>();
        String strP = sc.nextLine().trim();
        if (!strP.isEmpty()) {
            for (String x : strP.split(",")) { 
                try { idsP.add(Integer.parseInt(x.trim())); 
                } 
                catch (NumberFormatException ignored) {} 
            }
        }

        System.out.print("Horario entrega (HH:mm): "); String hora = sc.nextLine();
        
        System.out.print("Adicionais (IDs separados por virgula, Enter=nenhum): ");
        List<Integer> ads = new ArrayList<>();
        String s = sc.nextLine().trim();
        if (!s.isEmpty()) {
            for (String x : s.split(",")) { 
                try { ads.add(Integer.parseInt(x.trim())); } catch (NumberFormatException ignored) {} 
            }
        }
        
        sistema.getRegioes().forEach(System.out::println);
        System.out.print("ID da regiao: "); 
        int idR = lerInt();
        
   
        Pedido p = sistema.realizarPedido(idC, idsP, ads, hora, idCol, idR);
        if (p != null) System.out.println("Pedido realizado: " + p);
    }
/**
* O passo a passo do usuário pelo fluxo de pesquisa de pedidos por intervalo de
* data e horário, exibindo os resultados encontrados.
*/
    private void fluxoPesquisa() {
        System.out.print("Data inicio(Dia/Mes/Ano): "); 
        String di = sc.nextLine();
        System.out.print("Data fim(Dia/Mes/Ano): "); 
        String df = sc.nextLine();
        System.out.print("Hora inicio(HH:mm, Enter=ignorar): "); 
        String hi = sc.nextLine();
        System.out.print("Hora fim(HH:mm, Enter=ignorar): "); 
        String hf = sc.nextLine();
        List<Pedido> r = RelatorioVendas.pesquisarPorIntervalo(sistema.getPedidos(), di, df, nvl(hi), nvl(hf));
        System.out.println("Encontrados: " + r.size()); r.forEach(System.out::println);
    }
/**
* Solicita o ID da entrega e o horário de conclusão, delegando
* a operação ao {@link sistema.GerenciadorEntregas}.
*/
    private void fluxoConcluirEntrega() {
        System.out.print("ID da entrega: ");             
        int id = lerInt();
        System.out.print("Horario conclusao (HH:mm): "); 
        String h = sc.nextLine();
        /** Passa as listas necessárias para concluir a entrega */
        sistema.getGerenciadorEntregas().concluirEntrega(id, h, sistema.getEntregas(), sistema.getMotoqueiros());
    }
/**
* Lista todos os clientes cadastrados ordenados alfabeticamente pelo nome,
* utilizando um {@link Comparator} sem o compareTo.
*/
    private void listarClientes() {
        if (sistema.getClientes().isEmpty()){ 
            System.out.println("Nenhum cliente."); 
            return; 
        }
        List<Cliente> l = new ArrayList<>(sistema.getClientes());
       
        /** Passando um Comparator */ 
        l.sort(new Comparator<Cliente>() {
            @Override
            public int compare(Cliente c1, Cliente c2) {
                /** Compara a ordem alfabética das Strings */
                int resultado = c1.getNome().compareToIgnoreCase(c2.getNome());
                
                if (resultado > 0) {
                    return 1;  /** O nome 1 vem DEPOIS do nome 2 no alfabeto*/
                }
                
                if (resultado < 0) {
                    return -1; /** O nome 1 vem ANTES do nome 2 no alfabeto*/
                }
                
                return 0;      /** Os nomes são iguais*/
            }
        });
        l.forEach(System.out::println);
    }
    /**
    * Lista todos os pedidos cadastrados ordenados pelo ID em ordem crescente,
    * utilizando um {@link Comparator} sem o compareTo.
    */
    private void listarPedidos() {
        if (sistema.getPedidos().isEmpty()) { 
            System.out.println("Nenhum pedido."); 
            return; 
        }
        List<Pedido> l = new ArrayList<>(sistema.getPedidos());
    
        /** Passando o Comparator  */
        l.sort(new Comparator<Pedido>() {
            @Override
            public int compare(Pedido p1, Pedido p2) {
                if (p1.getId() > p2.getId()){
                return 1;  /** O ID 1 é maior, então vem depois */
               }
                if (p1.getId() < p2.getId()) {
                return -1; /** O ID 1 é menor, então vem antes */
               }
                return 0;      /** Os IDs são iguais */
               }
    });
    
    l.forEach(System.out::println);
}   
/**
* Pede a senha atual e a nova senha para o usuário identificado pelo login,
* delegando a alteração ao {@link Sistema#alterarSenha}.
*
* @param login login do usuário que deseja alterar a senha
 */
    private void fluxoSenha(String login) {
        System.out.print("Senha atual: "); 
        String atual = sc.nextLine();
        System.out.print("Nova senha: ");  
        String nova  = sc.nextLine();
        sistema.alterarSenha(login, atual, nova);
    }
/**
* Lê um valor inteiro da entrada do usuário.
* Retorna {@code 0} em caso de formato inválido.
*
* @return inteiro lido ou {@code 0} em caso de erro
*/
    private int lerInt() { 
        try { 
            return Integer.parseInt(sc.nextLine().trim()); 
        } 
        catch (NumberFormatException e) { 
            return 0; 
        } 
    }
/**
* Lê um valor decimal da entrada do usuário, aceitando vírgula como
* separador. Retorna {@code 0} em caso de formato inválido.
*
* @return double lido ou {@code 0} em caso de erro
*/
    private double lerDouble(){ 
        try{ 
            return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); 
        } 
        catch (NumberFormatException e){ 
            return 0; 
        } 
    }
/**
* Utilitário auxiliar que retorna {@code null} se a string for vazia ou nula,
* ou a string sem espaços extras caso contrário. Usado para distinguir
* campos não preenchidos (sem alteração) de campos intencionalmente em branco
* durante edições.
*
* @param s string a ser verificada
* @return a string sem espaços, ou {@code null} se vazia/nula
*/
    private String nvl(String s){ 
        return (s == null || s.trim().isEmpty()) ? null : s.trim(); 
    }
}