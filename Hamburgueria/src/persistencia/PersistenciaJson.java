package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

 /**
 * Utilitário de persistência em formato JSON utilizando a biblioteca Gson 2.10.1.
 * <p>
 * Todos os métodos são estáticos, a classe funciona como namespace de funções,
 * não sendo necessário instanciar. Utiliza {@code try-with-resources} para
 *  alocação/desalocação segura de arquivos  (Alternativa 14), evitando vazamentos 
 * de recursos mesmo em caso de exceção.
 * </p>
 *
 */
public class PersistenciaJson {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

     /**
     * Organiza em série uma lista de objetos para JSON e grava no arquivo especificado.
     * Erros de I/O são reportados no {@code System.err} sem lançar exceção.
     *
     * @param <T> tipo dos elementos da lista
     * @param lista lista de objetos a ser persistida
     * @param caminho caminho completo do arquivo de destino
     */
    public static <T> void salvarLista(List<T> lista, String caminho) {
        try (Writer writer = new FileWriter(caminho)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("[Erro] Salvar " + caminho + ": " + e.getMessage());
        }
    }
     /**
     * Lê um arquivo JSON e desserializa o conteúdo para uma lista do tipo informado.
     * Retorna lista vazia se o arquivo não existir ou ocorrer erro de leitura.
     *
     * @param <T> tipo dos elementos da lista
     * @param caminho caminho completo do arquivo a ser lido
     * @param tipo token de tipo Gson para a lista genérica (ex.: {@code new TypeToken<List<Cliente>>(){}.getType()})
     * @return lista desserializada, ou lista vazia em caso de erro ou ausência do arquivo
     */
    public static <T> List<T> carregarLista(String caminho, Type tipo) {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) 
            return new ArrayList<>();
        try (Reader reader = new FileReader(arquivo)) {
            List<T> r = gson.fromJson(reader, tipo);
            return r != null ? r : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[Erro] Carregar " + caminho + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
     /**
     * Serializa um único objeto para JSON e grava no arquivo especificado.
     * Erros de I/O são reportados no {@code System.err}.
     *
     * @param <T> tipo do objeto a ser persistido
     * @param objeto objeto a ser serializado
     * @param caminho caminho completo do arquivo de destino
     */
    public static <T> void salvarObjeto(T objeto, String caminho) {
        try (Writer writer = new FileWriter(caminho)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.err.println("[Erro] Salvar objeto " + caminho + ": " + e.getMessage());
        }
    }
   /**
     * Lê um arquivo JSON e desserializa o conteúdo para um objeto do tipo informado.
     * Retorna {@code null} se o arquivo não existir ou ocorrer erro.
     *
     * @param <T> tipo do objeto esperado
     * @param caminho caminho completo do arquivo a ser lido
     * @param tipo classe do tipo esperado (ex: {@code Administrador.class})
     * @return objeto desserializado, ou {@code null} em caso de erro ou ausência do arquivo
     */
    public static <T> T carregarObjeto(String caminho, Class<T> tipo) {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) 
            return null;
        try (Reader reader = new FileReader(arquivo)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("[Erro] Carregar objeto " + caminho + ": " + e.getMessage());
            return null;
        }
    }
    /**
     * Cria o diretório informado (e todos os diretórios pai necessários),
     * caso ainda não existam.
     *
     * @param diretorio caminho do diretório a ser criado (ex: {@code "data/"})
     */
    public static void garantirDiretorio(String diretorio) {
        File dir = new File(diretorio);
        if (!dir.exists()) dir.mkdirs();
    }
}
