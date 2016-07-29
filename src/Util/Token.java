package Util;

import Util.Jarvis.ErrorRegex;
import Util.Jarvis.PadraoRegex;
import java.util.regex.Pattern;

public class Token {

    private int id;
    private int nLinha;
    private String lexema;
    private String erro;
    private boolean isError;

    /**
     * Construtor de Tokens comuns
     * @param id identificador do token
     * @param lexema lexema do token
     * @param nLinha numero da linha em que foi criado
     */
    public Token(int id, String lexema, int nLinha) {
        this.id = id;
        this.lexema = lexema;
        this.nLinha = nLinha;
        isError = false;
        erro = "";
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
        erro += "SIMBOLO_DESCONHECIDO";
    }
}
