package Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * Esta interface representa um dicionario.
 */
public interface Dicionario {
	//singleton da tabela
	public static final int [][] tabela = montarTabela();
    static final int tamanhoTabela = 230;
    static final String arquivoTabela = "arquivos/Matriz_Compiladores_teste.csv";

	//Palavras Reservadas
    public static final int TK_PROGRAMA             = 0;
    public static final int TK_CONST                = 1;
    public static final int TK_VAR                  = 2;
    public static final int TK_FUNCAO               = 3;
    public static final int TK_INICIO               = 4;
    public static final int TK_FIM                  = 5;
    public static final int TK_SE                   = 6;
    public static final int TK_ENTAO                = 7;
    public static final int TK_SENAO                = 8;
    public static final int TK_ENQUANTO             = 9;
    public static final int TK_FACA                 = 10;
    public static final int TK_LEIA                 = 11;
    public static final int TK_ESCREVA              = 12;
    public static final int TK_INTEIRO              = 13;
    public static final int TK_REAL                 = 14;
    public static final int TK_CADEIA               = 15;
    public static final int TK_BOOLEANO             = 16;
    public static final int TK_CARACTERE            = 17;
    public static final int TK_VERDADEIRO           = 18;
    public static final int TK_FALSO                = 19;
    //Operadores
    public static final int TK_NAO                  = 20;
    public static final int TK_E                    = 21;
    public static final int TK_OU                   = 22;
    public static final int TK_SOMA                 = 23;
    public static final int TK_SUBTRACAO            = 24;
    public static final int TK_MULTIPLICACAO        = 25;
    public static final int TK_DIVISAO              = 26;
    public static final int TK_IGUAL                = 27;
    public static final int TK_DIFERENTE            = 28;
    public static final int TK_MENOR                = 29;
    public static final int TK_MENORIGUAL           = 30;
    public static final int TK_MAIOR                = 31;
    public static final int TK_MAIORIGUAL           = 32;
    //Identificador
    public static final int TK_ID                   = 33;
    //Cadeia de Caracteres
    public static final int TK_CADEIA_DE_CARACTERES = 34;
    //Caractere Literal
    public static final int TK_CARACTERE_L          = 35;
    //Numero
    public static final int TK_NUMERO               = 36;
    //Delimitadores
    public static final int TK_PONTOVIRGULA         = 37;
    public static final int TK_VIRGULA              = 38;
    public static final int TK_PARENTESE_A          = 39;
    public static final int TK_PARENTESE_F          = 40;
	//EOF ($)
	public static final int TK_EOF                  = 41;
	public static final int MAX_TOKEN_VALUE         = 42;
	//TODO favor ver isso aqui
	public static final int TK_EPSILON         		= 43;

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
    public static final int R_TIPO                        = 221;
    public static final int R_TIPO_C2                     = 222;
    public static final int R_TIPO_C3                     = 223;
    public static final int R_TIPO_C4                     = 224;
    public static final int R_TIPO_C5                     = 225;
    public static final int VOLTA_PRO_PAI                 = 226;

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

