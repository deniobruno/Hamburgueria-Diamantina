package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário de persistência JSON com Gson.
 * Usa try-with-resources para alocação/desalocação segura de arquivos (Alternativa 14).
 */
public class PersistenciaJson {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> void salvarLista(List<T> lista, String caminho) {
        try (Writer writer = new FileWriter(caminho)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("[Erro] Salvar " + caminho + ": " + e.getMessage());
        }
    }

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

    public static <T> void salvarObjeto(T objeto, String caminho) {
        try (Writer writer = new FileWriter(caminho)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.err.println("[Erro] Salvar objeto " + caminho + ": " + e.getMessage());
        }
    }

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

    public static void garantirDiretorio(String diretorio) {
        File dir = new File(diretorio);
        if (!dir.exists()) dir.mkdirs();
    }
}
