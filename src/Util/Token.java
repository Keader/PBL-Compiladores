package Util;

import Util.Jarvis.PadraoRegex;

public class Token {

    private int id;
    private int nLinha;
    private int nColuna;

    private String lexema;
    private String classe;
    
    public Token(int id, String lexema, int nLinha, int nColuna) {
        this.id = id;
        this.lexema = lexema;
        this.nLinha = nLinha;
        this.nColuna = nColuna;
    }

    public int getId() {
        return id;
    }

    public String getLexema() {
        return lexema;
    }

    public String getClasse() {
        if (classe == null) 
            classe = PadraoRegex.values()[id].name();
        return classe;
    }

    @Override
    public String toString() {
    	return nLinha + "\t" + lexema + "\t" + Jarvis.PadraoRegex.values()[id].name();
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
}
