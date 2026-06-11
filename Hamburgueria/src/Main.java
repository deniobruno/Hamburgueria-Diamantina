import model.*;
import sistema.Sistema;
import ui.Menu;


/**
 * Ponto de entrada do sistema. Executa testes de todas as funções (Alternativa 4)
 * e em seguida abre o menu interativo.
 */

/** Subclasse para demonstrar acesso ao contador protected (Estratégia B, Alternativa 11). */
class ProdutoDemo extends Produto {
    public ProdutoDemo() { super(0, "demo", 0); }
    public static int getContadorProtected() { return totalProdutosProtected; }
}

public class Main {

    public static void main(String[] args) {
        
        ProdutoDemo.getContadorProtected();

        Sistema s = Sistema.getInstancia();
        s.carregar();

        /** Menu interativo — volta ao login após cada logout */
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
// Testando o github