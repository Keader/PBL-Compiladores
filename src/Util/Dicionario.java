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
    //Espaco Livre do 41-99 para possiveis modificacoes nos items acima.
    //**************************************************************************
    //Regras
    public static final int R_PROGRAMA                     = 100;
    public static final int R_INICIO_CONST_VAR_FUNC        = 101;
    public static final int R_DEC_CONST_VAR_DERIVADA       = 102;
    public static final int R_INICIO_VAR_FUNC              = 103;
    public static final int R_DEC_CONST                    = 104;
    public static final int R_DEC_CONST_CONTINUO           = 105;
    public static final int R_DEC_CONST_I                  = 106;
    public static final int R_DEC_CONST_II                 = 107;
    public static final int R_DEC_VAR                      = 108;
    public static final int R_DEC_VAR_CONTINUO             = 109;
    public static final int R_DEC_VAR_I                    = 110;
    public static final int R_DEC_VAR_II                   = 111;
    public static final int R_DEC_FUNC                     = 112;
    public static final int R_DEC_FUNC_I                   = 113;
    public static final int R_PARAMETROS                   = 114;
    public static final int R_PARAMETROS_I                 = 115;
    public static final int R_DEC_MAIN                     = 116;
    public static final int R_EXP_RELACIONAL_BOOL          = 117;
    public static final int R_EXP_CONJUNTA                 = 118;
    public static final int R_EXP_CONJUNTA_I               = 119;
    public static final int R_EXP_RELACIONAL               = 120;
    public static final int R_EXP_RELACIONAL_I             = 121;
    public static final int R_OPERAR_RELACIONALMENTE       = 122;
    public static final int R_OP_RELACIONAL                = 123;
    public static final int R_NOT_OPC                      = 124;
    public static final int R_EXP_SIMPLES                  = 125;
    public static final int R_TERMO                        = 126;
    public static final int R_TERMO_I                      = 127;
    public static final int R_FATOR                        = 128;
    public static final int R_FATOR_I                      = 129;
    public static final int R_OP_MAIS_MENOS                = 130;
    public static final int R_OP_MULTI_DIV                 = 131;
    public static final int R_ID_FUNCAO_E_OUTROS           = 132;
    public static final int R_ID_FUNCAO_E_OUTROS_DERIVADO  = 133;
    public static final int R_POSSIBLE_FUNC                = 134;
    public static final int R_RETORNO_FUNC                 = 135;
    public static final int R_CORPO                        = 136;
    public static final int R_COMANDOS                     = 137;
    public static final int R_CHAMA_OU_ATRIBUI             = 138;
    public static final int R_WHOS_NEXT                    = 139;
    public static final int R_NOVO_ESCOPO                  = 140;
    public static final int R_ARRAY                        = 141;
    public static final int R_ARRAY_I                      = 142;
    public static final int R_ARRAY_INDEXES                = 143;
    public static final int R_ARRAY_PARAM                  = 144;
    public static final int R_ARRAY_PARAM_I                = 145;
    public static final int R_ARRAY_INDEXES_OPT            = 146;
    public static final int R_DEC_LEITURA                  = 147;
    public static final int R_LEITURA_I                    = 148;
    public static final int R_DEC_ESCRITA                  = 149;
    public static final int R_ESCREVIVEL_I                 = 150;
    public static final int R_ESCREVIVEL                   = 151;
    public static final int R_TERMO_E                      = 152;
    public static final int R_TERMO_I_E                    = 153;
    public static final int R_FATOR_E                      = 154;
    public static final int R_FATOR_I_E                    = 155;
    public static final int R_DEC_SE                       = 156;
    public static final int R_ELSE_OPC                     = 157;
    public static final int R_DEC_ENQUANTO                 = 158;
    public static final int R_PASSA_PARAM                  = 159;
    public static final int R_PASSA_PARAM_I                = 160;
    
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
