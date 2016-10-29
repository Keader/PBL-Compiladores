package AnalisadorSemantico;

import Util.Dicionario;
import java.util.Objects;

/**
 *
 * @author Bradley
 */
public class ErroSemantico implements Dicionario{

    private String error;
    private int tipo;
    private int linha;

    public ErroSemantico(String error, int tipo, int linha){
        this.error = error;
        this.tipo = tipo;
        this.linha = linha;
    }

    public ErroSemantico(int tipo, int linha){
        error = "";
        this.tipo = tipo;
        this.linha = linha;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErroSemantico other = (ErroSemantico) obj;
        if (this.linha != other.linha) {
            return false;
        }
        if (!Objects.equals(this.error, other.error)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        switch (tipo){
            case VAR_NAO_DECL:
                return "A variavel: "+error+", utilizada na linha: "+linha+" nao foi declarada";
            case FUNC_NAO_DECL:
                return "A funcao: "+error+", utilizada na linha: "+linha+" nao foi declarada";
            case ATRIBUICAO_INVALIDA_CONST:
                return "Foi detectada uma atribuicao invalida na linha: "+linha+" na constante: "+error;
            case OPC_INVALIDA:
                return "Foi detectado uma operacao invalida na linha: "+linha;
            case VAR_JA_DECL:
                return "A variavel/funcao/constante: "+error+", declarada na linha: "+linha+" ja havia sido declarada";
            case DIFF_DIMENSOES:
                return "Quantidade de dimensoes usadas no Array/Matriz: "+error+" invalido(s), na linha: "+linha;
            case RETORNO_INVALIDO:
                return "Retorno: "+error+", invalido na linha: "+linha;
            case TIPOS_INCOMPATIVEIS:
                return "Tipos incompativeis detectado na linha: "+linha+" provocado por: "+error;
            case QNT_PARAM_INVALIDOS:
                return "Quantidade de parametros invalidos na linha: "+linha+" na funcao: "+error;
            case TIPOS_PARAM_INVALIDOS:
                return "Tipos de parametros invalidos na funcao: "+error+" na linha: "+linha;
            case VAR_NAO_INICIALIZADA:
                return "A variavel: "+error+" usada na linha: "+linha+" nao foi inicializada";
            case FUNC_EM_CONST:
                return "Na declaracao da constante: "+error+", foi feita uma chamada de funcao, linha: "+linha;
            case TIPO_ATRIBUICAO_INVALIDA:
                return error+ ", na linha: "+linha;
            case VAR_EM_CONST:
                return "Na declaracao da constante: "+error+", foi feita uma atribuicao de variavel, na linha: "+linha;
            case ARRAY_EM_CONST:
                return "Na declaracao da constante: "+error+", foi feita uma chamada de Array, na linha: "+linha;
            default:
                return "Unknown";
        }
    }
}
