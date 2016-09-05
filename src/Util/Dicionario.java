package Util;

/**
 * Esta interface representa um dicionario.
 */
public interface Dicionario {
    //Palavras Reservadas
    public static final int TK_PROGRAMA             =  1;
    public static final int TK_CONST                =  2;
    public static final int TK_VAR                  =  3;
    public static final int TK_FUNCAO               =  4;
    public static final int TK_INICIO               =  5;
    public static final int TK_FIM                  =  6;
    public static final int TK_SE                   =  7;
    public static final int TK_ENTAO                =  8;
    public static final int TK_SENAO                =  9;
    public static final int TK_ENQUANTO             = 10;
    public static final int TK_FACA                 = 11;
    public static final int TK_LEIA                 = 12;
    public static final int TK_ESCREVA              = 13;
    public static final int TK_INTEIRO              = 14;
    public static final int TK_REAL                 = 15;
    public static final int TK_BOOLEANO             = 16;
    public static final int TK_VERDADEIRO           = 17;
    public static final int TK_FALSO                = 18;
    public static final int TK_CADEIA               = 19;
    public static final int TK_CARACTERE            = 20;
    //Operadores
    public static final int TK_NAO                  = 21;
    public static final int TK_E                    = 22;
    public static final int TK_OU                   = 23;
    public static final int TK_SOMA                 = 24;
    public static final int TK_SUBTRACAO            = 25;
    public static final int TK_MULTIPLICACAO        = 26;
    public static final int TK_DIVISAO              = 27;
    public static final int TK_DIFERENTE            = 28;
    public static final int TK_IGUAL                = 29;
    public static final int TK_MENOR                = 30;
    public static final int TK_MENORIGUAL           = 31;
    public static final int TK_MAIOR                = 32;
    public static final int TK_MAIORIGUAL           = 33;
    //Identificador
    public static final int TK_ID                   = 34;
    //Numero
    public static final int TK_NUMERO               = 35;
    //Delimitadores
    public static final int TK_PONTOVIRGULA         = 36;
    public static final int TK_VIRGULA              = 37;
    public static final int TK_PARENTESE_A          = 38;
    public static final int TK_PARENTESE_F          = 39;
    //Cadeia de Caracteres
    public static final int TK_CADEIA_DE_CARACTERES = 40;
    //Caractere Literal
    public static final int TK_CARACTERE_L          = 41;
	//EOF ($)
	public static final int TK_OEF                  = 42;
    //Espaco Livre do 42-99 para possiveis modificacoes nos items acima.
    //**************************************************************************
    //Regras
	/* Obs: R = Regra, C = condicao. A ausencia de C eh a condicao inicial.
	   Em casos que a gramatica possui varios "ou" eh preciso dividir em sub-regras para ficar no padrao
	   Exemplo:
	   S-> X | EPSILON
	   S-> W | P
	  No caso do exemplo acima, ficaria: S e S_C2.
	*/
    public static final int R_PROGRAMA                    = 100;
    public static final int R_PROGRAMA_C2                 = 101;
    public static final int R_PROGRAMA_C3                 = 102;
    public static final int R_INICIO_CONST_VAR_FUNC       = 103;
    public static final int R_DEC_CONST_VAR_DERIVADA      = 104;
    public static final int R_DEC_CONST_VAR_DERIVADA_C2   = 105;
    public static final int R_INICIO_VAR_FUNC             = 106;
    public static final int R_DEC_CONST                   = 107;
    public static final int R_DEC_CONST_CONTINUO          = 108;
    public static final int R_DEC_CONST_I                 = 109;
    public static final int R_DEC_CONST_I_C2              = 110;
    public static final int R_DEC_CONST_II                = 111;
    public static final int R_DEC_CONST_II_C2             = 112;
    public static final int R_DEC_VAR                     = 113;
    public static final int R_DEC_VAR_CONTINUO            = 114;
    public static final int R_DEC_VAR_I                   = 115;
    public static final int R_DEC_VAR_I_C2                = 116;
    public static final int R_DEC_VAR_II                  = 117;
    public static final int R_DEC_VAR_II_C2               = 118;
    public static final int R_DEC_FUNC                    = 119;
    public static final int R_DEC_FUNC_C2                 = 120;
    public static final int R_DEC_FUNC_I                  = 121;
    public static final int R_DEC_FUNC_I_C2               = 122;
    public static final int R_PARAMETROS                  = 123;
    public static final int R_PARAMETROS_C2               = 124;
    public static final int R_PARAMETROS_I                = 125;
    public static final int R_PARAMETROS_I_C2             = 126;
    public static final int R_DEC_MAIN                    = 127;
    public static final int R_EXP_RELACIONAL_BOOL         = 128;
    public static final int R_EXP_CONJUNTA                = 129;
    public static final int R_EXP_CONJUNTA_I              = 130;
    public static final int R_EXP_CONJUNTA_I_C2           = 131;
    public static final int R_EXP_RELACIONAL              = 132;
    public static final int R_EXP_RELACIONAL_I            = 133;
    public static final int R_EXP_RELACIONAL_I_C2         = 134;
    public static final int R_OPERAR_RELACIONALMENTE      = 135;
    public static final int R_OPERAR_RELACIONALMENTE_C2   = 136;
    public static final int R_OP_RELACIONAL               = 137;
    public static final int R_OP_RELACIONAL_C2            = 138;
    public static final int R_OP_RELACIONAL_C3            = 139;
    public static final int R_OP_RELACIONAL_C4            = 140;
    public static final int R_OP_RELACIONAL_C5            = 141;
    public static final int R_NOT_OPC                     = 142;
    public static final int R_NOT_OPC_C2                  = 143;
    public static final int R_EXP_SIMPLES                 = 144;
    public static final int R_EXP_SIMPLES_C2              = 145;
    public static final int R_TERMO                       = 146;
    public static final int R_TERMO_I                     = 147;
    public static final int R_TERMO_I_C2                  = 148;
    public static final int R_FATOR                       = 149;
    public static final int R_FATOR_C2                    = 150;
    public static final int R_FATOR_C3                    = 151;
    public static final int R_FATOR_C4                    = 152;
    public static final int R_FATOR_C5                    = 153;
    public static final int R_FATOR_C6                    = 154;
    public static final int R_FATOR_C7                    = 155;
    public static final int R_FATOR_I                     = 156;
    public static final int R_FATOR_I_C2                  = 157;
    public static final int R_OP_MAIS_MENOS               = 158;
    public static final int R_OP_MAIS_MENOS_C2            = 159;
    public static final int R_OP_MULTI_DIV                = 160;
    public static final int R_OP_MULTI_DIV_C2             = 161;
    public static final int R_ID_FUNCAO_E_OUTROS          = 162;
    public static final int R_ID_FUNCAO_E_OUTROS_C2       = 163;
    public static final int R_ID_FUNCAO_E_OUTROS_DERIVADO = 164;
    public static final int R_POSSIBLE_FUNC               = 165;
    public static final int R_POSSIBLE_FUNC_C2            = 166;
    public static final int R_RETORNO_FUNC                = 167;
    public static final int R_CORPO                       = 168;
    public static final int R_CORPO_C2                    = 169;
    public static final int R_COMANDOS                    = 170;
    public static final int R_COMANDOS_C2                 = 171;
    public static final int R_COMANDOS_C3                 = 172;
    public static final int R_COMANDOS_C4                 = 173;
    public static final int R_COMANDOS_C5                 = 174;
    public static final int R_COMANDOS_C6                 = 175;
    public static final int R_COMANDOS_C7                 = 176;
    public static final int R_CHAMA_OU_ATRIBUI            = 177;
    public static final int R_CHAMA_OU_ATRIBUI_C2         = 178;
    public static final int R_WHOS_NEXT                   = 179;
    public static final int R_WHOS_NEXT_C2                = 180;
    public static final int R_NOVO_ESCOPO                 = 181;
    public static final int R_ARRAY                       = 182;
    public static final int R_ARRAY_C2                    = 183;
    public static final int R_ARRAY_I                     = 184;
    public static final int R_ARRAY_I_C2                  = 185;
    public static final int R_ARRAY_INDEXES               = 186;
    public static final int R_ARRAY_PARAM                 = 187;
    public static final int R_ARRAY_PARAM_C2              = 188;
    public static final int R_ARRAY_PARAM_I               = 189;
    public static final int R_ARRAY_PARAM_I_C2            = 190;
    public static final int R_ARRAY_INDEXES_OPT           = 191;
    public static final int R_ARRAY_INDEXES_OPT_C2        = 192;
    public static final int R_DEC_LEITURA                 = 193;
    public static final int R_LEITURA_I                   = 194;
    public static final int R_LEITURA_I_C2                = 195;
    public static final int R_DEC_ESCRITA                 = 196;
    public static final int R_ESCREVIVEL_I                = 197;
    public static final int R_ESCREVIVEL_I_C2             = 198;
    public static final int R_ESCREVIVEL                  = 199;
    public static final int R_ESCREVIVEL_C2               = 200;
    public static final int R_TERMO_E                     = 201;
    public static final int R_TERMO_I_E                   = 202;
    public static final int R_TERMO_I_E_C2                = 203;
    public static final int R_FATOR_E                     = 204;
    public static final int R_FATOR_E_C2                  = 205;
    public static final int R_FATOR_E_C3                  = 206;
    public static final int R_FATOR_E_C4                  = 207;
    public static final int R_FATOR_E_C5                  = 208;
    public static final int R_FATOR_I_E                   = 209;
    public static final int R_FATOR_I_E_C2                = 210;
    public static final int R_DEC_SE                      = 211;
    public static final int R_ELSE_OPC                    = 212;
    public static final int R_ELSE_OPC_C2                 = 213;
    public static final int R_DEC_ENQUANTO                = 214;
    public static final int R_PASSA_PARAM                 = 215;
    public static final int R_PASSA_PARAM_C2              = 216;
    public static final int R_PASSA_PARAM_I               = 217;
    public static final int R_PASSA_PARAM_I_C2            = 218;
    public static final int R_EPSILON                     = 219;

