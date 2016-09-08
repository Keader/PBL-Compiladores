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
    public static final int TK_TIPO                 = 43; //TEM Q MUDAR DEPOIS, PREGUICA N DEIXA MUDAR TUDO AGORA
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
    //Espaco Livre do 43-99 para possiveis modificacoes nos items acima.
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
    public static final int R_INICIO_FUNC                 = 107;
    public static final int R_DEC_CONST                   = 108;
    public static final int R_DEC_CONST_CONTINUO          = 109;
    public static final int R_DEC_CONST_I                 = 110;
    public static final int R_DEC_CONST_I_C2              = 111;
    public static final int R_DEC_CONST_II                = 112;
    public static final int R_DEC_CONST_II_C2             = 113;
    public static final int R_DEC_VAR                     = 114;
    public static final int R_DEC_VAR_CONTINUO            = 115;
    public static final int R_DEC_VAR_I                   = 116;
    public static final int R_DEC_VAR_I_C2                = 117;
    public static final int R_DEC_VAR_II                  = 118;
    public static final int R_DEC_VAR_II_C2               = 119;
    public static final int R_DEC_FUNC                    = 120;
    public static final int R_DEC_FUNC_C2                 = 121;
    public static final int R_DEC_FUNC_I                  = 122;
    public static final int R_DEC_FUNC_I_C2               = 123;
    public static final int R_PARAMETROS                  = 124;
    public static final int R_PARAMETROS_C2               = 125;
    public static final int R_PARAMETROS_I                = 126;
    public static final int R_PARAMETROS_I_C2             = 127;
    public static final int R_DEC_MAIN                    = 128;
    public static final int R_EXP_RELACIONAL_BOOL         = 129;
    public static final int R_EXP_CONJUNTA                = 130;
    public static final int R_EXP_CONJUNTA_I              = 131;
    public static final int R_EXP_CONJUNTA_I_C2           = 132;
    public static final int R_EXP_RELACIONAL              = 133;
    public static final int R_EXP_RELACIONAL_I            = 134;
    public static final int R_EXP_RELACIONAL_I_C2         = 135;
    public static final int R_OPERAR_RELACIONALMENTE      = 136;
    public static final int R_OPERAR_RELACIONALMENTE_C2   = 137;
    public static final int R_OP_RELACIONAL               = 138;
    public static final int R_OP_RELACIONAL_C2            = 139;
    public static final int R_OP_RELACIONAL_C3            = 140;
    public static final int R_OP_RELACIONAL_C4            = 141;
    public static final int R_OP_RELACIONAL_C5            = 142;
    public static final int R_NOT_OPC                     = 143;
    public static final int R_NOT_OPC_C2                  = 144;
    public static final int R_EXP_SIMPLES                 = 145;
    public static final int R_EXP_SIMPLES_C2              = 146;
    public static final int R_TERMO                       = 147;
    public static final int R_TERMO_I                     = 148;
    public static final int R_TERMO_I_C2                  = 149;
    public static final int R_FATOR                       = 150;
    public static final int R_FATOR_C2                    = 151;
    public static final int R_FATOR_C3                    = 152;
    public static final int R_FATOR_C4                    = 153;
    public static final int R_FATOR_C5                    = 154;
    public static final int R_FATOR_C6                    = 155;
    public static final int R_FATOR_C7                    = 156;
    public static final int R_FATOR_I                     = 157;
    public static final int R_FATOR_I_C2                  = 158;
    public static final int R_OP_MAIS_MENOS               = 159;
    public static final int R_OP_MAIS_MENOS_C2            = 160;
    public static final int R_OP_MULTI_DIV                = 161;
    public static final int R_OP_MULTI_DIV_C2             = 162;
    public static final int R_ID_FUNCAO_E_OUTROS          = 163;
    public static final int R_ID_FUNCAO_E_OUTROS_C2       = 164;
    public static final int R_ID_FUNCAO_E_OUTROS_DERIVADO = 165;
    public static final int R_POSSIBLE_FUNC               = 166;
    public static final int R_POSSIBLE_FUNC_C2            = 167;
    public static final int R_RETORNO_FUNC                = 168;
    public static final int R_CORPO                       = 169;
    public static final int R_CORPO_C2                    = 170;
    public static final int R_COMANDOS                    = 171;
    public static final int R_COMANDOS_C2                 = 172;
    public static final int R_COMANDOS_C3                 = 173;
    public static final int R_COMANDOS_C4                 = 174;
    public static final int R_COMANDOS_C5                 = 175;
    public static final int R_COMANDOS_C6                 = 176;
    public static final int R_COMANDOS_C7                 = 177;
    public static final int R_CHAMA_OU_ATRIBUI            = 178;
    public static final int R_CHAMA_OU_ATRIBUI_C2         = 179;
    public static final int R_WHOS_NEXT                   = 180;
    public static final int R_WHOS_NEXT_C2                = 181;
    public static final int R_NOVO_ESCOPO                 = 182;
    public static final int R_ARRAY                       = 183;
    public static final int R_ARRAY_C2                    = 184;
    public static final int R_ARRAY_I                     = 185;
    public static final int R_ARRAY_I_C2                  = 186;
    public static final int R_ARRAY_INDEXES               = 187;
    public static final int R_ARRAY_PARAM                 = 188;
    public static final int R_ARRAY_PARAM_C2              = 189;
    public static final int R_ARRAY_PARAM_I               = 190;
    public static final int R_ARRAY_PARAM_I_C2            = 191;
    public static final int R_ARRAY_INDEXES_OPT           = 192;
    public static final int R_ARRAY_INDEXES_OPT_C2        = 193;
    public static final int R_DEC_LEITURA                 = 194;
    public static final int R_LEITURA_I                   = 195;
    public static final int R_LEITURA_I_C2                = 196;
    public static final int R_DEC_ESCRITA                 = 197;
    public static final int R_ESCREVIVEL_I                = 198;
    public static final int R_ESCREVIVEL_I_C2             = 199;
    public static final int R_ESCREVIVEL                  = 200;
    public static final int R_ESCREVIVEL_C2               = 201;
    public static final int R_TERMO_E                     = 202;
    public static final int R_TERMO_I_E                   = 203;
    public static final int R_TERMO_I_E_C2                = 204;
    public static final int R_FATOR_E                     = 205;
    public static final int R_FATOR_E_C2                  = 206;
    public static final int R_FATOR_E_C3                  = 207;
    public static final int R_FATOR_E_C4                  = 208;
    public static final int R_FATOR_E_C5                  = 209;
    public static final int R_FATOR_I_E                   = 210;
    public static final int R_FATOR_I_E_C2                = 211;
    public static final int R_DEC_SE                      = 212;
    public static final int R_ELSE_OPC                    = 213;
    public static final int R_ELSE_OPC_C2                 = 214;
    public static final int R_DEC_ENQUANTO                = 215;
    public static final int R_PASSA_PARAM                 = 216;
    public static final int R_PASSA_PARAM_C2              = 217;
    public static final int R_PASSA_PARAM_I               = 218;
    public static final int R_PASSA_PARAM_I_C2            = 219;
    public static final int R_EPSILON                     = 220;

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
