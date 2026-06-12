import model.*;
import sistema.Sistema;
import ui.Menu;


/**
 * Ponto de entrada do sistema. Executa testes de todas as funções (Alternativa 4)
 * e em seguida abre o menu interativo.
 * <p>
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
        
        ProdutoDemo.getContadorProtected();

        Sistema s = Sistema.getInstancia();
        s.carregar();

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
}