    //Enums
    public enum PadraoRegex {
        PALAVRA_RESERVADA("(programa)|(const)|(var)|(funcao)|(inicio)|(fim)|(se)|(entao)|(senao)|(enquanto)|(faca)|(leia)|(escreva)|(inteiro)|(real)|(booleano)|(verdadeiro)|(falso)|(cadeia)|(caractere)"),
        OPERADOR("(nao)|(e)|(ou)|(\\+)|(\\-)|(\\*)|(\\/)|(<>)|(=)|(<)|(<=)|(>)|(>=)"),
        ID("([a-zA-Z])([a-zA-Z]|(\\d)|(_))*?"),
        NRO("\\d+(\\.\\d+)?"),
        DELIMITADOR("(;)|(,)|(\\()|(\\))"),
        CADEIA_DE_CARACTERES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
        CARACTERE("'([a-zA-Z]|\\d)'");

        public String valor;

        private PadraoRegex(String valor) {
            this.valor = valor;
        }
    }

    public enum ErrorRegex {
        NRO_MAL_FORMADO("(\\d+\\.(.+)?)"),
        CARACTERE_MAL_FORMADO("^(')(.+)?");

        //Error na String e Comentario eh lancado sem o uso desse Enum.
        public String valor;

        private ErrorRegex(String valor) {
            this.valor = valor;
        }
    }

    public enum AuxRegex {
        SEPARADORES("(\")|(\\s+)|(')"),
        CASO_ESPECIAL("\\d+\\.");

        public String valor;

        private AuxRegex(String valor) {
            this.valor = valor;
        }
    }
}
