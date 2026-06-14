import java.io.FileWriter;
import java.io.IOException;

/**
 * Utilitário para limpar todos os dados do sistema.
 * Execute esta classe diretamente Run File)
 * para zerar todos os JSONs.
 */
public class LimparDados {

    public static void main(String[] args) {
        String dir = "data/";

        limpar(dir + "clientes.json",      "[]");
        limpar(dir + "pedidos.json",       "[]");
        limpar(dir + "produtos.json",      "[]");
        limpar(dir + "colaboradores.json", "[]");
        limpar(dir + "extratos.json",      "[]");
        limpar(dir + "vendas.json",        "[]");
        limpar(dir + "ingredientes.json",  "[]");
        limpar(dir + "adicionais.json",    "[]");
        limpar(dir + "entregas.json",      "[]");
        limpar(dir + "motoqueiros.json",   "[]");
        limpar(dir + "regioes.json",       "[]");
        limpar(dir + "administrador.json",
                "{\"id\":1,\"nome\":\"Admin\",\"login\":\"admin\",\"senha\":\"admin123\",\"email\":\"admin@hamburgueria.com\"}");

        System.out.println("=====================================");
        System.out.println("  Todos os dados foram apagados!");
        System.out.println("  Login: admin  |  Senha: admin123");
        System.out.println("=====================================");
    }

    private static void limpar(String caminho, String conteudo) {
        try (FileWriter fw = new FileWriter(caminho)) {
            fw.write(conteudo);
            System.out.println("[OK] " + caminho);
        } catch (IOException e) {
            System.out.println("[ERRO] " + caminho + " — " + e.getMessage());
        }
    }
}
