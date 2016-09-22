package AnalisadorSintatico;

import Util.Dicionario;
import static Util.Dicionario.*;

/**
 *
 * @author Bradley
 */
public class ErroSintatico implements Dicionario{

    private int tokenEsperado;
    private int tokenObtido;
    private boolean esperado;

    /**
     * Construtor para erros esperados, ou seja, esperava-se X elemento e recebeu Y elemento.
     * @param tokenEsperado Token esperado pelo programa
     * @param tokenObtido Token obtido pelo programa
     */
    public ErroSintatico(int tokenEsperado, int tokenObtido){
        this.tokenEsperado = tokenEsperado;
        this.tokenObtido = tokenObtido;
        esperado = true;
    }

    /**
     * Construtor utilizado sempre que houver entradas que nao sao esperadas pelo programa
     * Entra nos casos que o programador adiciona coisas alem do que deveria.
     * @param tokenObtido
     */
    public ErroSintatico(int tokenObtido){
        tokenEsperado = 0;
        this.tokenObtido = tokenObtido;
        esperado = false;
    }

    @Override
    public String toString() {
        if(!esperado)
            return "A entrada: " +conversorIdString(tokenObtido) + "nao era esperada neste contexto.";
        return "Era esperado: " + conversorIdString(tokenEsperado) + " porem foi obtido: " + conversorIdString(tokenObtido);
    }

//Havera mais metodos aqui?! Onde eles estão? Onde eles vivem? Veja sexta Feira, no Globo Reporter.

}
