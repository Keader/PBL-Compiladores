package AnalisadorSemantico;

import Util.Dicionario;

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
    public String toString(){
        switch (tipo){
            case VAR_NAO_DECL:
                return "A variavel: "+error+" utilizada na linha: "+linha+" nao foi declarada";
            case FUNC_NAO_DECL:
                return "A funcao: "+error+" utilizada na linha: "+linha+" nao foi declarada";
            case ATRIBUICAO_INVALIDA:
                return "Foi detectada uma atribuicao invalida na linha: "+linha;
            case OPC_INVALIDA:
                return "Foi detectado uma operacao invalida na linha: "+linha;
            case VAR_JA_DECL:
                return "A variavel: "+error+" declarada na linha: "+linha+" ja havia sido declarada";
            case DIFF_DIMENSOES:
                return "Quantidade de dimensoes usadas no Array/Matriz: "+error+" invalido(s), na linha: "+linha;
            case RETORNO_INVALIDO:
                return "Retorno: "+error+" invalido na linha: "+linha;
            case TIPOS_INCOMPATIVEIS:
                return "Tipos incompativeis detectado na linha: "+linha+" provado por: "+error;
            case QNT_PARAM_INVALIDOS:
                return "Quantidade de parametros invalidos na linha: "+linha+" na funcao: "+error;
            case TIPOS_PARAM_INVALIDOS:
                return "Tipos de parametros invalidos na funcao: "+error+" na linha: "+error;
            default:
                return "Unknown";
        }
    }
}
