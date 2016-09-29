package AnalisadorSintatico;

import Util.Dicionario;
import static Util.Dicionario.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bradley
 */
public class ErroSintatico implements Dicionario{

    private int tokenEsperado;
    private String tokenObtido;
    private boolean esperado;
    private List<Integer> tokensEsperados;
    private int linha;

    /**
     * Construtor para erros esperados, ou seja, esperava-se X elemento e recebeu Y elemento.
     * @param tokenEsperado Token esperado pelo programa
     * @param tokenObtido Token obtido pelo programa
     * @param linha numero da linha do token esperado
     */
    public ErroSintatico(int tokenEsperado, String tokenObtido, int linha){
        this.tokenEsperado = tokenEsperado;
        this.tokenObtido = tokenObtido;
        this.linha = linha;
        esperado = true;
        tokensEsperados = null;
    }

    /**
     * Construtor utilizado em casos de nao terminal da error
     * @param tokensEsperados lista de tokens esperados
     * @param tokenObtido token obtido
     * @param linha numero do token obtido
     */
    public ErroSintatico(List<Integer> tokensEsperados, String tokenObtido, int linha){
        tokenEsperado = -1;
        this.tokenObtido = tokenObtido;
        esperado = true;
        this.tokensEsperados = new ArrayList<>();
        this.tokensEsperados.addAll(tokensEsperados);
        this.linha = linha;
    }

    /**
     * Construtor utilizado sempre que houver entradas que nao sao esperadas pelo programa
     * Entra nos casos que o programador adiciona coisas alem do que deveria.
     *
     * @param tokenObtido token obtido
     * @param linha numero da linha do token
     */
    public ErroSintatico(String tokenObtido, int linha){
        tokenEsperado = 0;
        this.tokenObtido = tokenObtido;
        esperado = false;
        tokensEsperados = null;
        this.linha = linha;
    }

    @Override
    public String toString() {
        if (tokensEsperados != null){
            String acumulador = "";
            for (Integer atual: tokensEsperados)
                acumulador += "'" +conversorIdString(atual)+"' ";

            return linha + " -> Era esperado alguma das entradas a seguir: " + acumulador + ". Mas foi obtido: " + tokenObtido;
        }

        if(!esperado)
            return linha + " -> A entrada: '" + tokenObtido + "' nao era esperada neste contexto.";

        return linha + " -> Era esperada a entrada: '" + conversorIdString(tokenEsperado) + "'. Mas foi obtido: " + tokenObtido;
    }

}
