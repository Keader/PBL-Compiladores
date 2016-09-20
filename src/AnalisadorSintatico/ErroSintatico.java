package AnalisadorSintatico;

import Util.Dicionario;
import static Util.Dicionario.conversorIdString;

/**
 *
 * @author Bradley
 */
public class ErroSintatico implements Dicionario{

    private int tokenEsperado;
    private int tokenObtido;

    public ErroSintatico(int tokenEsperado, int tokenObtido){
        this.tokenEsperado = tokenEsperado;
        this.tokenObtido = tokenObtido;
    }

    @Override
    public String toString() {
        return "Era esperado: " + conversorIdString(tokenEsperado) + " porem foi obtido: " + conversorIdString(tokenObtido);
    }

//Havera mais metodos aqui?! Onde eles estão? Onde eles vivem? Veja sexta Feira, no Globo Reporter.

}