	public static int getRegraId(String nome){
		switch (nome) {
			case "R_PROGRAMA"					: return R_PROGRAMA;
			case "R_PROGRAMA_C2"				: return R_PROGRAMA_C2;
			case "R_PROGRAMA_C3"				: return R_PROGRAMA_C3;
			case "R_INICIO_CONST_VAR_FUNC"		: return R_INICIO_CONST_VAR_FUNC;
			case "R_DEC_CONST_VAR_DERIVADA"		: return R_DEC_CONST_VAR_DERIVADA;
			case "R_DEC_CONST_VAR_DERIVADA_C2"	: return R_DEC_CONST_VAR_DERIVADA_C2;
			case "R_INICIO_VAR_FUNC"			: return R_INICIO_VAR_FUNC;
			case "R_INICIO_FUNC"				: return R_INICIO_FUNC;
			case "R_DEC_CONST"					: return R_DEC_CONST;
			case "R_DEC_CONST_CONTINUO"			: return R_DEC_CONST_CONTINUO;
			case "R_DEC_CONST_I"				: return R_DEC_CONST_I;
			case "R_DEC_CONST_I_C2"				: return R_DEC_CONST_I_C2;
			case "R_DEC_CONST_II"				: return R_DEC_CONST_II;
			case "R_DEC_CONST_II_C2"			: return R_DEC_CONST_II_C2;
			case "R_DEC_VAR"					: return R_DEC_VAR;
			case "R_DEC_VAR_CONTINUO"			: return R_DEC_VAR_CONTINUO;
			case "R_DEC_VAR_I"					: return R_DEC_VAR_I;
			case "R_DEC_VAR_I_C2"				: return R_DEC_VAR_I_C2;
			case "R_DEC_VAR_II"					: return R_DEC_VAR_II;
			case "R_DEC_VAR_II_C2"				: return R_DEC_VAR_II_C2;
			case "R_DEC_FUNC"					: return R_DEC_FUNC;
			case "R_DEC_FUNC_C2"				: return R_DEC_FUNC_C2;
			case "R_DEC_FUNC_I"					: return R_DEC_FUNC_I;
			case "R_DEC_FUNC_I_C2"				: return R_DEC_FUNC_I_C2;
			case "R_PARAMETROS"					: return R_PARAMETROS;
			case "R_PARAMETROS_C2"				: return R_PARAMETROS_C2;
			case "R_PARAMETROS_I"				: return R_PARAMETROS_I;
			case "R_PARAMETROS_I_C2"			: return R_PARAMETROS_I_C2;
			case "R_DEC_MAIN"					: return R_DEC_MAIN;
			case "R_EXP_RELACIONAL_BOOL"		: return R_EXP_RELACIONAL_BOOL;
			case "R_EXP_CONJUNTA"				: return R_EXP_CONJUNTA;
			case "R_EXP_CONJUNTA_I"				: return R_EXP_CONJUNTA_I;
			case "R_EXP_CONJUNTA_I_C2"			: return R_EXP_CONJUNTA_I_C2;
			case "R_EXP_RELACIONAL"				: return R_EXP_RELACIONAL;
			case "R_EXP_RELACIONAL_I"			: return R_EXP_RELACIONAL_I;
			case "R_EXP_RELACIONAL_I_C2"		: return R_EXP_RELACIONAL_I_C2;
			case "R_OPERAR_RELACIONALMENTE"		: return R_OPERAR_RELACIONALMENTE;
			case "R_OPERAR_RELACIONALMENTE_C2"	: return R_OPERAR_RELACIONALMENTE_C2;
			case "R_OP_RELACIONAL"				: return R_OP_RELACIONAL;
			case "R_OP_RELACIONAL_C2"			: return R_OP_RELACIONAL_C2;
			case "R_OP_RELACIONAL_C3"			: return R_OP_RELACIONAL_C3;
			case "R_OP_RELACIONAL_C4"			: return R_OP_RELACIONAL_C4;
			case "R_OP_RELACIONAL_C5"			: return R_OP_RELACIONAL_C5;
			case "R_NOT_OPC"					: return R_NOT_OPC;
			case "R_NOT_OPC_C2"					: return R_NOT_OPC_C2;
			case "R_EXP_SIMPLES"				: return R_EXP_SIMPLES;
			case "R_EXP_SIMPLES_C2"				: return R_EXP_SIMPLES_C2;
			case "R_TERMO"						: return R_TERMO;
			case "R_TERMO_I"					: return R_TERMO_I;
			case "R_TERMO_I_C2"					: return R_TERMO_I_C2;
			case "R_FATOR"						: return R_FATOR;
			case "R_FATOR_C2"					: return R_FATOR_C2;
			case "R_FATOR_C3"					: return R_FATOR_C3;
			case "R_FATOR_C4"					: return R_FATOR_C4;
			case "R_FATOR_C5"					: return R_FATOR_C5;
			case "R_FATOR_C6"					: return R_FATOR_C6;
			case "R_FATOR_C7"					: return R_FATOR_C7;
			case "R_FATOR_I"					: return R_FATOR_I;
			case "R_FATOR_I_C2"					: return R_FATOR_I_C2;
			case "R_OP_MAIS_MENOS"				: return R_OP_MAIS_MENOS;
			case "R_OP_MAIS_MENOS_C2"			: return R_OP_MAIS_MENOS_C2;
			case "R_OP_MULTI_DIV"				: return R_OP_MULTI_DIV;
			case "R_OP_MULTI_DIV_C2"			: return R_OP_MULTI_DIV_C2;
			case "R_ID_FUNCAO_E_OUTROS"			: return R_ID_FUNCAO_E_OUTROS;
			case "R_ID_FUNCAO_E_OUTROS_C2"		: return R_ID_FUNCAO_E_OUTROS_C2;
			case "R_ID_FUNCAO_E_OUTROS_DERIVADO": return R_ID_FUNCAO_E_OUTROS_DERIVADO;
			case "R_POSSIBLE_FUNC"				: return R_POSSIBLE_FUNC;
			case "R_POSSIBLE_FUNC_C2"			: return R_POSSIBLE_FUNC_C2;
			case "R_RETORNO_FUNC"				: return R_RETORNO_FUNC;
			case "R_CORPO"						: return R_CORPO;
			case "R_CORPO_C2"					: return R_CORPO_C2;
			case "R_COMANDOS"					: return R_COMANDOS;
			case "R_COMANDOS_C2"				: return R_COMANDOS_C2;
			case "R_COMANDOS_C3"				: return R_COMANDOS_C3;
			case "R_COMANDOS_C4"				: return R_COMANDOS_C4;
			case "R_COMANDOS_C5"				: return R_COMANDOS_C5;
			case "R_COMANDOS_C6"				: return R_COMANDOS_C6;
			case "R_COMANDOS_C7"				: return R_COMANDOS_C7;
			case "R_CHAMA_OU_ATRIBUI"			: return R_CHAMA_OU_ATRIBUI;
			case "R_CHAMA_OU_ATRIBUI_C2"		: return R_CHAMA_OU_ATRIBUI_C2;
			case "R_WHOS_NEXT"					: return R_WHOS_NEXT;
			case "R_WHOS_NEXT_C2"				: return R_WHOS_NEXT_C2;
			case "R_NOVO_ESCOPO"				: return R_NOVO_ESCOPO;
			case "R_ARRAY"						: return R_ARRAY;
			case "R_ARRAY_C2"					: return R_ARRAY_C2;
			case "R_ARRAY_I"					: return R_ARRAY_I;
			case "R_ARRAY_I_C2"					: return R_ARRAY_I_C2;
			case "R_ARRAY_INDEXES"				: return R_ARRAY_INDEXES;
			case "R_ARRAY_PARAM"				: return R_ARRAY_PARAM;
			case "R_ARRAY_PARAM_C2"				: return R_ARRAY_PARAM_C2;
			case "R_ARRAY_PARAM_I"				: return R_ARRAY_PARAM_I;
			case "R_ARRAY_PARAM_I_C2"			: return R_ARRAY_PARAM_I_C2;
			case "R_ARRAY_INDEXES_OPT"			: return R_ARRAY_INDEXES_OPT;
			case "R_ARRAY_INDEXES_OPT_C2"		: return R_ARRAY_INDEXES_OPT_C2;
			case "R_DEC_LEITURA"				: return R_DEC_LEITURA;
			case "R_LEITURA_I"					: return R_LEITURA_I;
			case "R_LEITURA_I_C2"				: return R_LEITURA_I_C2;
			case "R_DEC_ESCRITA"				: return R_DEC_ESCRITA;
			case "R_ESCREVIVEL_I"				: return R_ESCREVIVEL_I;
			case "R_ESCREVIVEL_I_C2"			: return R_ESCREVIVEL_I_C2;
			case "R_ESCREVIVEL"					: return R_ESCREVIVEL;
			case "R_ESCREVIVEL_C2"				: return R_ESCREVIVEL_C2;
			case "R_TERMO_E"					: return R_TERMO_E;
			case "R_TERMO_I_E"					: return R_TERMO_I_E;
			case "R_TERMO_I_E_C2"				: return R_TERMO_I_E_C2;
			case "R_FATOR_E"					: return R_FATOR_E;
			case "R_FATOR_E_C2"					: return R_FATOR_E_C2;
			case "R_FATOR_E_C3"					: return R_FATOR_E_C3;
			case "R_FATOR_E_C4"					: return R_FATOR_E_C4;
			case "R_FATOR_E_C5"					: return R_FATOR_E_C5;
			case "R_FATOR_I_E"					: return R_FATOR_I_E;
			case "R_FATOR_I_E_C2"				: return R_FATOR_I_E_C2;
			case "R_DEC_SE"						: return R_DEC_SE;
			case "R_ELSE_OPC"					: return R_ELSE_OPC;
			case "R_ELSE_OPC_C2"				: return R_ELSE_OPC_C2;
			case "R_DEC_ENQUANTO"				: return R_DEC_ENQUANTO;
			case "R_PASSA_PARAM"				: return R_PASSA_PARAM;
			case "R_PASSA_PARAM_C2"				: return R_PASSA_PARAM_C2;
			case "R_PASSA_PARAM_I"				: return R_PASSA_PARAM_I;
			case "R_PASSA_PARAM_I_C2"			: return R_PASSA_PARAM_I_C2;
			case "R_EPSILON"					: return R_EPSILON;
			case "R_TIPO" 						: return R_TIPO;
			case "R_TIPO_C2" 					: return R_TIPO_C2;
			case "R_TIPO_C3"					: return R_TIPO_C3;
			case "R_TIPO_C4" 					: return R_TIPO_C4;
			case "R_TIPO_C5" 					: return R_TIPO_C5;
			default 							: return -1;
		}
	}

