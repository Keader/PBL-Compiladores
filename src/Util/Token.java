package Util;

import java.util.regex.Pattern;

public class Token implements Lib{

    private int id;
    private int nLinha;
    private int tipo;
    private String lexema;
    private String erro;
    private boolean isError;
    private int idUnico;

    /**
     * Construtor de Tokens comuns
     * @param id identificador do token
     * @param lexema lexema do token
     * @param nLinha numero da linha em que foi criado
     * @param tipo tipo do lexema, para diferenciar lexemas do mesmo identificador. Esta variavel contem o grupo do lexema no regex
     */
    public Token(int id, String lexema, int nLinha, int tipo) {
        this.id = id;
        this.lexema = lexema;
        this.nLinha = nLinha;
        isError = false;
        erro = "";
        this.tipo = tipo;
        idUnico = criaIdUnico();
    }

    /**
     * Construtor de erro auto-detectavel
     * @param lexema lexema do erro
     * @param nLinha numero da linha do erro
     * @param error flag de erro
     */
    public Token(String lexema, int nLinha, boolean error) {
        this.lexema = lexema;
        this.nLinha = nLinha;
        isError = error;
        erro = "";
        id = 0;
        tipo = 0;
        idUnico = 0;

        if(isError)
            autoDetectarError(lexema);
    }

    /**
     * Construtor para tokens de erro com mensagens
     * @param lexema lexema do erro
     * @param mensagem mensagem de erro
     * @param linha linha do erro
     * @param error flag de erro
     */
    public Token(String lexema, String mensagem, int linha, boolean error) {
        this.lexema = lexema;
        erro = mensagem;
        nLinha = linha;
        isError = error;
        id = 0;
        tipo = 0;
        idUnico = 0;
    }

    public int getId() {
        return id;
    }

    public String getLexema() {
        return lexema;
    }

    public int getnLinha() {
        return nLinha;
    }

    public String getErro() {
        return erro;
    }

    public boolean isIsError() {
        return isError;
    }

    public int getTipo() {
        return tipo;
    }

    public int getIdUnico(){
        return idUnico;
    }

    @Override
    public String toString() {
        if(isError)
            return nLinha + " " + lexema + " " + erro.toLowerCase();

    	return nLinha + " " + lexema + " " + PadraoRegex.values()[id].name().toLowerCase();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token aux = (Token) obj;
            if (aux.getId() == this.id && aux.getLexema().equals(this.lexema))
                return true;
        }
        return false;
    }

    /**
     * Metodo que auto-identifica o tipo do erro
     * @param error lexema do erro, para ser identificado
     */
    private void autoDetectarError(String error) {
        for (ErrorRegex regex : ErrorRegex.values()) {
            if (Pattern.matches(regex.valor, error)) {
                erro += regex.name();
                return;
            }
        }
        erro += "SIMBOLO_INVALIDO";
    }

    /**
     * Metodo que traduz o id e tipo do token e um identificador unico
     * @return numero do identificador
     */
    private int criaIdUnico(){

        switch (id){
            //Palavras reservadas
            case 0:{

              switch(tipo){
                  case  1: return TK_PROGRAMA;
                  case  2: return TK_CONST;
                  case  3: return TK_VAR;
                  case  4: return TK_FUNCAO;
                  case  5: return TK_INICIO;
                  case  6: return TK_FIM;
                  case  7: return TK_SE;
                  case  8: return TK_ENTAO;
                  case  9: return TK_SENAO;
                  case 10: return TK_ENQUANTO;
                  case 11: return TK_FACA;
                  case 12: return TK_LEIA;
                  case 13: return TK_ESCREVA;
                  case 14: return TK_INTEIRO;
                  case 15: return TK_REAL;
                  case 16: return TK_BOOLEANO;
                  case 17: return TK_VERDADEIRO;
                  case 18: return TK_FALSO;
                  case 19: return TK_CADEIA;
                  case 20: return TK_CARACTERE;
                  default: break;
              }
              break;
            }
            //Operadores
            case 1:{

                switch(tipo){
                  case  1: return TK_NAO;
                  case  2: return TK_E;
                  case  3: return TK_OU;
                  case  4: return TK_SOMA;
                  case  5: return TK_SUBTRACAO;
                  case  6: return TK_MULTIPLICACAO;
                  case  7: return TK_DIVISAO;
                  case  8: return TK_DIFERENTE;
                  case  9: return TK_IGUAL;
                  case 10: return TK_MENOR;
                  case 11: return TK_MENORIGUAL;
                  case 12: return TK_MAIOR;
                  case 13: return TK_MAIORIGUAL;
                  default: break;
              }
              break;
            }
            //Identificador
            case 2: return TK_ID;
            //Numero
            case 3: return TK_NUMERO;
            //Delimitadores
            case 4:{

                switch(tipo){
                  case  1: return TK_PONTOVIRGULA;
                  case  2: return TK_VIRGULA;
                  case  3: return TK_PARENTESE_A;
                  case  4: return TK_PARENTESE_F;
                  default: break;
              }
              break;
            }
            //Cadeia de Caractere
            case 5: return TK_CADEIA_DE_CARACTERES;
            case 6: return TK_CARACTERE_L;
            //Este caso nunca devera ocorrer
            default:
                break;
        }
        //Isso nunca devera ocorrer
        return -1;
    }
}
