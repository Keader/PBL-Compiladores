package Util;

/**
 * Esta interface representa um dicionario.
 */
public interface Lib {
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
    public static final int TK_CARACTERE_L          = 41; //Caractere Literal

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