    public static int getIdProducao(int regra, int token){
    	if(tabela == null)
    		montarTabela();

		return tabela[regra][token];
    }

	static int[][] montarTabela() {
	 	try {
			BufferedReader leitor = new BufferedReader(new FileReader(arquivoTabela));
			//capturando quantidade de regras
			String [] primeiraLinha = leitor.readLine().split(";");
			int qtdRegras = Integer.parseInt(primeiraLinha[0]);
			int qtdTerminais = Integer.parseInt(primeiraLinha[1]);
			int [][] auxTabela = new int[tamanhoTabela][qtdTerminais];
			String [] auxLinha;

			//consumindo uma linha
			leitor.readLine();

			//preenche a tabela com -1
			for(int i = 0; i < tamanhoTabela; i++)
				Arrays.fill(auxTabela[i], -1);
			//preenche a tabela com a predicao
			for(int i = 0; i < qtdRegras; i++){
				auxLinha = leitor.readLine().split(";");
				for(int x = 1; x < auxLinha.length; x++)
					auxTabela[getRegraId(auxLinha[0])][x-1] = getRegraId(auxLinha[x]);
			}

			leitor.close();

			return auxTabela;
		}
    	catch (FileNotFoundException e) {
    		Debug.messagePane(e.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
    	catch (IOException e) {
    		Debug.messagePane(e.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    	}

	 	return null;
	}

	public static String conversorIdString(int token) {
        switch (token) {
            case TK_PROGRAMA:
                return "programa";
            case TK_CONST:
                return "const";
            case TK_VAR:
                return "var";
            case TK_FUNCAO:
                return "funcao";
            case TK_INICIO:
                return "inicio";
            case TK_FIM:
                return "fim";
            case TK_SE:
                return "se";
            case TK_ENTAO:
                return "entao";
            case TK_SENAO:
                return "senao";
            case TK_ENQUANTO:
                return "enquanto";
            case TK_FACA:
                return "faca";
            case TK_LEIA:
                return "leia";
            case TK_ESCREVA:
                return "escreva";
            case TK_INTEIRO:
                return "inteiro";
            case TK_REAL:
                return "real";
            case TK_BOOLEANO:
                return "booleano";
            case TK_VERDADEIRO:
                return "verdadeiro";
            case TK_FALSO:
                return "falso";
            case TK_CADEIA:
                return "cadeia";
            case TK_CARACTERE:
                return "caractere";
            case TK_NAO:
                return "nao";
            case TK_E:
                return "e";
            case TK_OU:
                return "ou";
            case TK_SOMA:
                return "+";
            case TK_SUBTRACAO:
                return "-";
            case TK_MULTIPLICACAO:
                return "*";
            case TK_DIVISAO:
                return "/";
            case TK_DIFERENTE:
                return "<>";
            case TK_IGUAL:
                return "=";
            case TK_MENOR:
                return "<";
            case TK_MENORIGUAL:
                return "<=";
            case TK_MAIOR:
                return ">";
            case TK_MAIORIGUAL:
                return ">=";
            case TK_ID:
                return "identificador";
            case TK_NUMERO:
                return "numero";
            case TK_PONTOVIRGULA:
                return ";";
            case TK_VIRGULA:
                return ",";
            case TK_PARENTESE_A:
                return "(";
            case TK_PARENTESE_F:
                return ")";
            case TK_CADEIA_DE_CARACTERES:
                return "cadeia de caracteres";
            case TK_CARACTERE_L:
                return "caractere (literal)";
            case TK_EOF:
                return "fim de arquivo($)";
            case R_PROGRAMA:
                return "R_PROGRAMA";
            case R_PROGRAMA_C2:
                return "R_PROGRAMA_C2";
            case R_PROGRAMA_C3:
                return "R_PROGRAMA_C3";
            case R_INICIO_CONST_VAR_FUNC:
                return "R_INICIO_CONST_VAR_FUNC";
            case R_DEC_CONST_VAR_DERIVADA:
                return "R_DEC_CONST_VAR_DERIVADA";
            case R_DEC_CONST_VAR_DERIVADA_C2:
                return "R_DEC_CONST_VAR_DERIVADA_C2";
            case R_INICIO_VAR_FUNC:
                return "R_INICIO_VAR_FUNC";
            case R_INICIO_FUNC:
                return "R_INICIO_FUNC";
            case R_DEC_CONST:
                return "R_DEC_CONST";
            case R_DEC_CONST_CONTINUO:
                return "R_DEC_CONST_CONTINUO";
            case R_DEC_CONST_I:
                return "R_DEC_CONST_I";
            case R_DEC_CONST_I_C2:
                return "R_DEC_CONST_I_C2";
            case R_DEC_CONST_II:
                return "R_DEC_CONST_II";
            case R_DEC_CONST_II_C2:
                return "R_DEC_CONST_II_C2";
            case R_DEC_VAR:
                return "R_DEC_VAR";
            case R_DEC_VAR_CONTINUO:
                return "R_DEC_VAR_CONTINUO";
            case R_DEC_VAR_I:
                return "R_DEC_VAR_I";
            case R_DEC_VAR_I_C2:
                return "R_DEC_VAR_I_C2";
            case R_DEC_VAR_II:
                return "R_DEC_VAR_II";
            case R_DEC_VAR_II_C2:
                return "R_DEC_VAR_II_C2";
            case R_DEC_FUNC:
                return "R_DEC_FUNC";
            case R_DEC_FUNC_C2:
                return "R_DEC_FUNC_C2";
            case R_DEC_FUNC_I:
                return "R_DEC_FUNC_I";
            case R_DEC_FUNC_I_C2:
                return "R_DEC_FUNC_I_C2";
            case R_PARAMETROS:
                return "R_PARAMETROS";
            case R_PARAMETROS_C2:
                return "R_PARAMETROS_C2";
            case R_PARAMETROS_I:
                return "R_PARAMETROS_I";
            case R_PARAMETROS_I_C2:
                return "R_PARAMETROS_I_C2";
            case R_DEC_MAIN:
                return "R_DEC_MAIN";
            case R_EXP_RELACIONAL_BOOL:
                return "R_EXP_RELACIONAL_BOOL";
            case R_EXP_CONJUNTA:
                return "R_EXP_CONJUNTA";
            case R_EXP_CONJUNTA_I:
                return "R_EXP_CONJUNTA_I";
            case R_EXP_CONJUNTA_I_C2:
                return "R_EXP_CONJUNTA_I_C2";
            case R_EXP_RELACIONAL:
                return "R_EXP_RELACIONAL";
            case R_EXP_RELACIONAL_I:
                return "R_EXP_RELACIONAL_I";
            case R_EXP_RELACIONAL_I_C2:
                return "R_EXP_RELACIONAL_I_C2";
            case R_OPERAR_RELACIONALMENTE:
                return "R_OPERAR_RELACIONALMENTE";
            case R_OPERAR_RELACIONALMENTE_C2:
                return "R_OPERAR_RELACIONALMENTE_C2";
            case R_OP_RELACIONAL:
                return "R_OP_RELACIONAL";
            case R_OP_RELACIONAL_C2:
                return "R_OP_RELACIONAL_C2";
            case R_OP_RELACIONAL_C3:
                return "R_OP_RELACIONAL_C3";
            case R_OP_RELACIONAL_C4:
                return "R_OP_RELACIONAL_C4";
            case R_OP_RELACIONAL_C5:
                return "R_OP_RELACIONAL_C5";
            case R_NOT_OPC:
                return "R_NOT_OPC";
            case R_NOT_OPC_C2:
                return "R_NOT_OPC_C2";
            case R_EXP_SIMPLES:
                return "R_EXP_SIMPLES";
            case R_EXP_SIMPLES_C2:
                return "R_EXP_SIMPLES_C2";
            case R_TERMO:
                return "R_TERMO";
            case R_TERMO_I:
                return "R_TERMO_I";
            case R_TERMO_I_C2:
                return "R_TERMO_I_C2";
            case R_FATOR:
                return "R_FATOR";
            case R_FATOR_C2:
                return "R_FATOR_C2";
            case R_FATOR_C3:
                return "R_FATOR_C3";
            case R_FATOR_C4:
                return "R_FATOR_C4";
            case R_FATOR_C5:
                return "R_FATOR_C5";
            case R_FATOR_C6:
                return "R_FATOR_C6";
            case R_FATOR_C7:
                return "R_FATOR_C7";
            case R_FATOR_I:
                return "R_FATOR_I";
            case R_FATOR_I_C2:
                return "R_FATOR_I_C2";
            case R_OP_MAIS_MENOS:
                return "R_OP_MAIS_MENOS";
            case R_OP_MAIS_MENOS_C2:
                return "R_OP_MAIS_MENOS_C2";
            case R_OP_MULTI_DIV:
                return "R_OP_MULTI_DIV";
            case R_OP_MULTI_DIV_C2:
                return "R_OP_MULTI_DIV_C2";
            case R_ID_FUNCAO_E_OUTROS:
                return "R_ID_FUNCAO_E_OUTROS";
            case R_ID_FUNCAO_E_OUTROS_C2:
                return "R_ID_FUNCAO_E_OUTROS_C2";
            case R_ID_FUNCAO_E_OUTROS_DERIVADO:
                return "R_ID_FUNCAO_E_OUTROS_DERIVADO";
            case R_POSSIBLE_FUNC:
                return "R_POSSIBLE_FUNC";
            case R_POSSIBLE_FUNC_C2:
                return "R_POSSIBLE_FUNC_C2";
            case R_RETORNO_FUNC:
                return "R_RETORNO_FUNC";
            case R_CORPO:
                return "R_CORPO";
            case R_CORPO_C2:
                return "R_CORPO_C2";
            case R_COMANDOS:
                return "R_COMANDOS";
            case R_COMANDOS_C2:
                return "R_COMANDOS_C2";
            case R_COMANDOS_C3:
                return "R_COMANDOS_C3";
            case R_COMANDOS_C4:
                return "R_COMANDOS_C4";
            case R_COMANDOS_C5:
                return "R_COMANDOS_C5";
            case R_COMANDOS_C6:
                return "R_COMANDOS_C6";
            case R_COMANDOS_C7:
                return "R_COMANDOS_C7";
            case R_CHAMA_OU_ATRIBUI:
                return "R_CHAMA_OU_ATRIBUI";
            case R_CHAMA_OU_ATRIBUI_C2:
                return "R_CHAMA_OU_ATRIBUI_C2";
            case R_WHOS_NEXT:
                return "R_WHOS_NEXT";
            case R_WHOS_NEXT_C2:
                return "R_WHOS_NEXT_C2";
            case R_NOVO_ESCOPO:
                return "R_NOVO_ESCOPO";
            case R_ARRAY:
                return "R_ARRAY";
            case R_ARRAY_C2:
                return "R_ARRAY_C2";
            case R_ARRAY_I:
                return "R_ARRAY_I";
            case R_ARRAY_I_C2:
                return "R_ARRAY_I_C2";
            case R_ARRAY_INDEXES:
                return "R_ARRAY_INDEXES";
            case R_ARRAY_PARAM:
                return "R_ARRAY_PARAM";
            case R_ARRAY_PARAM_C2:
                return "R_ARRAY_PARAM_C2";
            case R_ARRAY_PARAM_I:
                return "R_ARRAY_PARAM_I";
            case R_ARRAY_PARAM_I_C2:
                return "R_ARRAY_PARAM_I_C2";
            case R_ARRAY_INDEXES_OPT:
                return "R_ARRAY_INDEXES_OPT";
            case R_ARRAY_INDEXES_OPT_C2:
                return "R_ARRAY_INDEXES_OPT_C2";
            case R_DEC_LEITURA:
                return "R_DEC_LEITURA";
            case R_LEITURA_I:
                return "R_LEITURA_I";
            case R_LEITURA_I_C2:
                return "R_LEITURA_I_C2";
            case R_DEC_ESCRITA:
                return "R_DEC_ESCRITA";
            case R_ESCREVIVEL_I:
                return "R_ESCREVIVEL_I";
            case R_ESCREVIVEL_I_C2:
                return "R_ESCREVIVEL_I_C2";
            case R_ESCREVIVEL:
                return "R_ESCREVIVEL";
            case R_ESCREVIVEL_C2:
                return "R_ESCREVIVEL_C2";
            case R_TERMO_E:
                return "R_TERMO_E";
            case R_TERMO_I_E:
                return "R_TERMO_I_E";
            case R_TERMO_I_E_C2:
                return "R_TERMO_I_E_C2";
            case R_FATOR_E:
                return "R_FATOR_E";
            case R_FATOR_E_C2:
                return "R_FATOR_E_C2";
            case R_FATOR_E_C3:
                return "R_FATOR_E_C3";
            case R_FATOR_E_C4:
                return "R_FATOR_E_C4";
            case R_FATOR_E_C5:
                return "R_FATOR_E_C5";
            case R_FATOR_I_E:
                return "R_FATOR_I_E";
            case R_FATOR_I_E_C2:
                return "R_FATOR_I_E_C2";
            case R_DEC_SE:
                return "R_DEC_SE";
            case R_ELSE_OPC:
                return "R_ELSE_OPC";
            case R_ELSE_OPC_C2:
                return "R_ELSE_OPC_C2";
            case R_DEC_ENQUANTO:
                return "R_DEC_ENQUANTO";
            case R_PASSA_PARAM:
                return "R_PASSA_PARAM";
            case R_PASSA_PARAM_C2:
                return "R_PASSA_PARAM_C2";
            case R_PASSA_PARAM_I:
                return "R_PASSA_PARAM_I";
            case R_PASSA_PARAM_I_C2:
                return "R_PASSA_PARAM_I_C2";
            case R_EPSILON:
                return "R_EPSILON";
            case R_TIPO:
                return "R_TIPO";
            case R_TIPO_C2:
                return "R_TIPO_C2";
            case R_TIPO_C3:
                return "R_TIPO_C3";
            case R_TIPO_C4:
                return "R_TIPO_C4";
            case R_TIPO_C5:
                return "R_TIPO_C5";
            default:
                return "";
        }
	}
}
